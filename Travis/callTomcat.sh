#!/bin/bash

until [ "`curl --silent --show-error --connect-timeout 1 -I http://localhost:8080/Robots_diagram | grep 'Coyote'`" != "" ];
do
  echo --- sleeping for 10 seconds
  sleep 10
done
if ["`curl --silent --show-error --connect-timeout 1 -I http://localhost:8080/Robots_diagram | grep '200'`" != ""]
then
  echo "0"
  exit 0
else
  echo "1"
  exit 1
fi
