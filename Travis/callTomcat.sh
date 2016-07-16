#!/bin/bash
iter=1
all=20
until [ "`curl --silent --show-error --connect-timeout 1 -I http://localhost:8080/Robots_diagram | grep '302 Found'`" != "" ];
do
  if [ "$iter" -lt "$all" ]
  then
    echo "--- sleeping for 10 seconds"
    sleep 10
    let iter=$iter+1
  else
    echo "Server didn't return 302 found for long time"
    exit 1
  fi
done
echo "Server returned 302 found"
exit 0
