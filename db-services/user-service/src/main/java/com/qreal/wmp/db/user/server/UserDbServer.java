package com.qreal.wmp.db.user.server;

import com.qreal.wmp.thrift.gen.UserDbService;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

/** Thrift server side service class for UserDBService.*/
public class UserDbServer {

    private static final Logger logger = LoggerFactory.getLogger(UserDbServer.class);

    private static void runTServer(UserDbService.Processor processor) {
        int port = 9090;
        logger.info("Starting User DB TServer on localhost on port {}", port);
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TThreadPoolServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor
                    (processor));
            server.serve();
            logger.info("User DB TServer started successfully");
        } catch (Exception e) {
            logger.error("UserDbServer encountered problem while starting TServer. TServer cannot be started.", e);
        }
    }

    /** Constructor starts Thrift TServer which implements RPC UserService interface.*/
    public UserDbServer(AbstractApplicationContext context) {
        try {
            UserDbServiceHandler handler = new UserDbServiceHandler(context);
            UserDbService.Processor processor = new UserDbService.Processor(handler);

            Runnable runServer = () -> {
                runTServer(processor);
            };
            logger.trace("Creating new thread for User DB TServer");

            new Thread(runServer).start();
            logger.trace("Thread created. Server started.");

        } catch (Exception x) {
            logger.error("UserDbServer encountered problem while creating TServer.", x);
        }
    }
}
