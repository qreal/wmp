cd ../../editor-core
mvn clean install
cd ../editor-service
mvn clean install
cd ../deployment/allintomcat
mvn clean install
mvn tomcat7:run-war 
