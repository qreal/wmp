package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.client.users.UserService;
import com.qreal.wmp.db.robot.exceptions.AbortedException;
import com.qreal.wmp.db.robot.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.robot.exceptions.NotFoundException;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
import com.qreal.wmp.thrift.gen.TRobot;
import com.qreal.wmp.thrift.gen.TUser;
import org.apache.thrift.TException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RobotDaoImpl implements RobotDao {

    private static final Logger logger = LoggerFactory.getLogger(RobotDaoImpl.class);

    /** UserService used to resolve foreign key dependencies. */
    private UserService userService;

    private final SessionFactory sessionFactory;

    public RobotDaoImpl(SessionFactory sessionFactory, UserService userService) {
        this.sessionFactory = sessionFactory;
        this.userService = userService;
    }

    /**
     * Saves a robot at local DB using Hibernate ORM.
     * Foreign key on User (in username) will NOT be checked. (at least for now)
     * @param robot robot to saveRobot (Id must not be set).
     */
    @Override
    public long saveRobot(@NotNull RobotSerial robot) {
        logger.trace("saveRobot() was called with parameters: robot = {}.", robot.getName());
        Session session = sessionFactory.getCurrentSession();
        session.save(robot);
        logger.trace("saveRobot() successfully saved robot {}.", robot.getName());
        return robot.getId();
    }

    /**
     * Finds a robot by Id at local DB using Hibernate ORM.
     * @param robotId id of a robot to find
     */
    @Override
    public @NotNull RobotSerial getRobot(long robotId) throws NotFoundException {
        logger.trace("getRobot() was called with parameters: robotId = {}.", robotId);
        Session session = sessionFactory.getCurrentSession();

        RobotSerial robot = (RobotSerial) session.get(RobotSerial.class, robotId);
        if (robot == null) {
            throw new NotFoundException(String.valueOf(robotId), "Robot with specified id was not found.");
        }
        return robot;
    }

    /**
     * Updates a robot at local DB using Hibernate ORM.
     * @param robot robot to updateUser (Id must be set correctly)
     */
    @Override
    public void updateRobot(@NotNull RobotSerial robot) throws AbortedException {
        logger.trace("updateRobot() was called with parameters: robot = {}.", robot.getName());
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsRobot(robot.getId())) {
            logger.error("Robot with specified Id doesn't exists.");
            throw new AbortedException("Robot with specified Id doesn't exists. Use saveRobot instead.",
                    "updateRobot() safely aborted.", RobotDaoImpl.class.getName());
        }
        session.merge(robot);

        logger.trace("updateRobot() successfully updated a robot");
    }

    /**
     * Deletes a robot from local DB using Hibernate ORM.
     * Loads User associated with this robot using UserService and deletes the robot from its list of robots.
     * Consistency kept using RPC calls to UserService.
     * @param robotId robot to delete (Id must be set correctly).
     */
    @Override
    public void deleteRobot(long robotId) throws AbortedException, ErrorConnectionException {
        logger.trace("deleteRobot() was called with parameters: id = {}.", robotId);
        Session session = sessionFactory.getCurrentSession();

        RobotSerial robot = getRobotCheckingConsistency(robotId);
        deleteRobotRecordFromUser(robot);

        logger.trace("Deleting robot {}", robot.getName());
        session.delete(robot);
        logger.trace("Robot {] deleted", robot.getName());

        logger.trace("deleteRobot() successfully deleted robot with id {}", robotId);
    }

    private RobotSerial getRobotCheckingConsistency(long robotId) throws AbortedException {
        RobotSerial robot;
        try {
            robot = getRobot(robotId);
        } catch (NotFoundException e) {
            logger.error("Robot with specified Id doesn't exist.", e);
            throw new AbortedException("Robot with specified Id doesn't exist.", "deleteRobot() was safely aborted",
                    RobotDaoImpl.class.getName());
        }
        return robot;
    }

    private void deleteRobotRecordFromUser(RobotSerial robotSerial) throws AbortedException, ErrorConnectionException {
        final String owner = robotSerial.getOwner();
        logger.trace("Deleting record from user {}", owner);
        TUser tUser = loadOwner(owner);

        TRobot tRobot = robotSerial.toTRobot();
        tUser.getRobots().remove(tRobot);

        updateOwner(tUser);
        logger.trace("Record from user {} deleted", robotSerial.getOwner());
    }

    private TUser loadOwner(String owner) throws ErrorConnectionException, AbortedException {
        TUser tUser = null;
        try {
            tUser = userService.getUser(owner);
        } catch (NotFoundException e) {
            logger.error("Inconsistent state: Robot contains user with id {}, but this user doesn't exist.", owner, e);
            throw new AbortedException("Inconsistent state: Robot contains user with id {}, but this user doesn't " +
                    "exist.", "deleteRobot() safely aborted", RobotDaoImpl.class.getName());
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return  tUser;
    }

    private void updateOwner(TUser tUser) throws AbortedException, ErrorConnectionException {
        try {
            userService.updateUser(tUser);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }

    /**
     * Tests if a robot exists at local DB using Hibernate ORM.
     * @param id id of a robot to test if it exists
     */
    @Override
    public boolean isExistsRobot(long id) {
        logger.trace("isExistsRobot() was called with parameters: id = {}", id);
        Session session = sessionFactory.getCurrentSession();
        RobotSerial robot = (RobotSerial) session.get(RobotSerial.class, id);
        return robot != null;
    }
}
