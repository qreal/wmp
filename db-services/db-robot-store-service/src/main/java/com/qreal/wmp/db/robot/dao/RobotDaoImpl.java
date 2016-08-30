package com.qreal.wmp.db.robot.dao;

import com.qreal.wmp.db.robot.client.users.UserService;
import com.qreal.wmp.db.robot.client.exceptions.NotFound;
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
    @Autowired
    private UserService userService;

    private final SessionFactory sessionFactory;

    @Autowired
    public RobotDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
    public void delete(long robotId) {
        logger.trace("delete method called with parameters: id = {}", robotId);
        Session session = sessionFactory.getCurrentSession();
        RobotSerial robot = findById(robotId);
        final String owner = robot.getOwner();
        logger.trace("Deleting record from user {}", owner);
        try {
            TUser tUser = userService.findByUserName(owner);
            tUser.getRobots().remove(robot.toTRobot());
            userService.update(tUser);
        } catch (NotFound e) {
            logger.error("Inconsistent state: Robot contains user with id {}, but this user doesn't exist.", owner);
        }
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
    public RobotSerial findById(long robotId) {
        logger.trace("findById method called with parameters: robotId = {}", robotId);
        Session session = sessionFactory.getCurrentSession();

        List<RobotSerial> robots = session.createQuery("from RobotSerial where id=?").setParameter(0, robotId).list();
        logger.trace("findById method extracted list of results from session with {} elements. First will be " +
                "returned.", robots.size());
        return robots.stream().findFirst().orElse(null);
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

        List<RobotSerial> robots = session.createQuery("from RobotSerial where id=?").setParameter(0, id).list();
        logger.trace("isExistsRobot extracted list with {} robots with id {}", robots.size(), id);
        return !robots.isEmpty();
    }

    /**
     * Updates robot at local DB using Hibernate ORM.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    @Override
    public void updateRobot(@NotNull RobotSerial robot) {
        logger.trace("updateRobot method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();

        session.merge(robot);

        logger.trace("updateRobot successfully updated robot");
    }
}
