import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

class Main
{
    public static final int PORT =48655;
    public static LinkedList<Server> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(PORT);
          try {
              while (true) {
                  System.out.println("Waiting for connect");
                  Socket socket = serverSocket.accept();
                  System.out.println("New client has connected to the server");
                  serverList.add(new Server(socket));

              }
          } finally {
              serverSocket.close();
          }
    }
    static void updateClientList()  {
        ArrayList<Integer> idList = new ArrayList<>(30);
        for (Server item: serverList)
            idList.add(item.id);
        System.out.println("Connected clients:");
        for (Server item: serverList)
        {
            System.out.println(item.id);
            try {
                item.objectOutputStream.writeObject("update");
                item.objectOutputStream.flush();
                item.objectOutputStream.writeObject(idList);
                item.objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
