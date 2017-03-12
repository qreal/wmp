#!/usr/bin/env bash
cd ../../editor-core
mvn clean install
cd ../editor-service
mvn clean install
cd ../deployment/allintomcat
mvn clean install
 
