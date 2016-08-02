#!/bin/bash
sh buildApp.sh

mvn clean install

cd DBServices/DiagramService
mvn clean package

cd ../RobotsService
mvn clean package

cd ../UserService
mvn clean package
