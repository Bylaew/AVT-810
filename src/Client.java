import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Client {

    private Socket clientSocket;
    private BufferedReader reader;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Habitat habitat;

    public Client(Habitat habitat) {
        this.habitat = habitat;
        try {

            clientSocket = new Socket("localhost", 8080);
            reader = new BufferedReader(new InputStreamReader(System.in));
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());


            ReadMsg read = new ReadMsg();
            read.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send_objects(int id) {
        try {
            System.out.println("Отправка объектов");
            Package pckg = new Package(Package.Request_type.REQUEST);
            pckg.setData(habitat.singleton);
            pckg.setDestination(id);
            send(pckg);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void send(Package msg) {
        try {
            out.writeObject(msg);
            System.out.println("Message was sent from client");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {

            Package pckg;

            try {
                while (true) {

                    pckg = (Package) in.readObject(); // ждем сообщения с сервера

                    System.out.println("Объект получен");
                    if (pckg.getRequest_type() == Package.Request_type.CLIENTS_RECEIVE) {
                        LinkedList<Connection> temp = (LinkedList<Connection>) pckg.getData();
                        for (Connection con : temp) {
                            System.out.println(con.getID());
                        }
                        habitat.sideMenu.connections.removeAllItems();
                        for (Connection con : temp) {
                            habitat.sideMenu.connections.addItem(con.getID());
                        }
                    } else if (pckg.getRequest_type() == Package.Request_type.REQUEST) {
                        Package answer = new Package(Package.Request_type.ANSWER);
                        answer.setDestination(pckg.sender);
                        answer.setData(habitat.singleton);
                        send(answer);
                        habitat.singleton = (Singleton) pckg.getData();
                        habitat.redraw();
                    } else if (pckg.getRequest_type() == Package.Request_type.ANSWER) {
                        habitat.singleton = (Singleton) pckg.getData();
                        habitat.redraw();
                    }

                    sleep(100);
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
