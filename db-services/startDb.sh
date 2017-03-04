#!/usr/bin/env bash
cd db-robot-store-service
mvn exec:java &
cd ../db-user-service
mvn exec:java &
cd ../db-diagram-service
mvn exec:java &
