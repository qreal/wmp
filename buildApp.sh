#!/bin/bash
cd SharedResources/src/main/webapp
sudo npm install
grunt

cd ../../..
sudo mvn clean
mvn install

cd ../RobotsEditor
mvn generate-sources
cd src/main/webapp
sudo npm install
grunt

cd ../../..
sudo mvn clean
mvn install
cd ..
