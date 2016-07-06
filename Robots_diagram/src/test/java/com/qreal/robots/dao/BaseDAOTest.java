/*
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

/**
 * Created by dageev on 14.03.15.
 */
public class BaseDAOTest {

    public static final String USER_NAME = "user";
    public static final String USER_NAME2 = "user2";
    public static final String USER_NAME3 = "user3";
    public static final String ROBOT_NAME = "robot";
    public static final String ROBOT_NAME2 = "robot2";
    public static final String PASSWORD = "password";

    protected User getAndSaveUser(String username, UserDAO userDAO) {
        User user = new User(username, PASSWORD, true);
        userDAO.save(user);
        return user;
    }

}
