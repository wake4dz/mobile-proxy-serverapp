#!/usr/bin/env bash

curl -X POST \
  http://srmobile-serverapp-develop-papertrail.mybluemix.net/api/authorization/v7/authorization \
  -H 'Accept: application/vnd.mywebgrocer.session+json' \
  -H 'Authorization: 566E23CB-00BC-46C4-A8AE-66AE8FBB896F' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: a156dcd7-3982-45b1-ba90-329a8d85d7c9' \
  -H 'cache-control: no-cache' \
  -d '{}'
