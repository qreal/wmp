package com.qreal.wmp.editor.database.users.client;

import com.qreal.wmp.editor.database.users.model.User;
import com.qreal.wmp.thrift.gen.TUser;
import com.qreal.wmp.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/** Thrift client side of UserDBService.*/
@Service("userService")
@PropertySource("classpath:client.properties")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private TTransport transport;

    private UserDbService.Client client;

    @Value("${port.db.user}")
    private int port;

    @Value("${path.db.user}")
    private String url;

    /** Constructor creates connection with Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client UserService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new UserDbService.Client(protocol);
    }

    @Override
    @Transactional
    public void save(User user) {
        logger.trace("save method called with parameters: user = {}", user.getUsername());
        try {
            transport.open();
            TUser tUser = user.toTUser();
            client.save(tUser);
            transport.close();
            logger.trace("save method saved user {}", user.getUsername());
        } catch (TException e) {
            logger.error("Client UserService encountered problem while sending save request with parameters: " +
                    "user = {}", user, e);
        }
    }

    @Override
    @Transactional
    public void update(User user) {
        logger.trace("update method called with parameters: user = {}", user.getUsername());
        try {
            transport.open();
            TUser tUser = user.toTUser();
            client.update(tUser);
            transport.close();
            logger.trace("update method updated user {}", user.getUsername());
        } catch (TException e) {
            logger.error("Client UserService encountered problem while sending update request with parameters: " +
                    "user = {}", user, e);
        }
    }

    @Override
    @Transactional
    public User findByUserName(String username) {
        logger.trace("findByUserName method called with paremeters: username = {}", username);
        User user = null;
        try {
            transport.open();
            TUser tUser = client.findByUserName(username);
            user = new User(tUser);
            transport.close();
            logger.trace("findByUserName method returned answer.");
        } catch (TException e) {
            logger.error("Client UserService encountered problem while sending findByUserName request with " +
                    "parameters: username = {}", username, e);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean isUserExist(String username) {
        logger.trace("isUserExist method called with parameters: username = {}", username);
        boolean isUserExist = false;
        try {
            transport.open();
            isUserExist = client.isUserExist(username);
            transport.close();
            logger.trace("isUserExist returned answer");
        } catch (TException e) {
            logger.error("Client UserService encountered problem while sending isUserExist request with parameters: " +
                    "username = {}", username, e);
        }
        return isUserExist;
    }
}
