
cd SharedResources/src/main/webapp
sudo npm install
grunt
cd ../../..
mvn clean install
cd ../Robots_diagram/src/main/webapp
sudo npm install
grunt
cd ../../..
echo "Now use IntelliJ to start project"
