/*
 * Copyright Denis Ageev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.socket;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by ageevdenis on 28-2-15.
 */
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
