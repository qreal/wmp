package com.qreal.robots.components.database.users.dao;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.components.authorization.model.auth.UserRole;
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

    public UserDaoImpl() {
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(User user) {
        logger.trace("save method called with parameters: user = {}", user.getUsername());
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        UserRole userRole = new UserRole(user, ROLE_USER);
        session.save(userRole);
        logger.trace("save method saved user {}", user.getUsername());
    }

    @Override
    public User findByUserName(String username) {
        logger.trace("findByUserName method called with paremeters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();

        List<User> users = session.createQuery("from User where username=:username").
                setParameter("username", username).list();
        logger.trace("findByUserName method extracted list of results from session with {} elements. First will be " +
                "returned.", users.size());

        return users.stream().findFirst().orElse(null);
    }

    @Override
    public boolean isUserExist(String username) {
        logger.trace("isUserExist method called with parameters: username = {}", username);
        Session session = sessionFactory.getCurrentSession();

        List<User> users = session.createQuery("from User where username=:username").
                setParameter("username", username).list();
        logger.trace("isUserExist extracted list with {} users with name {}", users.size(), username);
        return !users.isEmpty();

    }

}