package ru.nstu.javaprog.net.client;

import javafx.util.Pair;
import ru.nstu.javaprog.net.dto.Parcel;
import ru.nstu.javaprog.util.Properties;
import ru.nstu.javaprog.view.Network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

import static ru.nstu.javaprog.net.util.NetworkUtils.INVALID_ID;
import static ru.nstu.javaprog.net.util.NetworkUtils.SERVER_ID;

public final class ConnectionHandler {
    private final Network network;
    private final String host;
    private final int port;
    private final ExecutorService sender = Executors.newFixedThreadPool(1);
    private long sessionId = INVALID_ID;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private volatile boolean isInterrupted;
    private Thread thread;

    public ConnectionHandler(String host, int port, Network network) {
        this.network = network;
        this.host = host;
        this.port = port;
    }

    // запускать не в event thread
    public void start() throws IOException {
        socket = new Socket(InetAddress.getByName(host), port);
        outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        outputStream.flush();
        inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        thread = new Thread(new ServerRequestHandler());
        thread.start();
        sendRequest(SERVER_ID, Parcel.Request.CONNECT, null);
    }

    // запускать не в event thread
    public void interrupt() {
        isInterrupted = true;
        thread.interrupt();
        try {
            sendRequest(SERVER_ID, Parcel.Request.DISCONNECT, null)
                    .get(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException exception) {
            System.err.println("Exception while sending disconnect request: "
                    + exception.getMessage());
        }
        sender.shutdown();
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException exception) {
            // ignore
        }
    }

    public void getProperties(long recipient) {
        sendRequest(recipient, Parcel.Request.GET, null);
    }

    @Override
    public String toString() {
        if (sessionId == INVALID_ID)
            throw new IllegalStateException("Invalid session id");
        return "session id " + sessionId;
    }

    public long getSessionId() {
        if (sessionId == INVALID_ID)
            throw new IllegalStateException("Invalid session id");
        return sessionId;
    }

    private Future<?> sendRequest(long recipient, Parcel.Request request, Object data) {
        return sender.submit(() -> {
            try {
                outputStream.writeObject(new Parcel(sessionId, recipient, request, data));
                outputStream.flush();
            } catch (IOException exception) {
                if (!isInterrupted)
                    network.crash("Connection lost");
            }
        });
    }

    private class ServerRequestHandler implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Object object = inputStream.readObject();
                    if (object instanceof Parcel) {
                        Parcel parcel = (Parcel) object;
                        switch (parcel.getRequest()) {
                            case CONNECT:
                                connect(parcel);
                                break;
                            case GET:
                                get(parcel);
                                break;
                            case PROVIDE:
                                provide(parcel);
                                break;
                            case UPDATE:
                                update(parcel);
                                break;
                            default:
                                throw new IOException("Invalid request was received");
                        }
                    } else
                        throw new IOException("Invalid parcel was received");
                } catch (IOException | ClassNotFoundException exception) {
                    if (!isInterrupted)
                        network.crash("Connection lost");
                    Thread.currentThread().interrupt();
                }
            }
        }

        void connect(Parcel parcel) throws IOException {
            try {
                sessionId = parcel.getRecipient();
                network.showConnections();
            } catch (Exception exception) {
                throw new IOException("Invalid data was received");
            }
        }

        @SuppressWarnings("unchecked")
        void get(Parcel parcel) throws IOException {
            try {
                network.showRequestedProperties(
                        parcel.getSender(),
                        (Pair<Properties, Properties>) parcel.getData()
                );
            } catch (Exception exception) {
                throw new IOException("Invalid data was received");
            }
        }

        void provide(Parcel parcel) throws IOException {
            try {
                sendRequest(
                        parcel.getSender(),
                        Parcel.Request.PROVIDE,
                        new Pair<>(
                                network.getGoldProperties(),
                                network.getGuppyProperties()
                        )
                );
            } catch (Exception exception) {
                throw new IOException("Invalid data was received");
            }
        }

        @SuppressWarnings("unchecked")
        void update(Parcel parcel) throws IOException {
            try {
                network.updateMembersList((List<Long>) parcel.getData());
            } catch (Exception exception) {
                throw new IOException("Invalid data was received");
            }
        }
    }
}
