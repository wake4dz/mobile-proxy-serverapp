# Order Management APIs

| Server Type       | Value                         |
|-------------------|-------------------------------|
| DEV               | srdepmobileapp                |
| PROD              | shopritemobileproddep         |

## GET Orders
```sh
https://{DEV}.mybluemix.net/api/checkout/v7/orders/user/{userID}/store/{storeID}
```

| Key             | Value                                                  |
|-----------------|--------------------------------------------------------|
| Accept          | application/vnd.mywebgrocer.orders+json                |
| Authorization   | {token}                                                |


## GET Order Detail
```sh
https://{DEV}.mybluemix.net/api/checkout/v7/order/{orderID}/user/{userID}
```

| Key             | Value                                                  |
|-----------------|--------------------------------------------------------|
| Accept          | application/vnd.mywebgrocer.order-detail-v3+json       |
| Authorization   | {token}                                                |


## GET Change Order
```sh
https://{DEV}.mybluemix.net/api/checkout/v7/user/{userID}/store/{storeID}/changed/order
```

| Key             | Value                                                  |
|-----------------|--------------------------------------------------------|
| Accept          | application/vnd.mywebgrocer.changed-order+json         |
| Authorization   | {token}                                                |


## DELETE Change Order
```sh
https://{DEV}.mybluemix.net/api/checkout/v7/user/{userID}/store/{storeID}/changed/order
```

| Key             | Value                                                  |
|-----------------|--------------------------------------------------------|
| Accept          | application/vnd.mywebgrocer.changed-order+json         |
| Authorization   | {token}                                                |


## GET Change Order Message
```sh
https://{DEV}.mybluemix.net/api/checkout/v7/order/{orderID}/user/{userID}/store/{storeID}/to/cart
```

| Key             | Value                                                  |
|-----------------|--------------------------------------------------------|
| Accept          | application/vnd.mywebgrocer.message+json               |
| Authorization   | {token}                                                |

|Sample response                                                                 |
|--------------------------------------------------------------------------------|
|[{"Text":"You currently have items in your cart. Do you wish to add your current cart items to Order 21327172 before proceeding to change the order?","Code":"ChangeOrderMergeCartWarn"}]|


## Initiate (PUT) Change Order
```sh
https://{DEV}.mybluemix.net/api/checkout/v7/order/{orderID}/user/{userID}/store/{storeID}/to/cart
```

| Key             | Value                                                  |
|-----------------|--------------------------------------------------------|
| Accept          | application/vnd.mywebgrocer.message+json               |
| Authorization   | {token}                                                |

|Sample response                                                                 |
|--------------------------------------------------------------------------------|
|{"ChangeOrderId":21327172,"TimeslotId":0,"RemainingTimeSeconds":5397,"Links":[{"Rel":"cart","Placeholders":[],"Queries":null,"Uri":"https://mobileapi.shoprite.com/api/cart/v7/user/cf24c5c5-9e8f-4b22-8c68-d569ab2f5bf1/store/7330772/"}]}|

## Cancel (DELETE) Order
```sh
https://{DEV}.mybluemix.net/api/checkout/v7/order/{orderID}/user/{userID}
```

| Key             | Value                                                  |
|-----------------|--------------------------------------------------------|
| Authorization   | {token}                                                |
