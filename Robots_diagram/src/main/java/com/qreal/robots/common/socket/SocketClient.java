package com.qreal.robots.common.socket;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    public static final String ERROR_MESSAGE = "ERROR";
    private static final Logger LOG = Logger.getLogger(SocketClient.class);
    private final String hostName;
    private final int port;

    public SocketClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public boolean hostAvailable() {
        try (Socket s = new Socket(hostName, port)) {
            return true;
        } catch (IOException ex) {
        /* ignore */
        }
        return false;
    }

    public String sendMessage(String message) {
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
                LOG.error("Failed to send message", e);
            } finally {
                close(inFromServer);
                close(outToServer);
                close(socket);
            }
        } else {
            LOG.warn("Robot routing server is offline. Robot data is unavailable ");
        }
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
                e.printStackTrace();
            }
        }
    }

}
