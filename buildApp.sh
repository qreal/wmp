#Generate Shared resources
cd SharedResources/src/main/webapp
sudo npm install
grunt
cd ../../..
sudo mvn clean
mvn install
cd ..
#Generate robots-editor
cd RobotsEditor
sh thriftGen.sh
cd src/main/webapp
sudo npm install
grunt
cd ../../..
sudo mvn clean
mvn install
cd ..
#Generate main project
sudo mvn clean
mvn install
echo "Now use tomcat7 to start project"
