package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection implements Runnable {
    private Habitat habitat;
    private Socket client;
    private DataInputStream inputStream;
     Boolean running=true;
    DataOutputStream data;
    Connection(Habitat h){
        habitat=h;
        System.out.println("program is works");
        try {
            client=new Socket("localhost",48655);
            inputStream=new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            running=false;
           // e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                int size=0;
                size = inputStream.readInt();
                habitat.TCPbox.removeAllItems();
                habitat.TCPbox.addItem(" ");
                for (int i = 0; i < size; i++) {
                    int port = inputStream.readInt();
                    if(port!=client.getLocalPort()) {
                        System.out.println(port);
                        habitat.TCPbox.addItem(port);
                    }
                    System.out.println("oll ports: "+port);
                    //Vector.add(port);
                }
                if(size==-1)
                {
                    habitat.N1=inputStream.readDouble();
                    habitat.N2=inputStream.readDouble();
                    habitat.lifeTimeRabbit=inputStream.readInt();
                    habitat.lifeTimeAlbino=inputStream.readInt();
                    habitat.k=inputStream.readFloat();
                    habitat.P1=inputStream.readDouble();
                    habitat.ShowTime=inputStream.readBoolean();
                    System.out.println(habitat.N1);
                    System.out.println(habitat.N2);
                    System.out.println(habitat.lifeTimeRabbit);
                    System.out.println(habitat.lifeTimeAlbino);
                    System.out.println(habitat.k);
                    System.out.println(habitat.P1);
                    System.out.println(habitat.ShowTime);
                    //inputStream.close();
                }

            } catch (IOException e) {
                try {
                    inputStream.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                running=false;
                e.printStackTrace();
            }
        }
    }

    public void CloseConnection(){

        try {
        data = new DataOutputStream(client.getOutputStream());
            data.writeInt(-1);
        } catch (IOException e) {
           // running=false;
            e.printStackTrace();
        }
    }

    public void broadcastTread()
    {
        //while (running) {
            try {
                data = new DataOutputStream(client.getOutputStream());
                data.writeInt(1);
                data.writeInt((Integer) habitat.TCPbox.getSelectedItem());
                data.writeDouble(habitat.N1);
                data.writeDouble(habitat.N2);
                data.writeInt(habitat.lifeTimeRabbit);
                data.writeInt(habitat.lifeTimeAlbino);
                data.writeFloat(habitat.k);
                data.writeDouble(habitat.P1);
                data.writeBoolean(habitat.ShowTime);
            } catch (IOException e) {

               // running=false;
                e.printStackTrace();
            }
        //}
    }
}
