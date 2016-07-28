package com.qreal.robots.dao;

import com.qreal.robots.client.RobotService;
import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.auth.UserRole;
import com.qreal.robots.model.auth.serial.UserRoleSerial;
import com.qreal.robots.model.auth.serial.UserSerial;
import com.qreal.robots.model.robot.Robot;
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
    public void save(User user) {
        logger.trace("save method called with parameters: user = {}", user.getUsername());

        Session session = sessionFactory.getCurrentSession();
        logger.trace("saving robots of user {}", user.getUsername());
        UserSerial userSerial = saveRobots(user);
        logger.trace("robots of user {} saved", user.getUsername());
        session.save(userSerial);
        logger.trace("user {} saved", user.getUsername());
        UserRoleSerial userRole = new UserRoleSerial(userSerial, ROLE_USER);
        session.save(userRole);
        logger.trace("userRole of user {} saved", user.getUsername());
        logger.trace("save method saved user {}", user.getUsername());
    }

    @Override
    public User findByUserName(String username) {
        logger.trace("findByUserName method called with paremeters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();

        List<UserSerial> users = session.createQuery("from UserSerial where username=:username").
                setParameter("username", username).list();
        logger.trace("findByUserName method extracted list of results from session with {} elements. First will be " +
                "returned.", users.size());
        User user = loadRobots(users.stream().findFirst().orElse(null));

        return user;
    }

    @Override
    public void update(User user) {
        logger.trace("update method called with paremeters: username = {}", user.getUsername());
        Session session = sessionFactory.getCurrentSession();

        session.merge(saveRobots(user));

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

    private UserSerial saveRobots(User user) {
        long idRobot = 0;
        UserSerial userSerial = new UserSerial(user);


        for (Robot robot : user.getRobots()) {
            logger.trace("Robot {} of user {} saving", robot.getName(), user.getUsername());
            if (robot.getId() == null) {
                idRobot = robotService.register(robot);
            }
            else {
                idRobot = robot.getId();
            }
            userSerial.getRobots().add(idRobot);
            logger.trace("Robot {} of user {} saved with id {}", robot.getName(), user.getUsername(), robot.getId());
        }
        return userSerial;
    }

    private User loadRobots(UserSerial userSerial) {
        User user = userSerial.toUser();
        for (Long id : userSerial.getRobots()) {
            logger.trace("Robot with id {} of user {} loading", id, user.getUsername());

            Robot robot = robotService.findById(id);
            robot.setOwner(user);
            user.getRobots().add(robot);

            logger.trace("Robot  {} of user {} loaded", robot.getName(), user.getUsername());
        }
        return user;
    }

}