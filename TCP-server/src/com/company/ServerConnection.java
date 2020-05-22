package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;

public class ServerConnection extends Thread {
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    double N1=0;
    double N2=0;
    int lifeTimeRabbit=0;
    int lifeTimeAlbino=0;
    float k= (float) 0.0;
    double P1=0;
    boolean ShowTime;
    boolean running=true;

    int port;
    Singleton singleton=Singleton.getInstance();
    public ServerConnection(Socket sok)
    {
        socket=sok;
        singleton.getMap().put(getPort(),this);
        System.out.println(singleton.getMap().keySet());
        Update();
        this.start();
    }
    public void run(){//клиент закрывается если минус -1 то удалить из мап

           try {
               inStream = new DataInputStream(socket.getInputStream());
               while (running) {
               int close = inStream.readInt();
               if (close == -1) {
                   singleton.getMap().remove(getPort(), this);
                   outStream.writeInt(0);
                   System.out.println(singleton.map.keySet());
                   running=false;
                   socket.close();
                   Update();
               }
               if (close == 1) {
                   port = inStream.readInt();
                   N1 = inStream.readDouble();
                   N2 = inStream.readDouble();
                   lifeTimeRabbit = inStream.readInt();
                   lifeTimeAlbino = inStream.readInt();
                   k = inStream.readFloat();
                   P1 = inStream.readDouble();
                   ShowTime = inStream.readBoolean();
                   System.out.println(port);
                   System.out.println(N1);
                   System.out.println(N2);
                   System.out.println("lifeTimeRabbit" + lifeTimeRabbit);
                   System.out.println("lifeTimeAl" + lifeTimeAlbino);
                   System.out.println(k);
                   System.out.println(P1);
                   System.out.println(ShowTime);
                   DataOutputStream data = new DataOutputStream(singleton.getMap().get(port).getOut());

                   data.writeInt(-1);
                   data.writeDouble(N1);
                   data.writeDouble(N2);
                   data.writeInt(lifeTimeRabbit);
                   data.writeInt(lifeTimeAlbino);
                   data.writeFloat(k);
                   data.writeDouble(P1);
                   data.writeBoolean(ShowTime);
               }

           }
           } catch (IOException e) {
               try {
                   inStream.close();
                   outStream.close();
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
               running=false;
               e.printStackTrace();
           }

    }
    public int getPort()
    {
        return socket.getPort();
    }

    public void SendNotify()
    {
        try {
            outStream=new DataOutputStream(socket.getOutputStream());
            outStream.writeInt(singleton.getMap().size());
            for(Integer key:singleton.getMap().keySet())
                outStream.writeInt(key);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Update()
    {
        for(ServerConnection claind:singleton.getMap().values()) {
            claind.SendNotify();
        }
    }
    public OutputStream getOut() throws IOException {
        return socket.getOutputStream();
    }
}