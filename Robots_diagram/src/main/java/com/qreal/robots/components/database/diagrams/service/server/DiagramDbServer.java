package com.qreal.robots.components.database.diagrams.service.server;

import com.qreal.robots.components.database.diagrams.thrift.gen.DiagramDbService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.context.support.AbstractApplicationContext;

public class DiagramDbServer {
    public static DiagramDbServiceHandler handler;
    public static DiagramDbService.Processor processor;

    public DiagramDbServer(AbstractApplicationContext context) {
        try {
            handler = new DiagramDbServiceHandler(context);
            processor = new DiagramDbService.Processor(handler);

            Runnable simple = () -> {
                simple(processor);
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple(DiagramDbService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9093);
            TServer server = new TSimpleServer(new TSimpleServer.Args(serverTransport).processor(processor));
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

