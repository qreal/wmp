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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component("userDao")
@Repository
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
     * Saves user, user's roles and robots. At local DB will be saved all information of user using Hibernate ORM.
     * User's robots will be passed for saving to RobotService, their's ids will be saved with user.
     * User's roles will be saved at local DB using Hibernate ORM.
     * Consistency kept using RPC calls to RobotsService.
     *
     * @param user user to saveUser (Id must not be set)
     */
    @Override
    public void saveUser(@NotNull TUser user) throws AbortedException, ErrorConnectionException {
        logger.trace("saveUser method called with parameters: user = {}", user.getUsername());

        Session session = sessionFactory.getCurrentSession();
        UserSerial userSerial = saveOrUpdateRobots(user);
        logger.trace("robots of user {} saved, id's now in userSerial", user.getUsername());

        userSerial = addUserRole(userSerial);
        session.save(userSerial);
        logger.trace("user {} saved", user.getUsername());

        diagramService.createRootFolder(user.getUsername());
        logger.trace("rootfolder {} created", user.getUsername());

        logger.trace("saveUser method saved user {}", user.getUsername());
    }

    /**
     * Finds user, his roles and robots. User and his roles will be loaded from local DB by Hibernate ORM.
     * User's robots will be loaded from RobotsService using their's ids persisted with user here.
     *
     * @param username name of user to find
     */
    @Override
    public TUser findByUserName(String username) throws NotFoundException, ErrorConnectionException {
        logger.trace("findByUserName method called with parameters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();
        UserSerial userSerial = (UserSerial) session.get(UserSerial.class, username);
        if (userSerial == null) {
            throw new NotFoundException(username, "User with specified username not found.");
        }
        return loadRobots(userSerial);

    }

    /**
     * Updates user, his roles and robots. User and his roles will be updated at local DB using Hibernate ORM.
     * User's robots will be updated using RobotsService.
     * Consistency kept using RPC calls to RobotsService.
     */
    @Override
    public void updateUser(@NotNull TUser tUser) throws AbortedException, ErrorConnectionException {
        logger.trace("updateUser method called with parameters: username = {}", tUser.getUsername());
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsUser(tUser.getUsername())) {
            logger.error("User with specified username doesn't exists.");
            throw new AbortedException("User with specified username doesn't exists", "updateUser safely aborted",
                    UserDaoImpl.class.getName());
        }
        UserSerial userSerial = saveOrUpdateRobots(tUser);
        session.merge(userSerial);
        logger.trace("updateUser method updated user");

    }

    /**
     * Tests if user exist. Test if user exists at local DB using Hibernate ORM.
     *
     * @param username name of user to test if exists
     */
    @Override
    public boolean isExistsUser(String username) {
        logger.trace("isExistsUser method called with parameters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();
        UserSerial userSerial = (UserSerial) session.get(UserSerial.class, username);
        return userSerial != null;
    }

    /** For the sake of testing.*/
    @Override
    public void setRobotService(RobotService robotService) {
        this.robotService = robotService;
    }

    /** For the sake of testing.*/
    @Override
    public RobotService getRobotService() {
        return robotService;
    }

    /** For the sake of testing.*/
    @Override
    public void rewindRobotService() {
        robotService = null;
    }

    /** For the sake of testing.*/
    @Override
    public void setDiagramService(DiagramService diagramService) {
        this.diagramService = diagramService;
    }

    /** For the sake of testing.*/
    @Override
    public DiagramService getDiagramService() {
        return diagramService;
    }

    /** For the sake of testing.*/
    @Override
    public void rewindDiagramService() {
        this.diagramService = null;
    }


    /**
     * Saves or updates robots using RobotsService.
     * Robot will be updated if robot's id set. Otherwise robot will be saved.
     * Id's of all robots will be saved in UserSerial robots field.
     */
    private UserSerial saveOrUpdateRobots(@NotNull TUser tUser) throws AbortedException, ErrorConnectionException {
        UserSerial userSerial = new UserSerial(tUser);
        for (TRobot robot : tUser.getRobots()) {
            logger.trace("Robot {} of user {} saving", robot.getName(), tUser.getUsername());
            long idRobot = 0;
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
    private TUser loadRobots(@NotNull UserSerial userSerial) throws ErrorConnectionException {
        TUser tUser = userSerial.toTUser();
        for (Long id : userSerial.getRobots()) {
            logger.trace("Robot with id {} of user {} loading", id, tUser.getUsername());
            try {
                TRobot robot = robotService.findById(id);
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