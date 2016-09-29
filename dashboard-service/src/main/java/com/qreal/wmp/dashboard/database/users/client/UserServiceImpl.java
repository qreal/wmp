package com.qreal.wmp.dashboard.database.users.client;

import com.qreal.wmp.dashboard.database.exceptions.AbortedException;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.dashboard.database.exceptions.NotFoundException;
import com.qreal.wmp.dashboard.database.users.model.User;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
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

    /** Creates connection with Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client UserService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new UserDbService.Client(protocol);
    }

    @Override
    @Transactional
    public void save(@NotNull User user) throws AbortedException, ErrorConnectionException {
        TUser tUser = user.toTUser();
        logger.trace("save() was called with parameters: user = {}.", user.getUsername());
        try {
            transport.open();
            try {
                client.save(tUser);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TIdNotDefined e) {
                logger.error("save() encountered an IdNotDefined exception. User was not created.", e);
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client UserService encountered a problem while sending save request with parameters:" +
                        " user = {}", user, e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                        " a problem while sending save request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                    " a problem while opening transport.");
        }
        logger.trace("save() successfully saved {} user.", user.getUsername());
    }

    @Override
    @Transactional
    public void update(@NotNull User user) throws AbortedException, ErrorConnectionException {
        TUser tUser = user.toTUser();
        logger.trace("update() was called with parameters: user = {}", user.getUsername());
        try {
            transport.open();
            try {
                client.update(tUser);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TIdNotDefined e) {
                logger.error("update() encountered an IdNotDefined exception. You've tried to update user, " +
                        "but did not specified its id.", e);
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client UserService encountered a problem while sending update request with parameters:" +
                        " user = {}", user, e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                        " a problem while sending update request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                    " a problem while opening transport.");
        }
        logger.trace("update() successfully updated {} user.", user.getUsername());
    }

    @Override
    @Transactional
    @NotNull
    public User findByUserName(String username) throws NotFoundException, ErrorConnectionException {
        logger.trace("findByUserName() was called with parameters: username = {}.", username);
        TUser tUser = null;
        try {
            transport.open();
            try {
                tUser = client.findByUserName(username);
            } catch (TNotFound e) {
                throw new NotFoundException(e.getId(), e.getMessage());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client UserService encountered a problem while sending findByUserName request with" +
                        " parameters: username = {}", username, e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                        " a problem while sending findByUserName request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                    " a problem while opening transport.");
        }
        logger.trace("findByUserName() successfully returned an answer.");
        return new User(tUser);
    }

    @Override
    @Transactional
    public boolean isUserExist(String username) throws ErrorConnectionException {
        logger.trace("isUserExist() was called with parameters: username = {}", username);
        boolean isUserExist = false;
        try {
            transport.open();
            try {
                isUserExist = client.isUserExist(username);
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client UserService encountered a problem while sending isUserExist request with" +
                        " parameters: username = {}", username, e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                        " a problem while sending isUserExist request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered" +
                    " a problem while opening transport.");
        }
        logger.trace("isUserExist() successfully returned an answer");
        return isUserExist;
    }
}
