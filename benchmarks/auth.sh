#!/usr/bin/env bash

curl -X POST \
  http://localhost:9080/shopritemobileapplication/api/authorization/v7/authorization \
  -H 'Accept: application/vnd.mywebgrocer.session+json' \
  -H 'Authorization: 566E23CB-00BC-46C4-A8AE-66AE8FBB896F' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 4d51dcb1-3ce2-4846-b950-ebabd1708314' \
  -d '{}'
