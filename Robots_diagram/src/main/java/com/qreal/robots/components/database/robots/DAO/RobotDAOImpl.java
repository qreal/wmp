package com.qreal.robots.components.database.robots.DAO;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component("RobotDAO")
@Repository
public class RobotDAOImpl implements RobotDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public RobotDAOImpl() {
    }

    public RobotDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Robot robot) {
        Session session = sessionFactory.getCurrentSession();
        session.save(robot);
    }

    @Override
    public void delete(Robot robot) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(robot);
    }

    @Override
    public Robot findByName(String robotName) {
        Session session = sessionFactory.getCurrentSession();

        List<Robot> robots = session.createQuery("from Robot where name=?").setParameter(0, robotName).list();
        return (robots.size() > 0) ? robots.get(0) : null;
    }

    @Override
    public boolean isRobotExists(String robotName) {
        Session session = sessionFactory.getCurrentSession();

        List<User> robots = session.createQuery("from Robot where name=?").setParameter(0, robotName).list();
        return robots.size() > 0;
    }
}
