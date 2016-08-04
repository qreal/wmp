package com.qreal.robots.server;

import com.qreal.robots.thrift.gen.DiagramDbService;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift server side service class for DiagramDBService.
 */
public class DiagramDbServer {

    private static final Logger logger = LoggerFactory.getLogger(DiagramDbServer.class);

    /**
     * Function running TServer with chosen processor.
     */
    private static void runTServer(DiagramDbService.Processor processor) {
        int port = 9093;
        logger.info("Starting Diagram DB TServer on localhost on port {}", port);
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TThreadPoolServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport).processor(processor)
            );
            server.serve();
            logger.info("Diagram DB TServer started successfully");
        } catch (Exception e) {
            logger.error("DiagramDBServer encountered problem while starting TServer. TServer cannot be started.", e);
        }
    }

    /**
     * Constructor. Starts Thrift TServer which implements RPC DiagramService interface.
     */
    public DiagramDbServer(AbstractApplicationContext context) {
        try {
            DiagramDbServiceHandler handler = new DiagramDbServiceHandler(context);
            DiagramDbService.Processor processor = new DiagramDbService.Processor(handler);

            Runnable runServer = () -> {
                runTServer(processor);
            };
            logger.trace("Creating new thread for Diagram DB TServer");
            new Thread(runServer).start();
            logger.trace("Thread created. Server started.");
        } catch (Exception x) {
            logger.error("DiagramDBServer encountered problem while creating TServer.", x);
        }
    }
}
