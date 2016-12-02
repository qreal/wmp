package com.qreal.wmp.db.palette.server;

import com.qreal.wmp.thrift.gen.PaletteDbService;
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

/** Thrift server side service class for DiagramDBService. */
@Component
@PropertySource("classpath:server.properties")
public class PaletteDbServer implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(PaletteDbServer.class);

    @Value("${port.db.palette}")
    private int port;

    private ApplicationContext context;

    /** Function running TServer with a chosen processor. */
    private static void runTServer(PaletteDbService.Processor processor, int port) {
        logger.info("Starting Palette DB TServer on localhost on port {}", port);
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TThreadPoolServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport).processor(processor)
            );
            server.serve();
            logger.info("Palette DB TServer started successfully");
        } catch (Exception e) {
            logger.error("PaletteDBServer encountered problem while starting TServer. TServer cannot be started.", e);
        }
    }

    /** Starts Thrift TServer which implements RPC PaletteService interface. */
    @PostConstruct
    public void start() {
        try {
            PaletteDbServiceHandler handler = new PaletteDbServiceHandler(context);
            PaletteDbService.Processor processor = new PaletteDbService.Processor(handler);

            Runnable runServer = () -> runTServer(processor, port);
            logger.trace("Creating new thread for Palette DB TServer");
            new Thread(runServer).start();
            logger.trace("Thread created. Server started.");
        } catch (Exception x) {
            logger.error("DiagramDBServer encountered problem while creating TServer.", x);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
