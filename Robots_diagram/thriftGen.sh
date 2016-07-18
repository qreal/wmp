#!/usr/bin/env bash

#Authorization service
authThriftDir="src/main/java/com/qreal/robots/components/authorization/thrift"
mkdir -p "$authThriftDir/gen"
thrift -gen java:beans -out "$PWD/src/main/java" "$authThriftDir/User.thrift"

#Dashboard service
#service side
dashThriftDir="src/main/java/com/qreal/robots/components/dashboard/thrift"
mkdir -p "$dashThriftDir/gen"
thrift -gen java -out "$PWD/src/main/java" "$dashThriftDir/service/robotService.thrift"

mkdir "src/main/webapp/resources/thrift/"

#client side
dashClientDir="src/main/webapp/resources/thrift"
mkdir -p "$dashClientDir/dashboard"
thrift -gen js -out "$dashClientDir/dashboard" "$dashThriftDir/service/robotService.thrift"

#DBAuthorization service
dbAuthThriftDir="src/main/java/com/qreal/robots/components/database/users/thrift"
mkdir -p "$dbAuthThriftDir/gen"
thrift -gen java:beans -out "$PWD/src/main/java" "$dbAuthThriftDir/service/User.thrift"
thrift -gen java -out "$PWD/src/main/java" "$dbAuthThriftDir/service/UserDbService.thrift"

#DBRobots service
dbRobotThriftDir="src/main/java/com/qreal/robots/components/database/robots/thrift"
mkdir -p "$dbRobotThriftDir/gen"
thrift -gen java -out "$PWD/src/main/java" "$dbRobotThriftDir/RobotDbService.thrift"
thrift -gen java -out "$PWD/src/main/java" "$dbRobotThriftDir/Robot.thrift"

#Editor service
#service side
editorThriftDir="src/main/java/com/qreal/robots/components/editor/thrift"
mkdir -p "$editorThriftDir/gen"
thrift -gen java -out "$PWD/src/main/java" "$editorThriftDir/service/editorService.thrift"

#client side
editorClientDir="src/main/webapp/resources/thrift/editor"
mkdir -p "$editorClientDir"
thrift -gen js:ts -out "$editorClientDir" "$editorThriftDir/service/editorService.thrift"