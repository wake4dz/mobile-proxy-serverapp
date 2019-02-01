#!/usb/bin/env bash

curl -X GET \
  http://srmobile-serverapp-develop-papertrail.mybluemix.net/api/stores/v7/chains/0F60396/stores \
  -H 'Accept: application/vnd.mywebgrocer.stores+json' \
  -H 'Authorization: a7890929-7e71-4489-842f-4043a14e4c20' \
  -H 'Postman-Token: 1c2a4416-10b0-4518-9963-6792138bdac3' \
  -H 'cache-control: no-cache'
