#!/bin/bash
iter=1
all=120
until [ "`curl --silent --show-error --connect-timeout 1 -I http://localhost:8080/auth | grep '302 Found'`" != "" ];
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
echo "auth-service found"
iter=1
all=120
until [ "`curl --silent --show-error --connect-timeout 1 -I http://localhost:8081/dashboard | grep '302
Found'`" != "" ];
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
echo "dashboard-service found"
iter=1
all=120
until [ "`curl --silent --show-error --connect-timeout 1 -I http://localhost:8082/editor/editor | grep '302
Found'`" != "" ];
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
echo "editor-service found"
exit 0
