namespace java com.qreal.robots.components.database.robots.thrift.gen

include "Robot.thrift"

service RobotDbService {
    string registerRobot(1: Robot.TRobot tRobot),
    Robot.TRobot findByName(1: string name),
    string deleteRobot(1: string name)
}