namespace java com.qreal.wmp.thrift.gen

service RobotServiceThrift {
    bool registerRobot(1: string robotName, 2:string ssid);
    bool deleteRobot(1: i64 robotId)
}
