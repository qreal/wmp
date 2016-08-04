package com.qreal.robots.dao;

import com.qreal.robots.client.RobotService;
import com.qreal.robots.model.auth.UserRoleSerial;
import com.qreal.robots.model.auth.UserSerial;
import com.qreal.robots.thrift.gen.TRobot;
import com.qreal.robots.thrift.gen.TUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
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

    private final SessionFactory sessionFactory;

    /**
     * RobotService used to resolve foreign key dependencies.
     */
    @Autowired
    private RobotService robotService;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves user, user's roles and robots. At local DB will be saved all information of user using Hibernate ORM.
     * User's robots will be passed for saving to RobotService, their's ids will be saved with user.
     * User's roles will be saved at local DB using Hibernate ORM.
     * Consistency kept using RPC calls to RobotsService.
     *
     * @param user user to save (Id must not be set)
     */

    @Override
    public void save(@NotNull TUser user) {
        logger.trace("save method called with parameters: user = {}", user.getUsername());

        Session session = sessionFactory.getCurrentSession();
        logger.trace("saving robots of user {}", user.getUsername());
        UserSerial userSerial = saveOrUpdateRobots(user);
        logger.trace("robots of user {} saved, id's now in userSerial", user.getUsername());
        session.save(userSerial);
        logger.trace("user {} saved", user.getUsername());
        UserRoleSerial userRole = new UserRoleSerial(ROLE_USER);
        session.save(userRole);
        logger.trace("userRole of user {} saved", user.getUsername());
        logger.trace("save method saved user {}", user.getUsername());
    }

    /**
     * Finds user, his roles and robots. User and his roles will be loaded from local DB by Hibernate ORM.
     * User's robots will be loaded from RobotsService using their's ids persisted with user here.
     *
     * @param username name of user to find
     */
    @Override
    public TUser findByUserName(String username) {
        logger.trace("findByUserName method called with paremeters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();

        List<UserSerial> users = session.createQuery("from UserSerial where username=:username").
                setParameter("username", username).list();
        logger.trace("findByUserName method extracted list of results from session with {} elements. First will be " +
                "returned.", users.size());
        UserSerial user = users.stream().findFirst().orElse(null);
        if (user != null) {
            return loadRobots(user);
        } else {
            return null;
        }
    }

    /**
     * Updates user, his roles and robots. User and his roles will be updated at local DB using Hibernate ORM.
     * User's robots will be updated using RobotsService.
     * Consistency kept using RPC calls to RobotsService.
     */
    @Override
    public void update(@NotNull TUser tUser) {
        logger.trace("update method called with paremeters: username = {}", tUser.getUsername());

        Session session = sessionFactory.getCurrentSession();
        session.merge(saveOrUpdateRobots(tUser));

        logger.trace("update method updated user");

    }

    /**
     * Tests if user exist. Test if user exists at local DB using Hibernate ORM.
     *
     * @param username name of user to test if exists
     */
    @Override
    public boolean isUserExist(String username) {
        logger.trace("isUserExist method called with parameters: username = {}", username);

        Session session = sessionFactory.getCurrentSession();
        List users = session.createQuery("from UserSerial where username=:username").
                setParameter("username", username).list();

        logger.trace("isUserExist extracted list with {} users with name {}", users.size(), username);
        return !users.isEmpty();
    }


    /**
     * Saves or updates robots using RobotsService.
     * Robot will be updated if robot's id set. Otherwise robot will be saved.
     * Id's of all robots will be saved in UserSerial robots field.
     */
    private UserSerial saveOrUpdateRobots(@NotNull TUser tUser) {
        UserSerial userSerial = new UserSerial(tUser);

        for (TRobot robot : tUser.getRobots()) {
            logger.trace("Robot {} of user {} saving", robot.getName(), tUser.getUsername());
            long idRobot;
            if (!robot.isSetId()) {
                idRobot = robotService.register(robot);
            } else {
                robotService.update(robot);
                idRobot = robot.getId();
            }
            userSerial.getRobots().add(idRobot);
            logger.trace("Robot {} of user {} saved with id {}", robot.getName(), tUser.getUsername(), robot.getId());
        }
        return userSerial;
    }

    /**
     * Loads robots using RobotsService.
     * Robots are loaded from RobotsService using id's of them saved in UserSerial.
     */
    private TUser loadRobots(@NotNull UserSerial userSerial) {
        TUser tUser = userSerial.toTUser();
        for (Long id : userSerial.getRobots()) {
            logger.trace("Robot with id {} of user {} loading", id, tUser.getUsername());

            TRobot robot = robotService.findById(id);
            if (robot != null) {
                tUser.getRobots().add(robot);
                logger.trace("Robot  {} of user {} loaded", robot.getName(), tUser.getUsername());
            } else {
                logger.error("Got null TRobot object for id '{}'", id);
            }
        }
        return tUser;
    }

}