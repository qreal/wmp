package com.qreal.robots.components.database.robots.DAO;

import com.qreal.robots.components.dashboard.model.robot.Robot;

public interface RobotDAO {

    public void save(Robot robot);

    public void delete(Robot robot);

    public Robot findByName(String robotName);

    public boolean isRobotExists(String robotName);
}
