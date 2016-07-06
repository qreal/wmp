/*
 * Copyright Vladimir Zakharov
 * Copyright Denis Ageev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.dao;

import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.auth.UserRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dageev on 04.03.15.
 */
@Repository
public class UserDAOImpl implements UserDAO {

    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private SessionFactory sessionFactory;

    public UserDAOImpl() {
    }

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        UserRole userRole = new UserRole(user, ROLE_USER);
        session.save(userRole);
    }

    public User findByUserName(String username) {
        Session session = sessionFactory.getCurrentSession();

        List<User> users = session.createQuery("from User where username=:username").
                setParameter("username", username).list();
        return (users.size() > 0) ? users.get(0) : null;
    }

    public boolean isUserExist(String username) {
        Session session = sessionFactory.getCurrentSession();

        List<User> users = session.createQuery("from User where username=:username")
                .setParameter("username", username).list();
        return users.size() > 0;

    }

}