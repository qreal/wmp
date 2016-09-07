package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.client.users.UserService;
import com.qreal.wmp.db.robot.exceptions.Aborted;
import com.qreal.wmp.db.robot.exceptions.ErrorConnection;
import com.qreal.wmp.db.robot.exceptions.NotFound;
import com.qreal.wmp.db.robot.model.robot.RobotSerial;
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

import java.util.List;

@Transactional
@Component("robotDao")
@Repository
public class RobotDaoImpl implements RobotDao {

    private static final Logger logger = LoggerFactory.getLogger(RobotDaoImpl.class);

    /** UserService used to resolve foreign key dependencies.*/
    private final UserService userService;

    private final SessionFactory sessionFactory;

    @Autowired
    public RobotDaoImpl(SessionFactory sessionFactory, UserService userService) {
        this.sessionFactory = sessionFactory;
        this.userService = userService;
    }

    /**
     * Saves robot at local DB using Hibernate ORM.
     * Foreign key on User (in username) will NOT be checked. (at least for now)
     *
     * @param robot robot to save (Id must not be set).
     */
    @Override
    public long save(@NotNull RobotSerial robot) {
        logger.trace("save method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();
        session.save(robot);
        logger.trace("save method saved robot {}", robot.getName());
        return robot.getId();
    }

    /**
     * Deletes robot from local DB using Hibernate ORM.
     * Loads User associated with this robot using UserService and deletes robot from it's list of robots.
     * Consistency kept using RPC calls to UserService.
     *
     * @param robotId robot to delete (Id must be set correctly).
     */
    @Override
    public void delete(long robotId) throws Aborted, ErrorConnection {
        logger.trace("delete method called with parameters: id = {}", robotId);
        Session session = sessionFactory.getCurrentSession();

        RobotSerial robot = null;
        try {
            robot = findById(robotId);
        } catch (NotFound e) {
            logger.error("Robot with specified Id doesn't exist.", e);
            throw new Aborted("Robot with specified Id doesn't exist.", "delete was safely aborted",
                    RobotDaoImpl.class.getName());
        }

        final String owner = robot.getOwner();
        logger.trace("Deleting record from user {}", owner);
        TUser tUser = null;
        try {
            tUser = userService.findByUserName(owner);
        } catch (NotFound e) {
            logger.error("Inconsistent state: Robot contains user with id {}, but this user doesn't exist.", owner, e);
            throw new Aborted("Inconsistent state: Robot contains user with id {}, but this user doesn't exist.",
                    "delete safely aborted", RobotDaoImpl.class.getName());
        }
        tUser.getRobots().remove(robot.toTRobot());
        userService.update(tUser);

        logger.trace("Record from user {} deleted", robot.getOwner());

        logger.trace("Deleting robot {}", robot.getName());
        session.delete(robot);
        logger.trace("Robot {] deleted", robot.getName());

        logger.trace("delete method deleted robot with id {}", robotId);
    }

    /**
     * Finds robot by id at local DB using Hibernate ORM.
     *
     * @param robotId id of robot to find
     */
    @Override
    @NotNull
    public RobotSerial findById(long robotId) throws NotFound {
        logger.trace("findById method called with parameters: robotId = {}", robotId);
        Session session = sessionFactory.getCurrentSession();

        RobotSerial robot = (RobotSerial) session.get(RobotSerial.class, robotId);
        if (robot == null) {
            throw new NotFound(String.valueOf(robotId), "Robot with specified id not found.");
        }
        return robot;
    }

    /**
     * Test if robot exists at local DB using Hibernate ORM.
     *
     * @param id id of robot to test if exists
     */
    @Override
    public boolean isExistsRobot(long id) {
        logger.trace("isExistsRobot method called with parameters: id = {}", id);
        Session session = sessionFactory.getCurrentSession();
        RobotSerial robot = (RobotSerial) session.get(RobotSerial.class, id);
        return robot != null;
    }

    /**
     * Updates robot at local DB using Hibernate ORM.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    @Override
    public void updateRobot(@NotNull RobotSerial robot) throws Aborted {
        logger.trace("updateRobot method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();
        if (!isExistsRobot(robot.getId())) {
            logger.error("Robot with specified Id doesn't exists.");
            throw new Aborted("Robot with specified Id doesn't exists. Use save instead.", "updateRobot safely " +
                    "aborted.", RobotDaoImpl.class.getName());
        }
        session.merge(robot);

        logger.trace("updateRobot successfully updated robot");
    }
}
