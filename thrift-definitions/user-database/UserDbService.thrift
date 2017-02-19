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

//CRU USER + isExists

    /**
    * In case of exception saveUser operation will be safely aborted
    *
    * TAborted will be thrown in the following cases:
    * 1. Saving or updating of robots of user is aborted.
    * 2. Creating of rootFolder is aborted.
    *
    * TErrorConnection will be thrown in the following cases:
    * 1. The robotsService is unreachable
    * 1. The diagramsService is unreachable
    **/
    void saveUser(1: User.TUser user) throws (1: DbExceptions.TIdNotDefined notDefined
                                          2: DbExceptions.TAborted aborted,
                                          3: DbExceptions.TErrorConnection errorConnection),


    /**
    * In case of exception updateUser operation will be safely aborted
    *
    * TAborted will be thrown in the following cases:
    * 1. The user doesn't exist
    * 2. Saving or updating of robots of user is aborted.
    **/
    void updateUser(1: User.TUser user) throws (1: DbExceptions.TAborted aborted,
                                            2: DbExceptions.TIdNotDefined notDefined,
                                            3: DbExceptions.TErrorConnection errorConnection),

    /**
    * If not found User with specified username TNotFound will be thrown
    *
    * TErrorConnection will be thrown in the following cases:
    * 1. The robotsService is unreachable
    **/
    User.TUser getUser(1: string username) throws (1: DbExceptions.TNotFound notFound,
                                                          2: DbExceptions.TErrorConnection errorConnection),

    /** No exceptions possible.*/
    bool isUserExists(1: string username)
}

