namespace java com.qreal.wmp.thrift.gen

include "../struct/User.thrift"

include "../exception/DbExceptions.thrift"

/**
* UserDbService uses client to RobotDbService and DiagramDbService
* to resolve foreign key dependencies.
*
* UserDbService foreign key to rootFolder and robots will be checked,
* but NO backwards checking from RobotDbService and DiagramDbService
* on User as a foreign key will be performed.
**/
service UserDbService {
    /**
    * In case of exception save will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. Aborted saving or updating of robots of saving user
    * 2. Aborted creating of rootFolder
    *
    * TErrorConnection will be thrown in following cases:
    * 1.Robots service is unreachable
    * 1.Diagrams service is unreachable
    **/
    void save(1: User.TUser user) throws (1: DbExceptions.TIdNotDefined notDefined
                                          2: DbExceptions.TAborted aborted,
                                          3: DbExceptions.TErrorConnection errorConnection),


    /**
    * In case of exception update will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. User doesn't exist
    * 2. Aborted saving or updating of robots of updating user
    **/
    void update(1: User.TUser user) throws (1: DbExceptions.TAborted aborted,
                                            2: DbExceptions.TIdNotDefined notDefined,
                                            3: DbExceptions.TErrorConnection errorConnection),

    /**
    * If not found User with specified username TNotFound will be thrown
    * TErrorConnection will be thrown in following cases:
    * 1.Robots service is unreachable
    **/
    User.TUser findByUserName(1: string username) throws (1: DbExceptions.TNotFound notFound,
                                                          2: DbExceptions.TErrorConnection errorConnection),

    //No exceptions possible
    bool isUserExist(1: string username)
}

