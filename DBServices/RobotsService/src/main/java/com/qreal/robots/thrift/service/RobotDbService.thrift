namespace java com.qreal.robots.thrift.gen

include "../struct/Robot.thrift"

service RobotDbService {
    i64 registerRobot(1: Robot.TRobot tRobot),
    Robot.TRobot findById(1: i64 id),
    void deleteRobot(1: i64 id),
    bool isRobotExists(1: i64 id)
}
