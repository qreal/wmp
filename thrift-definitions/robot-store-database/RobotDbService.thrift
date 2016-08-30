namespace java com.qreal.wmp.thrift.gen

include "../struct/Robot.thrift"

include "../exception/DbExceptions.thrift"

service RobotDbService {
    //In case of exception register will be safely aborted
    i64 registerRobot(1: Robot.TRobot tRobot) throws (1: DbExceptions.TIdAlreadyDefined e),
    //In case of exception undefined value will be returned
    Robot.TRobot findById(1: i64 id) throws (1: DbExceptions.TNotFound e),
    //In case of exception delete will be safely aborted
    void deleteRobot(1: i64 id) throws (1: DbExceptions.TNotFound e),
    //No exceptions possible
    bool isRobotExists(1: i64 id),
    //In case of exception update will be safely aborted
    void updateRobot(1: Robot.TRobot tRobot) throws (1: DbExceptions.TNotFound notFound,
                                                     2: DbExceptions.TIdNotDefined notDefined)
}


