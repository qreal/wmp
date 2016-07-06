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

package com.qreal.robots.model.robot;

import java.util.List;

/**
 * Created by ageevdenis on 02-3-15.
 */
public class Message {

    private String from;
    private String type;
    private RobotInfo robot;
    private String user;
    private List<RobotInfo> robots;

    public Message() {
    }

    public Message(String from, String type) {
        this.from = from;
        this.type = type;
    }

    public Message(String from, String type, RobotInfo robots) {
        this.from = from;
        this.type = type;
        this.robot = robots;
    }

    public Message(String from, String type, List<RobotInfo> robots) {
        this.from = from;
        this.type = type;
        this.robots = robots;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RobotInfo getRobot() {
        return robot;
    }

    public void setRobot(RobotInfo robot) {
        this.robot = robot;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<RobotInfo> getRobots() {
        return robots;
    }

    public void setRobots(List<RobotInfo> robots) {
        this.robots = robots;
    }

}
