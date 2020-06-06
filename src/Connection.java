import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class Connection extends Thread implements Serializable {

    private transient Socket socket;
    private transient ObjectInputStream in; // поток чтения из сокета
    private transient ObjectOutputStream out; // поток записи в сокет
    private int ID = (int) (Math.random()*10000);


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        start();
    }

    @Override
    public void run() {
        Package pckg;
        try {

            while (true) {

                pckg = (Package) in.readObject();
                if (pckg.getRequest_type() == Package.Request_type.REQUEST |pckg.getRequest_type() == Package.Request_type.ANSWER ) {
                    if (pckg.sender == 0) {pckg.sender = ID;}
                    for (Connection vr : Server.serverList) {
                        if (vr.getID() == pckg.getDestination())
                        vr.send(pckg);
                    }
                }
            }

        } catch (SocketException e) {
            System.out.println("Клиент прекратил работу");
            Server.serverList.remove(this);
            Server.update_clients();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void send(Package msg) {
        try {
            out.writeObject(msg);
            System.out.println("Message was sent from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_available() {
        LinkedList<Connection> temp = new LinkedList<Connection>(Server.serverList);
        temp.remove(this);
        Package pckg = new Package(Package.Request_type.CLIENTS_RECEIVE);
        pckg.setData(temp);
        send(pckg);
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

