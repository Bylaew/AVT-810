package com.company;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends Thread {
    Socket socket;
    static int countConnectionUsers = -1;
    static Main[] usersSocket = null;
    static ServerSocket ser = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    public Main(Socket s) throws IOException {
        countConnectionUsers++;
        socket = s;
        ois = new ObjectInputStream(s.getInputStream());
        oos = new ObjectOutputStream(s.getOutputStream());
    }

    public static void main(String[] args) {
        System.out.println("Server is running");
        Socket s = null;
        usersSocket = new Main[64];
        try {
            ser = new ServerSocket(8031);
            while (true) {
                s = ser.accept();
                System.out.println("Соединение с клиентом " + (countConnectionUsers + 1) + ": " + s.getLocalAddress());

                Main nst = new Main(s);
                usersSocket[countConnectionUsers] = nst;
                nst.start();
            }
        } catch (Exception e) {
            System.out.println("ошибка: " + e);
        }
    }


    public void run() {
        while (true) {
            try {
                Object obj = ois.readObject();
                if (obj != null) System.out.println((String) obj);
                if (((String) (obj)).equals(("Show users"))) {
                    String[] users = new String[countConnectionUsers+1];
                    for (int i = 0; i < countConnectionUsers + 1; i++) {
                        users[i] = i + ". Hostname: " + usersSocket[i].socket.getLocalAddress().getHostName() + "; port: " + usersSocket[i].socket.getPort();
                    }

                    oos.writeObject(users);
                } else {
                    if (((String) (obj)).equals(("Send"))) {
                        int id = (int) ois.readObject();
                        int mas1[][] = (int[][]) ois.readObject();

                        for (int i = 0; i < usersSocket.length; i++) {
                            System.out.println(usersSocket[i].socket.getInetAddress().getHostName());
                            if (usersSocket[i].socket.getInetAddress().getHostName().equals("127.0.0." + id)) {
                                Main.usersSocket[i].oos.writeObject(mas1);
                                break;
                            }
                        }
                    } else {
                        if (((String) (obj)).equals(("Exit"))) {
                            boolean isFind = false;
                            String str = (String) ois.readObject();
                            for (int i = 0; i < countConnectionUsers; i++) {
                                if (!isFind)
                                    if (str.equals(usersSocket[i].socket.getLocalAddress().getHostName()))
                                        isFind = true;
                                if (isFind)
                                    usersSocket[i] = usersSocket[i + 1];
                            }
                            usersSocket[countConnectionUsers] = null;
                            countConnectionUsers--;
                            System.out.println("Thead is over");
                            break;
                        } else
                            oos.writeObject("Unknown command");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            this.ois.close();
            this.oos.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

