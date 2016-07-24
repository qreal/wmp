
thriftSourcesDir="src/main/java/com/qreal/robots/thrift"
thriftServerGenDir="src/main/java/com/qreal/robots/thrift/gen"
thriftClientGenDir="src/main/webapp/resources/thrift"

echo delete server gen dir
[ -d "$thriftServerGenDir" ] && rm -rf "$thriftServerGenDir"
echo create servet gen dir
mkdir -p "$thriftServerGenDir"

echo delete client gen dir
[ -d "$thriftClientGenDir" ] && rm -rf "$thriftClientGenDir"
echo create client gen dir
mkdir -p "$thriftClientGenDir"

echo Dashboard service
echo service side
thrift -gen java -out "$PWD/src/main/java" "$thriftSourcesDir/dashboard/robotService.thrift"
echo client side
mkdir -p "$thriftClientGenDir/dashboard"
thrift -gen js -out "$thriftClientGenDir/dashboard" "$thriftSourcesDir/dashboard/robotService.thrift"

echo DBAuthorization service
echo user
thrift -gen java:beans -out "$PWD/src/main/java" "$thriftSourcesDir/struct/User.thrift"
echo DBService
thrift -gen java -out "$PWD/src/main/java" "$thriftSourcesDir/userDB/UserDbService.thrift"

echo DBRobots service
echo robot
thrift -gen java:beans -out "$PWD/src/main/java" "$thriftSourcesDir/struct/Robot.thrift"
echo DBService
thrift -gen java -out "$PWD/src/main/java" "$thriftSourcesDir/robotDB/RobotDbService.thrift"

echo DBDiagrams service
echo diagram
thrift -gen java:beans -out "$PWD/src/main/java" "$thriftSourcesDir/struct/Diagram.thrift"
echo DBService
thrift -gen java -out "$PWD/src/main/java" "$thriftSourcesDir/diagramDB/DiagramDbService.thrift"

echo Editor service
echo service side
thrift -gen java -out "$PWD/src/main/java" "$thriftSourcesDir/editor/EditorService.thrift"
echo client side
mkdir -p "$thriftClientGenDir/editor"
#import not working normally with typescript
tail -n +4 "$thriftSourcesDir/editor/EditorService.thrift"  > "$thriftClientGenDir/editor/EditorService.thrift"
sed -i -e 's/Diagram.TDiagram/TDiagram/g' "$thriftClientGenDir/editor/EditorService.thrift"
sed -i -e 's/Diagram.TFolder/TFolder/g' "$thriftClientGenDir/editor/EditorService.thrift"
tail -n +2 "$thriftSourcesDir/struct/Diagram.thrift" >> "$thriftClientGenDir/editor/EditorService.thrift"
thrift -gen js:ts -out "$thriftClientGenDir/editor" "$thriftClientGenDir/editor/EditorService.thrift"
