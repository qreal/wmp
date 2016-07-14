namespace java com.qreal.robots.components.database.robots.thrift.gen

include "Robot.thrift"

service RobotDbService {
    void registerRobot(1: Robot.TRobot tRobot),
    Robot.TRobot findByName(1: string name),
    void deleteRobot(1: string name)
}