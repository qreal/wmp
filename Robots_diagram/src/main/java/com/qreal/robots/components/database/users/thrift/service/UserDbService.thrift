namespace java com.qreal.robots.components.database.users.thrift.gen

include "User.thrift"

service UserDbService {
    void save(1: User.TUser user),
    User.TUser findByUserName(1: string username),
    bool isUserExist(1: string username)
}