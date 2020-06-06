package ru.nstu.javaprog.net.server;

import ru.nstu.javaprog.net.dto.Parcel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.nstu.javaprog.net.util.NetworkUtils.MULTICAST_ID;
import static ru.nstu.javaprog.net.util.NetworkUtils.SERVER_ID;


final class ServerConnectionHandler {
    private final Map<Long, ClientRequestHandler> connections;
    private final ServerSocket serverSocket;
    private final ExecutorService connectionPool = Executors.newCachedThreadPool();
    private final ExecutorService sender = Executors.newFixedThreadPool(1);
    private Thread thread;

    ServerConnectionHandler(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        connections = Collections.synchronizedMap(new HashMap<>());
    }

    void start() {
        thread = new Thread(() -> {
            long sessionIds = 0;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    connectionPool.submit(
                            new ClientRequestHandler(sessionIds++, serverSocket.accept())
                    );
                } catch (IOException exception) {
                    System.err.println("Exception while accepting connection: "
                            + exception.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();
        System.out.println("Server was started");
    }

    void interrupt() {
        System.out.println("Interrupting...");
        connectionPool.shutdown();
        sender.shutdown();
        thread.interrupt();
        connections.forEach((id, service) -> service.close());
        try {
            serverSocket.close();
        } catch (IOException exception) {
            System.err.println("Exception while closing ServerSocket: "
                    + exception.getMessage());
        }
        System.out.println("Server was interrupted");
    }

    private class ClientRequestHandler implements Runnable {
        final long sessionId;
        final Socket socket;
        final ObjectInputStream inputStream;
        final ObjectOutputStream outputStream;

        ClientRequestHandler(long sessionId, Socket socket) throws IOException {
            this.sessionId = sessionId;
            this.socket = socket;
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outputStream.flush();
            inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        }

        @Override
        public void run() {
            connections.put(sessionId, this);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Object object;
                    object = inputStream.readObject();
                    if (object instanceof Parcel) {
                        Parcel parcel = (Parcel) object;
                        System.out.println("Client " + socket + ", request " + parcel.getRequest());
                        switch (parcel.getRequest()) {
                            case CONNECT:
                                connect();
                                update();
                                break;
                            case DISCONNECT:
                                Thread.currentThread().interrupt();
                                connections.remove(sessionId);
                                update();
                                break;
                            case GET:
                                get(parcel);
                                break;
                            case PROVIDE:
                                provide(parcel);
                                break;
                            default:
                                throw new IOException("Invalid request was received");
                        }
                    } else
                        throw new IOException("Invalid parcel was received");
                } catch (IOException | ClassNotFoundException exception) {
                    Thread.currentThread().interrupt();
                    connections.remove(sessionId);
                    System.err.println("Exception while running service: "
                            + exception.getMessage());
                }
            }
            close();
        }

        void connect() {
            sendRequest(SERVER_ID, sessionId, Parcel.Request.CONNECT, null);
        }

        void get(Parcel parcel) throws IOException {
            try {
                connections.get(parcel.getRecipient()).sendRequest(
                        parcel.getSender(),
                        parcel.getRecipient(),
                        Parcel.Request.PROVIDE,
                        null
                );
            } catch (Exception exception) {
                throw new IOException("Invalid data was received");
            }
        }

        void close() {
            try {
                socket.close();
                inputStream.close();
                outputStream.close();
            } catch (IOException exception) {
                System.err.println("Exception while closing service: " + exception.getMessage());
            }
        }

        void provide(Parcel parcel) throws IOException {
            try {
                connections.get(parcel.getRecipient()).sendRequest(
                        parcel.getSender(),
                        parcel.getRecipient(),
                        Parcel.Request.GET,
                        parcel.getData()
                );
            } catch (Exception exception) {
                throw new IOException("Invalid data was received");
            }
        }

        void update() {
            List<Long> availableConnections = new LinkedList<>(connections.keySet());
            connections.forEach((id, service) -> service.sendRequest(
                    SERVER_ID,
                    MULTICAST_ID,
                    Parcel.Request.UPDATE,
                    availableConnections
            ));
        }

        void sendRequest(long senderId, long recipientId, Parcel.Request request, Object data) {
            sender.submit(() -> {
                try {
                    outputStream.writeObject(new Parcel(senderId, recipientId, request, data));
                    outputStream.flush();
                } catch (IOException exception) {
                    System.err.println("Exception while sending request: "
                            + exception.getMessage());
                }
            });
        }
    }
}
