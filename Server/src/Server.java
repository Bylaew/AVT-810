import objects.*;
import java.io.*;
import java.net.Socket;
import java.util.Vector;


public class Server extends Thread
{
    public int id;
    private Socket socket;
    public ObjectInputStream objectInputStream;
    public ObjectOutputStream objectOutputStream;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        this.id = (int) (Math.random() * 100000);
        objectOutputStream= new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        start();
    }

    @Override
    public void run() {
        super.run();
        try {
            while(true)
            {
                System.out.println("Waiting for client input");
                String message = (String) objectInputStream.readObject();
                System.out.println("Client input was: " + message);
                if (message.equals("update"))
                {
                    Main.updateClientList();
                }

                String[] res = message.split(" ");
                if (res[0].equals("gettome"))
                {
                    int destId = Integer.parseInt(res[1]);
                    for (Server item: Main.serverList)
                        if (item.id == destId)
                        {
                            item.objectOutputStream.writeObject("getto " + this.id);
                        }
                }
                if (res[0].equals("setto"))
                {
                    int destId = Integer.parseInt(res[1]);
                    Vector<House> data = (Vector<House>) objectInputStream.readObject();
                    for (Server item: Main.serverList)
                        if (item.id == destId)
                        {
                            item.objectOutputStream.writeObject("set");
                            item.objectOutputStream.flush();
                            item.objectOutputStream.writeObject(data);
                        }
                }

            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            //e.printStackTrace();
            delete();
            System.out.println("Disconnected");
        }
    }
    private void delete()  {
        Main.serverList.remove(this);
        try {
            this.objectInputStream.close();
            this.objectOutputStream.close();
            this.socket.close();
            Main.updateClientList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
