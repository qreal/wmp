package com.qreal.wmp.auth.database.users;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is the main class for work with users tables in database.
 * Because of not very complex logic i decided do not create separate userService
 */
@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Retrieves a single user by id.
     */
    public User get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        User person = session.get(User.class, id);
        logger.trace("User {} taken from database using id {}", person.getUsername(), id);
        return person;
    }

    /**
     * Retrieves a list of users by login.
     * List here to be sure...
     */
    public List get(String login) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM User E WHERE E.username = :login");
        query.setParameter("login", login);
        List results = query.list();
        if (results.isEmpty()) {
            logger.trace("User {} was not found using login", login);
        }
        else {
            logger.trace("User was taken from database using login {}", login);
        }
        return results;
    }

    /**
     * Retrieves a single user by login.
     * (Take first from list of returned)
     */
    public UserDetails loadUserByUsername(String login) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM User E WHERE E.username = :login");
        query.setParameter("login", login);
        List<User> results = query.list();
        if (results.size() > 1 || results.size() == 0) {
            logger.trace("User {} was not found using login", login);
            return null;
        }
        logger.trace("User {} taken from database using login", login);
        return results.get(0);
    }

    /**
     * Retrieves all users from database.
     */
    public List<User> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM User E");
        List<User> results = query.list();
        logger.trace("{} users were loaded from database", results.size());
        return results;
    }

    /**
     * Adds a new user.
     */
    public void add(User person) {
        if (get(person.getUsername()).size() > 0) {
            return;
        }
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
        logger.trace("{} user was saved to database", person.getUsername());
    }

    /**z
     * Deletes an existing user by id.
     */
    public void delete(Integer id) {

        Session session = sessionFactory.getCurrentSession();
        User person = session.get(User.class, id);
        session.delete(person);
        logger.trace("{} user with id {} was deleted from database", person.getUsername(), id);

    }

    /**
     * Edits an existing user.
     */
    public void edit(User person) {
        Session session = sessionFactory.getCurrentSession();
        User existingPerson = session.get(User.class, person.getId());
        existingPerson.setUsername(person.getUsername());
        existingPerson.setPassword(person.getPassword());
        //If we delete collection we will create orphan, that's not really good
        existingPerson.getAuthorities().clear();
        existingPerson.getAuthorities().addAll(person.getAuthorities());
        session.save(existingPerson);
        logger.trace("{} user was edited in database", person.getUsername());

    }

}