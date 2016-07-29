package com.qreal.robots.client;

import com.qreal.robots.thrift.gen.TUser;
import com.qreal.robots.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Thrift client side of UserDBService.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private TTransport transport;

    private UserDbService.Client client;

    /**
     * Constructor creates connection with Thrift TServer.
     */
    public UserServiceImpl() {
        String url = "localhost";
        int port = 9090;
        logger.info("Client UserService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new UserDbService.Client(protocol);
    }

    @Override
    @Transactional
    public void save(@NotNull TUser tUser) {
        logger.trace("save method called with parameters: user = {}", tUser.getUsername());
        try {
            transport.open();
            client.save(tUser);
            transport.close();
            logger.trace("save method saved user {}", tUser.getUsername());
        } catch (TException e) {
            logger.error("Client UserService encountered problem while sending save request with parameters: " +
                    "user = {}", tUser, e);
        }
    }

    @Override
    @Transactional
    public void update(@NotNull TUser tUser) {
        logger.trace("update method called with parameters: user = {}", tUser.getUsername());
        try {
            transport.open();
            client.update(tUser);
            transport.close();
            logger.trace("update method updated user {}", tUser.getUsername());
        } catch (TException e) {
            logger.error("Client UserService encountered problem while sending update request with parameters: " +
                    "user = {}", tUser.getUsername(), e);
        }
    }

    @Override
    @Transactional
    public TUser findByUserName(String username) {
        logger.trace("findByUserName method called with paremeters: username = {}", username);
        TUser tUser = null;
        try {
            transport.open();
            tUser = client.findByUserName(username);
            transport.close();
            logger.trace("findByUserName method returned answer.");
        } catch (TException e) {
            logger.error("Client UserService encountered problem while sending findByUserName request with " +
                    "parameters: username = {}", username, e);
        }
        return tUser;
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
