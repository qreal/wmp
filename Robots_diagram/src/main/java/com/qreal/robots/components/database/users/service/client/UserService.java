package com.qreal.robots.components.database.users.service.client;

import com.qreal.robots.components.authorization.model.auth.User;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

public interface UserService {

    void save(User user) throws TException;

    User findByUserName(String username) throws TException;

    boolean isUserExist(String username) throws TException;
}
