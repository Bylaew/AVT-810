import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
            int port = 1024;
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket;
            while (true)
            {
                socket = serverSocket.accept();
                System.out.println("New client request was received: " + socket);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Creating a new handler for this client...");
                ClientHandler mtch = new ClientHandler(socket, "Клиент " + count, inputStream, outputStream);
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
    final DataInputStream inputStream;
    final DataOutputStream outputStream;
    Socket socket;
    boolean isLoggedIn;


    public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos)
    {
        this.inputStream = dis;
        this.outputStream = dos;
        this.name = name;
        this.socket = s;
        this.isLoggedIn = true;
    }

    @Override
    public void run()
    {
        try
        {
            Update();
            while (isLoggedIn)
            {
                int operationID = inputStream.readInt();
                if (operationID == 1)
                {
                    Server.clients.remove(this);
                    Server.count--;
                    Update();
                    isLoggedIn = false;
                    System.out.println("Client disconnected");
                }
                else if (operationID == 2)
                {
                    System.out.println("New transport request was received");
                    String reciever = inputStream.readUTF();
                    System.out.println("Receiver of the data pack: " + reciever);
                    int N1 = inputStream.readInt();
                    double P1 = inputStream.readDouble();
                    int N2 = inputStream.readInt();
                    long ORLifetime = inputStream.readLong();
                    long ALifetime = inputStream.readLong();
                    System.out.println("Data pack was received");
                    for (ClientHandler ch : Server.clients)
                        if (ch.name.equals(reciever))
                        {
                            outputStream.writeBytes(Integer.toBinaryString(N1));
                            outputStream.writeBytes(Double.toString(P1));
                            outputStream.writeBytes(Integer.toBinaryString(N2));
                            outputStream.writeBytes(Long.toBinaryString(ORLifetime));
                            outputStream.writeBytes(Long.toBinaryString(ALifetime));
                            System.out.println("Data pack was sent");
                            break;
                        }
                }
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Update() throws IOException
    {
        for (ClientHandler ch : Server.clients)
        {
            ch.outputStream.writeInt(Server.count);
            for (ClientHandler ch1 : Server.clients)
            {
                if (ch == ch1)
                    ch.outputStream.writeUTF("Вы: " + ch1.name);
                else ch.outputStream.writeUTF(ch1.name);
            }
        }
    }
}
