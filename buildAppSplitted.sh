#build monolith
echo "1"
sh buildApp.sh
#build splitted parts
#sh thriftGenSplitted.sh
echo "2"
cd DBServices
#build DiagramDBService
cd DiagramService
mvn clean package
#build RobotsDBService
cd ../RobotsService
mvn clean package
#build UserDbService
cd ../UserService
mvn clean package
echo "Now use tomcat7 to start project"
