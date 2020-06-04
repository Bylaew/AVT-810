import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server
{
    static Vector<ClientHandler> clients = new Vector<>();
    static int count = 0;

    public static void main(String[] args)
    {
        try
        {
            System.out.println("Server is running");
            int port = 48655;
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket;
            while (true)
            {
                socket = serverSocket.accept();
                System.out.println("New client request was received: " + socket);
                System.out.println("Creating a new handler for this client...");
                ClientHandler mtch = new ClientHandler(socket, "Клиент " + count);
                System.out.println("Adding this client to active client list");
                clients.add(mtch);
                mtch.start();
                count++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread
{
    private String name;
    final ObjectOutputStream outputStream;
    final ObjectInputStream inputStream;
    Socket socket;
    boolean isLoggedIn;


    public ClientHandler(Socket s, String name) throws IOException
    {
        this.name = name;
        this.socket = s;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.isLoggedIn = true;
    }

    @Override
    public void run()
    {
        try
        {
            Vector<String> c = new Vector<>();
            for (ClientHandler ch : Server.clients)
            {
                c.add(ch.name);
                if (ch != this)
                {
                    ch.outputStream.writeObject(new Request(1, this.name));
                }
            }
            outputStream.writeObject(new Request(0, c));
            while (isLoggedIn)
            {
                Request request = (Request) inputStream.readObject();
                if (request.operationID == 2)
                {
                    Server.clients.remove(this);
                    Server.count--;
                    for (ClientHandler ch : Server.clients)
                    {
                        if (ch != this)
                        {
                            ch.outputStream.writeObject(new Request(2, this.name));
                        }
                    }
                    isLoggedIn = false;
                    System.out.println("Client disconnected");
                }
                else if (request.operationID == 3)
                {
                    System.out.println("New transport request was received");
                    System.out.println("Receiver of the data pack: " + request.receiver);
                    for (ClientHandler ch : Server.clients)
                        if (ch.name.equals(request.receiver))
                        {
                            outputStream.writeObject(request);
                            System.out.println("Data pack was sent");
                            break;
                        }
                }
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
