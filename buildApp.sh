#Generate Shared resources
cd SharedResources/src/main/webapp
sudo npm install
grunt
cd ../../..
sudo mvn clean
mvn install
cd ..
echo "shared resources done"
#Generate robots-editor
cd RobotsEditor
mvn generate-sources
#sh thriftGen.sh
cd src/main/webapp
sudo npm install
grunt
cd ../../..
sudo mvn clean
mvn install
cd ..
#Generate main project
#sudo mvn clean
mvn install # do we really need this?
