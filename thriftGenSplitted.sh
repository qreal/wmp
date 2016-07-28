echo DBDiagrams service
diagramService="DBServices/DiagramService/src/main/java"
diagramServiceThrift="$diagramService/com/qreal/robots/thrift"
diagramServiceGen="$diagramService/com/qreal/robots/thrift/gen"
[ -d "$diagramServiceGen" ] && rm -rf "$diagramServiceGen"
mkdir -p "$diagramServiceGen"
echo diagram
thrift -gen java:beans -out "$diagramService" "$diagramServiceThrift/struct/Diagram.thrift"
echo DBService
thrift -gen java -out "$diagramService" "$diagramServiceThrift/service/DiagramDbService.thrift"


echo DBRobots service
robotsService="DBServices/RobotsService/src/main/java"
robotsServiceThrift="$robotsService/com/qreal/robots/thrift"
robotsServiceGen="$robotsService/com/qreal/robots/thrift/gen"
[ -d "$robotsServiceGen" ] && rm -rf "$robotsServiceGen"
mkdir -p "$robotsServiceGen"
echo robots
thrift -gen java:beans -out "$robotsService" "$robotsServiceThrift/struct/Robot.thrift"
echo DBService
thrift -gen java -out "$robotsService" "$robotsServiceThrift/service/RobotDbService.thrift"


echo DBAuthorization service
userService="DBServices/UserService/src/main/java"
userServiceThrift="$userService/com/qreal/robots/thrift"
userServiceGen="$userService/com/qreal/robots/thrift/gen"
echo robots
thrift -gen java:beans -out "$userService" "$userServiceThrift/struct/Robot.thrift"
echo user
thrift -gen java:beans -out "$userService" "$userServiceThrift/struct/User.thrift"
echo DBService
thrift -gen java -out "$userService" "$userServiceThrift/service/UserDbService.thrift"
thrift -gen java -out "$userService" "$userServiceThrift/service/RobotDbService.thrift"
