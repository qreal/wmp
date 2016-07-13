package com.qreal.robots.components.database.users.service.client;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.database.users.service.client.UserService;
import com.qreal.robots.components.database.users.thrift.gen.TUser;
import com.qreal.robots.components.database.users.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


//In service transactions are added to DAO. DAO only DB functions implements.

@Service("UserService")
public class UserServiceImpl implements UserService {

    TTransport transport;
    TProtocol protocol;
    UserDbService.Client client;

    public UserServiceImpl() {
        transport = new TSocket("localhost", 9090);
        protocol = new TBinaryProtocol(transport);
        client = new UserDbService.Client(protocol);
    }

    @Transactional
    public void save(User user) throws TException {
        transport.open();
        TUser tUser = convertFromUser(user);
        client.save(tUser);
        transport.close();
    }

    @Transactional
    public User findByUserName(String username) throws TException {
        transport.open();
        TUser tUser = client.findByUserName(username);
        User user = convertFromTUser(tUser);
        transport.close();
        return  user;
    }

    @Transactional
    public boolean isUserExist(String username) throws TException {
        transport.open();
        boolean isUserExist = client.isUserExist(username);
        transport.close();
        return  isUserExist;
    }

    public static User convertFromTUser(TUser tUser) {
        User user = new User(tUser.getUsername(), tUser.getPassword(), tUser.isEnabled());
        return user;
    }

    public static TUser convertFromUser(User user) {
        TUser tUser = new TUser(user.getUsername(), user.getPassword(), user.isEnabled());
        return tUser;
    }

}
