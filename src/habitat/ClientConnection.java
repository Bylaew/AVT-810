package habitat;
import ai.StoneAI;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import objects.House;
import objects.Stone;
import objects.Wood;
import singleton.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ClientConnection extends Thread
{
    Habitat h;
    private Socket clientSocket;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;

    @Override
    public void run() {
        super.run();
        try {
            System.out.println("Client connection...");
            clientSocket = new Socket("localhost", 48655);
            if (clientSocket.isConnected()) {
                System.out.println("Client connected");
                objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
            }
            objectOutput.writeObject("update"); objectOutput.flush();
            while(true)
            {
                System.out.println("Waiting for server input");
                String serverMessage = (String) objectInput.readObject();
                System.out.println("Server message: "+ serverMessage);
                if (serverMessage.equals("update") )
                {
                    ArrayList <Integer> idList ;
                    idList = (ArrayList<Integer>) objectInput.readObject();
                    h.updateConnections(idList);
                }
                if (serverMessage.equals("set"))
                {
                    System.out.println("Adding objects");
                    Vector<House> addVector = (Vector<House>) objectInput.readObject();
                        for (House item: addVector)
                            h.houseSingleton.houseList.add(item);

                }
                String[] res =serverMessage.split(" ");
                if (res[0].equals("getto"))
                {
                    System.out.println("Sending objects to " + res[1]);
                    objectOutput.writeObject("setto " + res[1]);
                    objectOutput.writeObject(h.houseSingleton.houseList);
                    objectOutput.flush();
                }

            }

        }
        catch (IOException e ) {
            //System.out.println("No connection");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            //System.out.println("Class not found");
        }
    }

    ClientConnection(Habitat habitat) throws IOException
    {
        h = habitat;
        start();
    }
    public void receive(int id) throws IOException {
        objectOutput.writeObject("gettome "+id);
        objectOutput.flush();
    }
}
