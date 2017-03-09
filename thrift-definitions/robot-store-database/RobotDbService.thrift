namespace java com.qreal.wmp.thrift.gen

include "../struct/Robot.thrift"

include "../exception/DbExceptions.thrift"


/**
* RobotDbService uses client to UsersDbService to resolve
* foreign key dependencies.
*
* The robot foreign key dependency of owner to Users table NOT checked,
* but foreign key from user to robot checked and if robot will be deleted,
* robot from user's list will be also deleted.
**/
service RobotDbService {

//CRUD ROBOT + isExists

    /**
    * In case of exception saveRobot operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * No cases for now
    **/
    i64 saveRobot(1: Robot.TRobot tRobot) throws (1: DbExceptions.TIdAlreadyDefined alreadyDefined,
                                                  2: DbExceptions.TAborted aborted),

    /** TNotFound is thrown if the robot with specified id is not found.*/
    Robot.TRobot getRobot(1: i64 id) throws (1: DbExceptions.TNotFound e),


    /**
    * In case of exception deleteRobot operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * 1. The robot to delete doesn't exist
    * 2. Inconsistency of db state - robot contains link to owner but this user doesn't exist
    *
    * TErrorConnection will be thrown in the following cases:
    * 1.The userService is unreachable
    **/
    void deleteRobot(1: i64 id) throws (1: DbExceptions.TAborted aborted,
                                        2: DbExceptions.TErrorConnection errorConnection),

    /**
    * In case of exception updateRobot operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * 1. The robot to update doesn't exist
    **/
    void updateRobot(1: Robot.TRobot tRobot) throws (1: DbExceptions.TAborted aborted,
                                                     2: DbExceptions.TIdNotDefined notDefined)

    /** No exceptions possible.*/
    bool isRobotExists(1: i64 id),
}


