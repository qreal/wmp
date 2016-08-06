namespace java com.qreal.wmp.thrift.gen

include "../struct/User.thrift"

service UserDbService {
    void save(1: User.TUser user),
    void update(1: User.TUser user),
    User.TUser findByUserName(1: string username),
    bool isUserExist(1: string username)
}
