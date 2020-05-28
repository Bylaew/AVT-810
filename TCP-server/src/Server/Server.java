package Server;

import Pack.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    protected static Vector<ServerConnection> conn = new Vector<ServerConnection>();
    private static Object mutex = new Object();


    protected static ServerConnection get_conn(int port){
        int i;
        for (i=0; i<conn.size();i++)
            if(port==conn.get(i).getID())
                break;
            return conn.get(i);
    }

    protected static void del_con(ServerConnection obj){
        synchronized (mutex){
            conn.remove(obj);
            notification();
        }
    }

    protected static void notification(){
        Vector temp = new Vector();
        for (int i=0; i<conn.size();i++)
            temp.add(conn.get(i).getID());
        Request mess = new Request(6);
        mess.setData(temp);
        for (int i=0; i<conn.size();i++)
            conn.get(i).send(mess);
    }

    public static void main(String []args){
        try (ServerSocket server = new ServerSocket(3345);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server.Server socket created, command console reader for listen to server commands");
            while(!server.isClosed()){
                if(br.ready()){
                    System.out.println("Main Server.Server found any messages in channel, let's look at them.");
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server.Server initiate exiting...");
                        server.close();
                        break;
                    }
                }

                // если комманд от сервера нет то становимся в ожидание
                // подключения к сокету общения под именем - "clientDialog" н   а
                // серверной стороне
                Socket client = server.accept();

                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable(при необходимости можно создать Callable)
                // монопоточную нить = сервер - MonoThreadClientHandler и тот
                // продолжает общение от лица сервера
                conn.add(new ServerConnection(client));
                notification();
                System.out.print("Connection accepted.");
            }

            conn.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
