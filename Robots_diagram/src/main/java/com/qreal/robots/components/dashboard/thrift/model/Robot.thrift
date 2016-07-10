namespace java robot

include "User.thrift"

struct Robot {
    1 : i32 id,
    2 : string name,
    3 : string ssid,
    4 : User owner
}
