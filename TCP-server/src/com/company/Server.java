package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Server {
    private static final int port=48655;
    Singleton singleton=Singleton.getInstance();
    ServerSocket serverSock;
    Server() {
        try {
            serverSock=new ServerSocket(port);
            System.out.println("System works");
            while (true) {
                new ServerConnection(serverSock.accept());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //s.close();
            System.err.println(e);
        }
    }
}
