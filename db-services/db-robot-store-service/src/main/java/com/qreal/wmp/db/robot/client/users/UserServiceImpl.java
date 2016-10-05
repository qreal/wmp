package com.qreal.wmp.db.robot.client.users;

import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
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

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

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
    public void save(@NotNull TUser tUser) throws AbortedException, ErrorConnectionException {
        logger.trace("save method called with parameters: user = {}", tUser.getUsername());
        try {
            transport.open();
            try {
                client.save(tUser);
            } catch (TIdNotDefined e) {
                logger.error("save method encountered exception IdNotDefined. User was not created", e);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client UserService encountered problem while sending save request with parameters: " +
                        "user = {}", tUser, e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                        "problem while sending save request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                    "problem  while opening transport.");
        }
        logger.trace("save method saved user {}", tUser.getUsername());

    }

    @Override
    @Transactional
    public void update(@NotNull TUser tUser) throws AbortedException, ErrorConnectionException {
        logger.trace("update method called with parameters: user = {}", tUser.getUsername());
        try {
            transport.open();
            try {
                client.update(tUser);
            } catch (TIdNotDefined e) {
                logger.error("update method encountered exception IdNotDefined. You've tried to update user, but not" +
                        " specified it's id.", e);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client UserService encountered problem while sending update request with parameters: " +
                        "user = {}", tUser.getUsername(), e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered  " +
                        "problem while sending update request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                    "problem while opening transport.");
        }
        logger.trace("update method updated user {}", tUser.getUsername());

    }

    @Override
    @Transactional
    @NotNull
    public TUser findByUserName(String username) throws NotFoundException, ErrorConnectionException {
        logger.trace("findByUserName method called with parameters: username = {}", username);
        TUser tUser = null;
        try {
            transport.open();
            try {
                tUser = client.findByUserName(username);
            } catch (TNotFound e) {
                throw new NotFoundException(e.getId(), e.getMessage());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client UserService encountered problem while sending findByUserName request with " +
                        "parameters: username = {}", username, e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                        "problem while sending findByUserName request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                    "problem while opening transport.");
        }
        logger.trace("findByUserName method returned answer.");
        return tUser;
    }

    @Override
    @Transactional
    public boolean isUserExist(String username) throws ErrorConnectionException {
        logger.trace("isUserExist method called with parameters: username = {}", username);
        boolean isUserExist = false;
        try {
            transport.open();
            try {
                isUserExist = client.isUserExist(username);
            } catch (TException e) {
                logger.error("Client UserService encountered problem while sending isUserExist request with " +
                        "parameters: username = {}", username, e);
                throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                        "problem while sending isUserExist request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client UserService encountered problem while opening transport.", e);
            throw new ErrorConnectionException(UserServiceImpl.class.getName(), "Client UserService encountered " +
                    "problem while opening transport.");
        }
        logger.trace("isUserExist returned answer");
        return isUserExist;
    }
}
