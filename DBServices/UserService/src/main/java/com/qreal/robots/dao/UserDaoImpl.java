package com.qreal.robots.dao;

import com.qreal.robots.client.RobotService;
import com.qreal.robots.model.auth.UserRoleSerial;
import com.qreal.robots.model.auth.UserSerial;
import com.qreal.robots.thrift.gen.TRobot;
import com.qreal.robots.thrift.gen.TUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("userDao")
@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    public static final String ROLE_USER = "ROLE_USER";

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RobotService robotService;

    public UserDaoImpl() {
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TUser user) {
        logger.trace("save method called with parameters: user = {}", user.getUsername());

        Session session = sessionFactory.getCurrentSession();
        logger.trace("saving robots of user {}", user.getUsername());
        UserSerial userSerial = saveOrUpdateRobots(user);
        logger.trace("robots of user {} saved, id's now in userSerial", user.getUsername());
        session.save(userSerial);
        logger.trace("user {} saved", user.getUsername());
        UserRoleSerial userRole = new UserRoleSerial(userSerial, ROLE_USER);
        session.save(userRole);
        logger.trace("userRole of user {} saved", user.getUsername());
        logger.trace("save method saved user {}", user.getUsername());
    }

    @Override
    public TUser findByUserName(String username) {
        logger.trace("findByUserName method called with paremeters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();

        List<UserSerial> users = session.createQuery("from UserSerial where username=:username").
                setParameter("username", username).list();
        logger.trace("findByUserName method extracted list of results from session with {} elements. First will be " +
                "returned.", users.size());
        TUser tUser = loadRobots(users.stream().findFirst().orElse(null));

        return tUser;
    }

    @Override
    public void update(TUser tUser) {
        logger.trace("update method called with paremeters: username = {}", tUser.getUsername());
        Session session = sessionFactory.getCurrentSession();

        session.merge(saveOrUpdateRobots(tUser));

        logger.trace("update method updated user");

    }

    @Override
    public boolean isUserExist(String username) {
        logger.trace("isUserExist method called with parameters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();

        List users = session.createQuery("from UserSerial where username=:username").
                setParameter("username", username).list();
        logger.trace("isUserExist extracted list with {} users with name {}", users.size(), username);
        return !users.isEmpty();
    }

    private UserSerial saveOrUpdateRobots(TUser tUser) {
        UserSerial userSerial = new UserSerial(tUser);

        for (TRobot robot : tUser.getRobots()) {
            logger.trace("Robot {} of user {} saving", robot.getName(), tUser.getUsername());
            long idRobot;
            if (!robot.isSetId()) {
                idRobot = robotService.register(robot);
            }
            else {
                robotService.update(robot);
                idRobot = robot.getId();
            }
            userSerial.getRobots().add(idRobot);
            logger.trace("Robot {} of user {} saved with id {}", robot.getName(), tUser.getUsername(), robot.getId());
        }
        return userSerial;
    }

    private TUser loadRobots(UserSerial userSerial) {
        TUser tUser = userSerial.toTUser();
        for (Long id : userSerial.getRobots()) {
            logger.trace("Robot with id {} of user {} loading", id, tUser.getUsername());

            TRobot robot = robotService.findById(id);
            if (robot != null) {
                tUser.getRobots().add(robot);
            }

            logger.trace("Robot  {} of user {} loaded", robot.getName(), tUser.getUsername());
        }
        return tUser;
    }

}