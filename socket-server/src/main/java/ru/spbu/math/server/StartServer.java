package ru.spbu.math.server;

import ru.math.spbu.server.SocketServer;

/**
 * Created by dageev on 22.03.15.
 */
public class StartServer {
    public static void main(String[] args) {
        SocketServer server = new SocketServer(9002);
        new Thread(server).start();
    }

}
