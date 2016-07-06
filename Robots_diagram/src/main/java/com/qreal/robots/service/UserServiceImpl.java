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

package com.qreal.robots.service;

import com.qreal.robots.dao.UserDAO;
import com.qreal.robots.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by vladzx on 22.06.15.
 */
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
