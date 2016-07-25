namespace java com.qreal.robots.thrift.gen

include "..//struct/Robot.thrift"

service RobotDbService {
    void registerRobot(1: Robot.TRobot tRobot),
    Robot.TRobot findById(1: i64 id),
    void deleteRobot(1: i64 id)
}
