package com.qreal.robots.components.dashboard.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    public static final String ERROR_MESSAGE = "ERROR";

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final String hostName;

    private final int port;

    public SocketClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public boolean hostAvailable() {
        try (Socket s = new Socket(hostName, port)) {
            return true;
        } catch (IOException e) {
            logger.error("SocketClient has an IO exception.", e);
        }
        return false;
    }

    public String sendMessage(String message) {
        logger.trace("sendMessage method was called with parameters: message = {}", message);
        Socket socket = null;
        DataOutputStream outToServer = null;
        BufferedReader inFromServer = null;
        String result = ERROR_MESSAGE;
        if (hostAvailable()) {
            try {
                socket = new Socket(hostName, port);

                outToServer = new DataOutputStream(socket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                outToServer.writeBytes(message + "\n");
                result = getResultMessage(inFromServer);
            } catch (IOException e) {
                logger.error("SocketClient failed to send message", e);
            } finally {
                close(inFromServer);
                close(outToServer);
                close(socket);
            }
        } else {
            logger.warn("SocketClient found that robot routing server is offline.");
        }
        logger.trace("SocketClient sent message {}", message);
        return result;
    }

    private String getResultMessage(BufferedReader inFromServer) throws IOException {
        return inFromServer.readLine();
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.error("SocketClient has an IO exception.", e);
            }
        }
    }

}
