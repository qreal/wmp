#!/bin/bash
cd SharedResources/src/main/webapp
sudo npm install
grunt
cd ../../..
sudo mvn clean
mvn install
cd ..

cd EditorService
mvn generate-sources
cd src/main/webapp
sudo npm install
grunt
cd ../../..
sudo mvn clean
mvn install
cd ..

cd DashboardService
sudo mvn clean
mvn install
cd ..

cd ../RobotsEditor
mvn generate-sources
cd src/main/webapp
sudo npm install
grunt
cd ../../..
sudo mvn clean
mvn install
cd ..
