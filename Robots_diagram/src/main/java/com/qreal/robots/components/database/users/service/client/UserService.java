package com.qreal.robots.components.database.users.service.client;

import com.qreal.robots.components.authorization.model.auth.User;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

public interface UserService {

    public void save(User user) throws TException;

    public User findByUserName(String username) throws TException;

    public boolean isUserExist(String username) throws TException;

}
