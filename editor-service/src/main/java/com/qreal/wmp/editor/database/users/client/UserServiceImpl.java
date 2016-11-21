package com.qreal.wmp.editor.database.users.client;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.users.model.User;
import com.qreal.wmp.thrift.gen.TUser;
import com.qreal.wmp.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.jetbrains.annotations.NotNull;
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

    /** Connects to a Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client UserService was created with Thrift socket on url = {}, port = {}.", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new UserDbService.Client(protocol);
    }

    @Override
    @Transactional
    public void saveUser(@NotNull User user) throws AbortedException, ErrorConnectionException, TException {
        TUser tUser = user.toTUser();
        logger.trace("saveUser() was called with parameters: user = {}.", user.getUsername());
        transport.open();
        try {
            client.saveUser(tUser);
        } finally {
            transport.close();
        }
        logger.trace("saveUser() successfully saved user {}.", user.getUsername());
    }

    @Override
    @Transactional
    public void updateUser(@NotNull User user) throws AbortedException, ErrorConnectionException, TException {
        TUser tUser = user.toTUser();
        logger.trace("updateUser() was called with parameters: user = {}", user.getUsername());
        transport.open();
        try {
            client.updateUser(tUser);
        } finally {
            transport.close();
        }
        logger.trace("updateUser() successfully updated user {}.", user.getUsername());
    }

    @Override
    @Transactional
    @NotNull
    public User getUser(String username) throws NotFoundException, ErrorConnectionException, TException {
        logger.trace("getUser() was called with parameters: username = {}.", username);
        TUser tUser = null;
        transport.open();
        try {
            tUser = client.getUser(username);
        } finally {
            transport.close();
        }
        logger.trace("getUser() successfully returned an answer.");
        return new User(tUser);
    }

    @Override
    @Transactional
    public boolean isUserExists(String username) throws ErrorConnectionException, TException {
        logger.trace("isUserExists() was called with parameters: username = {}", username);
        boolean isUserExist = false;
        transport.open();
        try {
            isUserExist = client.isUserExists(username);
        } finally {
            transport.close();
        }
        logger.trace("isUserExists successfully returned an answer");
        return isUserExist;
    }
}
