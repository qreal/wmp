package com.qreal.robots.common.config.core;

import com.qreal.robots.components.database.diagrams.service.DiagramService;
import com.qreal.robots.components.database.users.thrift.gen.TUser;
import com.qreal.robots.components.database.users.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//User with login "123" and password "123" will be created automatically


@Component
public class DBInit implements ApplicationListener {

    @EventListener
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();

            DiagramService diagramService = (DiagramService) applicationContext.getBean("DiagramService");
            PasswordEncoder encoder = (PasswordEncoder) applicationContext.getBean("PasswordEncoder");

            diagramService.createRootFolder("123");

            TTransport transport;
            try {
                transport = new TSocket("localhost", 9090);

                TProtocol protocol = new TBinaryProtocol(transport);

                UserDbService.Client client = new UserDbService.Client(protocol);
                transport.open();

                if (client.isUserExist("123")) {
                    return;
                }

                TUser user = new TUser("123", encoder.encode("123"), true);
                client.save(user);
                transport.close();

            } catch (TTransportException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            }


        }
    }

}