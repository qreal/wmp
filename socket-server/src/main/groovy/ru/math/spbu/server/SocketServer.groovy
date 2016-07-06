package ru.math.spbu.server

import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j

/**
 * Created by ageevdenis on 28-2-15.
 */
@TupleConstructor
@Slf4j
class SocketServer implements Runnable {

    protected int serverPort = 9002;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public SocketServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        log.info "Server Started at " + new Date()

        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    log.info "Server Stopped."
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new WorkerRunnable(socket: clientSocket)).start();
        }
        log.info "Server Stopped."
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }


    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port $serverPort", e);
        }
    }

}


