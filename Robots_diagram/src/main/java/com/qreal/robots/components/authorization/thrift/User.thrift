namespace java com.qreal.robots.components.authorization.thrift.gen

struct TRobot {
    1: i32 id,
    2: string name,
    3: string ssid
}

struct TUserRole {
    1: i32 userRoleId,
    3: string role
}

struct TUser {
    1: string username,
    2: string password,
    3: bool enabled,
    4: set<TUserRole> roles,
    5: set<TRobot> robots
}
