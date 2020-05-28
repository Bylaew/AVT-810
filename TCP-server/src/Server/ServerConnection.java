package Server;

import Pack.Request;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConnection {
    private int ID;
    private BlockingQueue<Request> que;
    private Socket clientDialog;
    private ObjectInputStream in_obj;
    private ObjectOutputStream out_obj;
    private Runnable listen_task = new Runnable() {
        @Override
        public void run() {
            try {
                while (!clientDialog.isClosed()) {
                    Request entry = (Request)in_obj.readObject();

                    if(entry.getType()!=1)
                    send (entry);
                    else{
                        stop_con();
                    }
                }


                System.out.println("Closing connections & channels - DONE.");
            } catch (IOException e) {
                e.printStackTrace();
            }  catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable talk_task = new Runnable() {
        @Override
        public void run() {
            try{
                while(!clientDialog.isClosed()){
                    Request r=que.take();
                    switch (r.getType()){
                        case 2:{
                            r.setType(4);
                            ServerConnection temp = Server.get_conn(r.getDestination());
                            r.setDestination(ID);
                            System.out.println(ID+"Отправил запрос другому");
                            temp.send(r);
                            break;
                        }
                        case 3:{
                            r.setType(5);
                            Server.get_conn(r.getDestination()).send(r);
                            System.out.println(ID +"Отправил коллекцию другому");
                            break;
                        }
                        case 4:
                        case 5:
                        case 6:{
                            out_obj.writeObject(r);
                            System.out.println(ID + "Отправил запрос клиенту");
                            break;
                        }


                    }
                }



                System.out.println("Closing connections & channels - DONE.");
            }catch (IOException e) {
                e.printStackTrace();
            }catch (InterruptedException e){e.printStackTrace();}
        }
    };
    private Thread listen = new Thread(listen_task);
    private Thread talk = new Thread(talk_task);

    public ServerConnection(Socket client) {
        try{
        this.clientDialog = client;
        ID = (int)(Math.random()*100000);
        que = new LinkedBlockingQueue<>();
        in_obj = new ObjectInputStream(clientDialog.getInputStream());
        out_obj = new ObjectOutputStream(clientDialog.getOutputStream());
        listen.start();
        talk.start();
        }catch (IOException e){e.printStackTrace();}
    }

    private void stop_con(){
        try{
        in_obj.close();
        out_obj.close();
        clientDialog.close();
        Server.del_con(this);
        }catch (IOException e){e.printStackTrace();}
    }

    public int getID(){
        return ID;
    }

    protected Socket get_sock(){
        return clientDialog;
    }


   protected void send(Request obj){
        que.add(obj);
    }



}

