package com.qreal.wmp.db.user.server;

import com.qreal.wmp.thrift.gen.UserDbService;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/** Thrift server side service class for UserDBService.*/
@Component
@PropertySource("classpath:server.properties")
public class UserDbServer implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(UserDbServer.class);

    @Value("${port.db.user}")
    private int port;

    private ApplicationContext context;

    /** Function running TServer with chosen processor.*/
    private static void runTServer(UserDbService.Processor processor, int port) {
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
    @PostConstruct
    public void start() {
        try {
            UserDbServiceHandler handler = new UserDbServiceHandler(context);
            UserDbService.Processor processor = new UserDbService.Processor(handler);

            Runnable runServer = () -> runTServer(processor, port);
            logger.trace("Creating new thread for User DB TServer");

            new Thread(runServer).start();
            logger.trace("Thread created. Server started.");

        } catch (Exception x) {
            logger.error("UserDbServer encountered problem while creating TServer.", x);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
