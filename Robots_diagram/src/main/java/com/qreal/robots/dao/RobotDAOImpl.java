/*
 * Copyright Vladimir Zakharov
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
import com.qreal.robots.model.robot.Robot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dageev on 07.03.15.
 */

@Transactional
@Repository
public class RobotDAOImpl implements RobotDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public RobotDAOImpl() {

    }

    public RobotDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Robot robot) {
        Session session = sessionFactory.getCurrentSession();
        session.save(robot);
    }

    public void delete(Robot robot) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(robot);
    }

    public Robot findByName(String robotName) {
        Session session = sessionFactory.getCurrentSession();

        List<Robot> robots = session.createQuery("from Robot where name=?").setParameter(0, robotName).list();
        return (robots.size() > 0) ? robots.get(0) : null;
    }

    public boolean isRobotExists(String robotName) {
        Session session = sessionFactory.getCurrentSession();

        List<User> robots = session.createQuery("from Robot where name=?").setParameter(0, robotName).list();
        return robots.size() > 0;
    }

}
