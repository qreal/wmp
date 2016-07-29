#build monolith
sh buildApp.sh
#build splitted parts
sh thriftGenSplitted.sh
cd DBServices
#build DiagramDBService
cd DiagramService
mvn clean install
#build RobotsDBService
cd ../RobotsService
mvn clean install
#build UserDbService
cd ../UserService
mvn clean install
echo "Now use tomcat7 to start project"
