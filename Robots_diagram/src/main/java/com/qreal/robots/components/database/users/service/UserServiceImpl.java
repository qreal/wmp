package com.qreal.robots.components.database.users.service;

import com.qreal.robots.components.database.users.DAO.UserDAO;
import com.qreal.robots.components.authorization.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


//In service transactions are added to DAO. DAO only DB functions implements.

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;

    @Transactional
    public void save(User user) {
        userDAO.save(user);
    }

    @Transactional
    public User findByUserName(String username) {
        return userDAO.findByUserName(username);
    }

    @Transactional
    public boolean isUserExist(String username) {
        return userDAO.isUserExist(username);
    }

}
