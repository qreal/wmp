#!/usr/bin/env bash

#Authorization service
authThriftDir="src/main/java/com/qreal/robots/components/authorization/thrift"
mkdir "$authThriftDir/gen"
thrift -gen java -out "$PWD/src/main/java" "$authThriftDir/User.thrift"

#Dashboard service
#service side
dashThriftDir="src/main/java/com/qreal/robots/components/dashboard/thrift"
mkdir "$dashThriftDir/gen"
thrift -gen java -out "$PWD/src/main/java" "$dashThriftDir/service/robotService.thrift"

mkdir "src/main/webapp/resources/thrift/"

#client side
dashClientDir="src/main/webapp/resources/thrift/dashboard"
mkdir "$dashClientDir"
thrift -gen js:ts -out "$dashClientDir" "$dashThriftDir/service/robotService.thrift"

#Editor service
#service side
editorThriftDir="src/main/java/com/qreal/robots/components/editor/thrift"
mkdir "$editorThriftDir/gen"
thrift -gen java -out "$PWD/src/main/java" "$editorThriftDir/service/editorService.thrift"

#client side
editorClientDir="src/main/webapp/resources/thrift/editor"
mkdir "$editorClientDir"
thrift -gen js:ts -out "$editorClientDir" "$editorThriftDir/service/editorService.thrift"
