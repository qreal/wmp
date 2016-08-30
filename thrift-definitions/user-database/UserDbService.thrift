namespace java com.qreal.wmp.thrift.gen

include "../struct/User.thrift"

include "../exception/DbExceptions.thrift"

service UserDbService {
    //In case of exception save will be safely aborted
    void save(1: User.TUser user) throws (1: DbExceptions.TIdNotDefined e),
    //In case of exception update will be safely aborted
    void update(1: User.TUser user) throws (1: DbExceptions.TNotFound notFound, 2: DbExceptions.TIdNotDefined
    notDefined),
    //In case of exception undefined value will be returned
    User.TUser findByUserName(1: string username) throws (1: DbExceptions.TNotFound e),
    //No exceptions possible
    bool isUserExist(1: string username)
}

