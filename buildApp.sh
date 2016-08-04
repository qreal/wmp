#!/bin/bash

sudo mvn clean
mvn install

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

cd AuthService
sudo mvn clean
mvn install
cd ..

cd DBServices/DiagramService
mvn clean package

cd ../RobotsService
mvn clean package

cd ../UserService
mvn clean package

cd ../..
