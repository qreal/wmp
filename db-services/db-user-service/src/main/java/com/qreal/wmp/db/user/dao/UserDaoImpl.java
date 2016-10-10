package com.qreal.wmp.db.user.dao;

import com.qreal.wmp.db.user.client.diagrams.DiagramService;
import com.qreal.wmp.db.user.client.robots.RobotService;
import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.exceptions.NotFoundException;
import com.qreal.wmp.db.user.model.auth.UserRoleSerial;
import com.qreal.wmp.db.user.model.auth.UserSerial;
import com.qreal.wmp.thrift.gen.TRobot;
import com.qreal.wmp.thrift.gen.TUser;
import org.apache.thrift.TException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional
public class UserDaoImpl implements UserDao {
    private static final String ROLE_USER = "ROLE_USER";

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory;

    /** RobotService used to resolve foreign key dependencies.*/
    private RobotService robotService;

    /** DiagramService used to resolve foreign key dependencies.*/
    private DiagramService diagramService;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory, RobotService robotService, DiagramService diagramService) {
        this.sessionFactory = sessionFactory;
        this.robotService = robotService;
        this.diagramService = diagramService;
    }

    /**
     * Saves the user, user's roles and robots. At local DB all the information will be saved using Hibernate ORM.
     * User's robots will be passed for saving to RobotService, their ids will be saved with user.
     * User's roles will be saved at local DB using Hibernate ORM.
     * Consistency kept using RPC calls to RobotsService.
     * @param user user to saveUser (Id must not be set)
     */
    @Override
    public void saveUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException {
        logger.trace("saveUser() was called with parameters: user = {}.", user.getUsername());

        Session session = sessionFactory.getCurrentSession();
        UserSerial userSerial = saveOrUpdateRobots(user);
        logger.trace("robots of user {} saved, theay are now in userSerial.", user.getUsername());

        userSerial = addUserRole(userSerial);
        session.save(userSerial);
        logger.trace("user {} saved.", user.getUsername());

        try {
            diagramService.createRootFolder(user.getUsername());
        } catch (TException e) {
            e.printStackTrace();
        }
        logger.trace("rootfolder {} created.", user.getUsername());

        logger.trace("saveUser() successfully saved user {}.", user.getUsername());
    }

    /**
     * Finds the user, his roles and robots. The user and his roles will be loaded from local DB by Hibernate ORM.
     * User's robots will be loaded from RobotsService using their ids persisted with user here.
     * @param username name of user to find
     */
    @Override
    public TUser findByUserName(String username) throws NotFoundException, ErrorConnectionException {
        logger.trace("findByUserName() was called with parameters: username = {}.", username);
        Session session = sessionFactory.getCurrentSession();
        UserSerial userSerial = (UserSerial) session.get(UserSerial.class, username);
        if (userSerial == null) {
            throw new NotFoundException(username, "User with specified username not found.");
        }
        return loadRobots(userSerial);
    }

    /**
     * Updates the user, his roles and robots. The user and his roles will be updated at local DB using Hibernate ORM.
     * User's robots will be updated using RobotsService.
     * Consistency kept using RPC calls to RobotsService.
     */
    @Override
    public void updateUser(@NotNull TUser tUser) throws AbortedException, ErrorConnectionException {
        logger.trace("updateUser() was called with parameters: username = {}.", tUser.getUsername());
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsUser(tUser.getUsername())) {
            logger.error("User with specified username doesn't exists.");
            throw new AbortedException("User with specified username doesn't exists", "updateUser() safely aborted",
                    UserDaoImpl.class.getName());
        }
        UserSerial userSerial = saveOrUpdateRobots(tUser);
        session.merge(userSerial);
        logger.trace("updateUser() successfully updated the user.");

    }

    /**
     * Tests if a user exist at local DB using Hibernate ORM.
     * @param username name of user to test if exists
     */
    @Override
    public boolean isExistsUser(String username) {
        logger.trace("isExistsUser() was called with parameters: username = {}.", username);
        Session session = sessionFactory.getCurrentSession();
        UserSerial userSerial = (UserSerial) session.get(UserSerial.class, username);
        return userSerial != null;
    }

    /**
     * Saves or updates robots using RobotsService.
     * A robot will be updated if robot's Id is set. Otherwise a robot will be saved.
     * Ids of all robots will be saved in UserSerial robots field.
     */
    private UserSerial saveOrUpdateRobots(@NotNull TUser tUser) throws AbortedException, ErrorConnectionException {
        UserSerial userSerial = new UserSerial(tUser);
        for (TRobot robot : tUser.getRobots()) {
            logger.trace("Saving robot {} of user {}.", robot.getName(), tUser.getUsername());
            long idRobot = 0;
            if (!robot.isSetId()) {
                try {
                    idRobot = robotService.register(robot);
                } catch (TException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    robotService.update(robot);
                } catch (TException e) {
                    e.printStackTrace();
                }
                idRobot = robot.getId();
            }
            userSerial.getRobots().add(idRobot);
            logger.trace("Robot {} of user {} saved with id {}.", robot.getName(), tUser.getUsername(), robot.getId());
        }
        return userSerial;
    }

    /**
     * Loads robots using RobotsService.
     * Robots are loaded from RobotsService using their ids saved in UserSerial.
     */
    private TUser loadRobots(@NotNull UserSerial userSerial) throws ErrorConnectionException {
        TUser tUser = userSerial.toTUser();
        for (Long id : userSerial.getRobots()) {
            logger.trace("Loading a robot with id {} of user {}", id, tUser.getUsername());
            try {
                TRobot robot = null;
                try {
                    robot = robotService.findById(id);
                } catch (TException e) {
                    e.printStackTrace();
                }
                tUser.getRobots().add(robot);
                logger.trace("Robot  {} of user {} loaded", robot.getName(), tUser.getUsername());
            } catch (NotFoundException notFound) {
                logger.error("Inconsistent state: User contains robot with id {}, but this robot doesn't exist.", id);
            }
        }
        return tUser;
    }

    private UserSerial addUserRole(@NotNull UserSerial userSerial) {
        if (!userSerial.getRoles().contains(new UserRoleSerial(ROLE_USER))) {
            Set<UserRoleSerial> roles = new HashSet<>();
            UserRoleSerial userRole = new UserRoleSerial(ROLE_USER);
            roles.add(userRole);
            userSerial.setRoles(roles);
        }
        return userSerial;
    }
}