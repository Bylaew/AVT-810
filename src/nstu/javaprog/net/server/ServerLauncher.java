package nstu.javaprog.net.server;

import java.io.IOException;
import java.util.Scanner;

final class ServerLauncher {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ServerConnectionHandler serverConnectionHandler = new ServerConnectionHandler(8080);
            serverConnectionHandler.start();
            while (scanner.hasNext()) {
                if (scanner.next().equals("interrupt")) {
                    serverConnectionHandler.interrupt();
                    break;
                }
            }
        } catch (IOException exception) {
            System.err.println("Connection handler was interrupted: " + exception.getMessage());
        }
    }
}
