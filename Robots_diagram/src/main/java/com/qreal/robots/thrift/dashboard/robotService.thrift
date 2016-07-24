namespace java com.qreal.robots.thrift.gen

service RobotServiceThrift {
    bool registerRobot(1: string robotName, 2:string ssid);
    bool deleteRobot(1: string robotName)
}
