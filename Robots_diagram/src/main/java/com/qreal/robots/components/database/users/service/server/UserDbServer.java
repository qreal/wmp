package com.qreal.robots.components.database.users.service.server;

import com.qreal.robots.components.database.users.thrift.gen.UserDbService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by artemiibezguzikov on 11.07.16.
 */

public class UserDbServer {

    public static UserDbServiceHandler handler;
    public static UserDbService.Processor processor;

    public UserDbServer(AbstractApplicationContext context) {
        try {
            handler = new UserDbServiceHandler(context);
            processor = new UserDbService.Processor(handler);

            Runnable simple = () -> {
                simple(processor);
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple(UserDbService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TSimpleServer.Args(serverTransport).processor(processor));
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
