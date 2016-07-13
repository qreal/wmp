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

#client side
dashClientDir="src/main/webapp/resources/thrift"
mkdir -p "$dashClientDir/dashboard"
thrift -gen js -out "$dashClientDir/dashboard" "$dashThriftDir/service/robotService.thrift"

#DBAuthorization service
dbAuthThriftDir="src/main/java/com/qreal/robots/components/database/users/thrift"
mkdir -p "$dbAuthThriftDir/gen"
thrift -gen java:beans -out "$PWD/src/main/java" "$dbAuthThriftDir/service/User.thrift"
thrift -gen java -out "$PWD/src/main/java" "$dbAuthThriftDir/service/UserDbService.thrift"
