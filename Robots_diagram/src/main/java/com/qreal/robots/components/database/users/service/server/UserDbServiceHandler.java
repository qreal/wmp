package com.qreal.robots.components.database.users.service.server;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.authorization.model.auth.UserRole;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.components.database.users.DAO.UserDAO;
import com.qreal.robots.components.database.users.thrift.gen.TRobot;
import com.qreal.robots.components.database.users.thrift.gen.TUser;
import com.qreal.robots.components.database.users.thrift.gen.TUserRole;
import com.qreal.robots.components.database.users.thrift.gen.UserDbService;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.HashSet;
import java.util.Set;

public class UserDbServiceHandler implements UserDbService.Iface {

    private AbstractApplicationContext context;

    public UserDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public void save(TUser user) throws TException {
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        userDAO.save(ConverterFromT.castFromT(user));
    }

    @Override
    public TUser findByUserName(String username) throws TException {
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        return ConverterToT.castFrom(userDAO.findByUserName(username));
    }

    @Override
    public boolean isUserExist(String username) throws TException {
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        return userDAO.isUserExist(username);
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