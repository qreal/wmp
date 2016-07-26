package com.qreal.robots.components.database.robots.DAO;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
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
@Component("robotDAO")
@Repository
public class RobotDAOImpl implements RobotDAO {

    private static final Logger logger = LoggerFactory.getLogger(RobotDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public RobotDAOImpl() {
    }

    public RobotDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Robot robot) {
        logger.trace("save method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();
        session.save(robot);
        logger.trace("save method saved robot {}", robot.getName());
    }

    @Override
    public void delete(Robot robot) {
        logger.trace("delete method called with parameters: robot = {}", robot.getName());
        Session session = sessionFactory.getCurrentSession();
        session.delete(robot);
        logger.trace("delete method deleted robot {}", robot.getName());
    }

    @Override
    public Robot findById(long robotId) {
        logger.trace("findById method called with parameters: robotId = {}", robotId);
        Session session = sessionFactory.getCurrentSession();

        List<Robot> robots = session.createQuery("from Robot where id=?").setParameter(0, robotId).list();
        logger.trace("findById method extracted list of results from session with {} elements. First will be " +
                "returned.", robots.size());
        return robots.stream().findFirst().orElse(null);
    }

    @Override
    public boolean isRobotExists(String robotName) {
        logger.trace("isRobotExists method called with parameters: robotName = {}", robotName);
        Session session = sessionFactory.getCurrentSession();

        List<User> robots = session.createQuery("from Robot where name=?").setParameter(0, robotName).list();
        logger.trace("isRobotExists extracted list with {} robots with name {}", robots.size(), robotName);
        return !robots.isEmpty();
    }
}
