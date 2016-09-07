namespace java com.qreal.wmp.thrift.gen

include "../struct/Robot.thrift"

include "../exception/DbExceptions.thrift"


/**
* RobotDbService uses client to UsersDbService to resolve
* foreign key dependencies.
*
* Robot foreign key dependency of owner to Users table NOT checked,
* but foreign key from user to robot checked and if robot will be deleted,
* robot from user's list will be also deleted.
**/
service RobotDbService {
    /**
    * In case of exception registerRobot will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * No cases for now
    **/
    i64 registerRobot(1: Robot.TRobot tRobot) throws (1: DbExceptions.TIdAlreadyDefined alreadyDefined,
                                                      2: DbExceptions.TAborted aborted),

    //If not found Robot with specified id TNotFound will be thrown
    Robot.TRobot findById(1: i64 id) throws (1: DbExceptions.TNotFound e),


    /**
    * In case of exception deleteRobot will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. Robot to delete doesn't exist
    * 2. Inconsistency of db state - robot contains link to owner but this user doesn't exist
    *
    * TErrorConnection will be thrown in following cases:
    * 1.User service is unreachable
    **/
    void deleteRobot(1: i64 id) throws (1: DbExceptions.TAborted aborted,
                                        2: DbExceptions.TErrorConnection errorConnection),

    //No exceptions possible
    bool isRobotExists(1: i64 id),

    /**
    * In case of exception deleteRobot will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. Robot to update doesn't exist
    **/
    void updateRobot(1: Robot.TRobot tRobot) throws (1: DbExceptions.TAborted aborted,
                                                     2: DbExceptions.TIdNotDefined notDefined)
}


