package com.qreal.robots.components.database.robots.service.server;

import com.qreal.robots.components.database.robots.thrift.gen.RobotDbService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.context.support.AbstractApplicationContext;

public class RobotDbServer {

    public static RobotDbServiceHandler handler;

    public static RobotDbService.Processor processor;

    public static void simple(RobotDbService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9091);
            TServer server = new TSimpleServer(new TSimpleServer.Args(serverTransport).processor(processor));
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RobotDbServer(AbstractApplicationContext context) {
        try {
            handler = new RobotDbServiceHandler(context);
            processor = new RobotDbService.Processor(handler);

            Runnable simple = () -> {
                simple(processor);
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}