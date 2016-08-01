#!/bin/bash

sh buildApp.sh

cd DBServices/DiagramService
mvn clean package

cd ../RobotsService
mvn clean package

cd ../UserService
mvn clean package