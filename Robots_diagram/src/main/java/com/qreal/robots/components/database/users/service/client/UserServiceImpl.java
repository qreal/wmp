package com.qreal.robots.components.database.users.service.client;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.authorization.model.auth.UserRole;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.users.thrift.gen.TRobot;
import com.qreal.robots.components.database.users.thrift.gen.TUser;
import com.qreal.robots.components.database.users.thrift.gen.TUserRole;
import com.qreal.robots.components.database.users.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;


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
        TUser tUser = ConverterToT.castFrom(user);
        client.save(tUser);
        transport.close();
    }

    @Transactional
    public User findByUserName(String username) throws TException {
        transport.open();
        TUser tUser = client.findByUserName(username);
        User user = ConverterFromT.castFromT(tUser);
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

}

class ConverterFromT {

    static Set<UserRole> castFromTRole(Set<TUserRole> tUserRoles, User user) {
        Set<UserRole> userRoles = new HashSet<>();
        for (TUserRole tur : tUserRoles) {

            userRoles.add(new UserRole(tur.getUserRoleId(), user, tur.getRole()));
        };
        return userRoles;
    }

    static Set<Robot> castFromTRobot(Set<TRobot> tRobots, User user) {
        Set<Robot> robots = new HashSet<>();
        for (TRobot trob : tRobots) {
            robots.add(new Robot(trob.getId(), trob.getName(), trob.getSsid(), user));
        }
        return robots;
    }

    static User castFromTPartly(TUser tUser) {
        return new User(tUser.getUsername(), tUser.getPassword(), tUser.isEnabled());
    }

    static User castFromT(TUser tUser) {
        User user = castFromTPartly(tUser);
        user.setUserRole(castFromTRole(tUser.getRoles(), user));
        user.setRobots(castFromTRobot(tUser.getRobots(), user));
        return user;
    }

}

class ConverterToT {

    static Set<TUserRole> castFromRole(Set<UserRole> userRoles, TUser tUser) {
        Set<TUserRole> tUserRoles = new HashSet<>();
        for (UserRole ur : userRoles) {
            tUserRoles.add(new TUserRole(ur.getUserRoleId(), ur.getRole()));
        };
        return tUserRoles;
    }

    static Set<TRobot> castFromRobot(Set<Robot> robots, TUser tUser) {
        Set<TRobot> tRobots = new HashSet<>();
        for (Robot rob : robots) {
            tRobots.add(new TRobot(rob.getId(), rob.getName(), rob.getSsid()));
        }
        return tRobots;
    }

    static TUser castFromPartly(User user) {
        TUser tUser = new TUser();
        tUser.setUsername(user.getUsername());
        tUser.setPassword(user.getPassword());
        tUser.setEnabled(user.isEnabled());
        return tUser;
    }

    static TUser castFrom(User user) {
        TUser tUser = castFromPartly(user);
        tUser.setRoles(castFromRole(user.getUserRole(), tUser));
        tUser.setRobots(castFromRobot(user.getRobots(), tUser));
        return tUser;
    }

}