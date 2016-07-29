package com.qreal.robots.server;

import com.qreal.robots.thrift.gen.RobotDbService;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift server side service class for RobotDBService.
 */
public class RobotDbServer {

    private static final Logger logger = LoggerFactory.getLogger(RobotDbServer.class);

    private static void runTServer(RobotDbService.Processor processor) {
        int port = 9091;
        logger.info("Starting RobotSerial DB TServer on localhost on port {}", port);
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TThreadPoolServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            server.serve();
            logger.info("RobotSerial DB TServer started successfully");
        } catch (Exception e) {
            logger.error("RobotDBServer encountered problem while starting TServer. TServer cannot be started.", e);
        }
    }

    /**
     * Constructor starts Thrift TServer which implements RPC RobotService interface.
     */
    public RobotDbServer(AbstractApplicationContext context) {
        try {
            RobotDbServiceHandler handler = new RobotDbServiceHandler(context);
            RobotDbService.Processor processor = new RobotDbService.Processor(handler);

            Runnable runServer = () -> {
                runTServer(processor);
            };
            logger.trace("Creating new thread for RobotSerial DB TServer");
            new Thread(runServer).start();
            logger.trace("Thread created. Server started.");
        } catch (Exception x) {
            logger.error("RobotDBServer encountered problem while creating TServer.", x);
        }
    }
}
