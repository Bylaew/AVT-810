import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<Connection> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Сервер запущен");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new Connection(socket));
                    System.out.println("Клиент подключен");
                    update_clients();
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    public static void update_clients(){
        for (Connection client : serverList){
            client.send_available();
        }
    }
}