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

import com.qreal.robots.model.robot.Robot;

/**
 * Created by vladzx on 22.06.15.
 */
public interface RobotDAO {

    public void save(Robot robot);

    public void delete(Robot robot);

    public Robot findByName(String robotName);

    public boolean isRobotExists(String robotName);
}
