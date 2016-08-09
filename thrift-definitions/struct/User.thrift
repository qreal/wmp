namespace java com.qreal.wmp.thrift.gen

include "Robot.thrift"

struct TUserRole {
    1: i32 id,
    2: string role
}

struct TUser {
    1: string username,
    2: string password,
    3: bool enabled,
    4: set<TUserRole> roles,
    5: set<Robot.TRobot> robots
}
