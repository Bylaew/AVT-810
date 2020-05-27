package com.company;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

public class Client extends Thread {
    Habitat habitat = null;

    static int idSend = -1;

    Socket clientSocket = null;
    int port = 8031;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    public static int currentCountUsers = 0;
    public static int countUsers = 0;

    static boolean needShow;

    boolean isGoing = true;

    static String[] users = new String[64];

    Client(Habitat h) throws IOException {
        needShow = true;
        this.habitat = h;
            clientSocket = new Socket("127.0.0." + ++countUsers, port);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
            currentCountUsers++;
    }

    public String[] showUsers() {
        if (!needShow) return users;
        try {
            oos.writeObject("Show users");
            oos.flush();
            users = (String[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        needShow = false;
        return users;
    }

    public void sendObj(int id, Ant[] mas) throws IOException {
        oos.writeObject("Send");
        idSend = id;
        oos.writeObject(id);

        int[][] mas1 = new int[mas.length][7];

        for (int i = 0; i < mas.length; i++) {
            System.out.println(mas[i].toMas().toString());
            mas1[i] = mas[i].toMas();
        }
        oos.writeObject(mas1);

    }

    public void exit() {
        needShow = true;
        isGoing = false;
        try {
            oos.writeObject("Exit");
            oos.writeObject(clientSocket.getInetAddress().getHostName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentCountUsers--;
        try {
            ois.close();
            oos.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        synchronized (this) {
            while (isGoing) {
                if (("127.0.0." + idSend).equals(this.clientSocket.getInetAddress().getHostName())) {

                    int qwe[][] = new int[0][];
                    try {
                        qwe = (int[][]) ois.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (habitat.getMas().getArray() == null) {
                        habitat.getMas().setArray(new Vector<>());
                    }

                    for (int[] i : qwe) {
                        if (i[0] == 1) {
                            habitat.getMas().getArray().add(new Warrior(i[1], i[2], i[3], i[4], i[5]));
                        } else {
                            habitat.getMas().getArray().add(new Worker(i[1], i[2], i[3], i[4], i[5]));
                        }
                    }
                    idSend = -1;
                }

                try {
                    this.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

