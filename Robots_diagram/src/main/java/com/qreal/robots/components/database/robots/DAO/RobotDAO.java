package com.qreal.robots.components.database.robots.DAO;

import com.qreal.robots.components.dashboard.model.robot.Robot;

public interface RobotDAO {

    void save(Robot robot);

    void delete(Robot robot);

    Robot findByName(String robotName);

    boolean isRobotExists(String robotName);
}
