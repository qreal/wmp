package com.qreal.robots.dao;

import com.qreal.robots.client.UserService;
import com.qreal.robots.model.robot.RobotSerial;
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

@Transactional
@Component("robotDao")
@Repository
public class RobotDaoImpl implements RobotDao {

    private static final Logger logger = LoggerFactory.getLogger(RobotDaoImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionFactory sessionFactory;

    public RobotDaoImpl() {
    }

    public RobotDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long save(RobotSerial robot) {
        logger.trace("save method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();
        session.save(robot);
        logger.trace("save method saved robot {}", robot.getName());
        return robot.getId();
    }

    @Override
    public void delete(RobotSerial robot) {
        logger.trace("delete method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();

        logger.trace("Deleting record from user {}", robot.getOwner());
        TUser tUser = userService.findByUserName(robot.getOwner());
        if (tUser != null) {
            tUser.getRobots().remove(robot.toTRobot());
            userService.update(tUser);
        }
        logger.trace("Record from user {} deleted", robot.getOwner());

        logger.trace("Deleting robot {}", robot.getName());
        session.delete(robot);
        logger.trace("Robot {] deleted", robot.getName());

        logger.trace("delete method deleted robot {}", robot.getName());
    }

    @Override
    public RobotSerial findById(long robotId) {
        logger.trace("findById method called with parameters: robotId = {}", robotId);
        Session session = sessionFactory.getCurrentSession();

        List<RobotSerial> robots = session.createQuery("from RobotSerial where id=?").setParameter(0, robotId).list();
        logger.trace("findById method extracted list of results from session with {} elements. First will be " +
                "returned.", robots.size());
        return robots.stream().findFirst().orElse(null);
    }

    @Override
    public boolean isRobotExists(long id) {
        logger.trace("isRobotExists method called with parameters: id = {}", id);
        Session session = sessionFactory.getCurrentSession();

        List<RobotSerial> robots = session.createQuery("from RobotSerial where id=?").setParameter(0, id).list();
        logger.trace("isRobotExists extracted list with {} robots with id {}", robots.size(), id);
        return !robots.isEmpty();
    }

    @Override
    public void updateRobot(RobotSerial robot) {
        logger.trace("updateRobot method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();

        session.merge(robot);

        logger.trace("updateRobot successfully updated robot");
    }
}
