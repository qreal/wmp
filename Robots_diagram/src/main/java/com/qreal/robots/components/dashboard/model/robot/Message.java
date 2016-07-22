package com.qreal.robots.components.dashboard.model.robot;

import java.util.List;

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
