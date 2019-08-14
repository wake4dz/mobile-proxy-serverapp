# Bluemix's VCAP environment variables

Bluemix's VCAP keeps track of the server's configurations. It directed the outbound traffic to correct MI9's endpoint for service; retains authorization token of the services used in the application, ie. Product Recommendations, Item Locator, Coupon... 

## Authorization Token Variables
- Note: auth.yml contains authorization tokens used in SR application, managed by Mark Covello, Danny Zheng, & Loi Cao. Please seek assistant if new server deployment is performed.

| Env Name   | Description                                                                                                                                                                                                          | Default Value | Possible Values                                                           |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------------------------------------------------------------------|
| chain      | Set the destination for api calls, ShopRiteProd for calling MWG production, ShopRiteStage for calling MWG staging service                           | ShopRiteProd  | [ShopRiteProd,ShopRiteStage,FreshGrocerStage,FreshGrocerProd] |
| cors       | Strictly for development usage, bypassing the CORS requirement in webapp browser run                                                                                                                                                                                                                                                                                                                               | True          | [**True**, False]                                                         |
| url        | Set mwg api endpoint, mobile for calling mobileapi.shoprite.com, web for calling api.shoprite.com (default is mobile)                                                                                                                                                                                                                                                                                              | mobile        | [**mobile**, web]                                                         |                                                       |
| plastic_bag_fee | List of pseudo store ids delimited by comma that require plastic bag fees | BF80788 | [<...store_id>] |
| coupon_v2_key | Contains the Coupon service authorization key for ShopRite | [hidden..] |
| sr_mwg_stage_key | The authorization token key to MI9 (formerly MWG) STAGING environment for ShopRite | [hidden..] |
| sr_mwg_prod_key | The authorization token key to MI9 PRODUCTION environment for ShopRite | [hidden..] |
| tfg_mwg_prod_key | The authorization token key to MI9 staging environment for The Fresh Grocer | [hidden..] |
| jwt_public_key | Contains the Item Locator & Digital Receipt service authorization key for ShopRite | [hidden..] |
| prod_not_found_login | Login credentials to Business Intelligent to log product not found SKU for analysis | [username:password] |
| sr_product_recommendation_key | Contains the Product Recommendation service authorization key for ShopRite | [hidden..] |
| tfg_product_recommendation_key | Contains the Product Recommendation service authorization key for The Fresh Grocer | [hidden..] |

## Services Verification API
- An api was created to accomodate this change, the purpose is to expedite the services verification process post server deployment. It's to test the integrity of the token keys, as well as check for null/empty/missing VCAP environemnt variable key/value.

- API's cURL

```sh
curl -X POST \
  https://[APP_NAME].mybluemix.net/api/wakefern/services/v7/verify \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/wakefern-services' \
  -d '{
	"ppc":"[PPC _NO]",
	"storeId":"[EXTERNAL_STORE_ID]"
}'
```

-Sample api response with missing VCAP names:
```sh
[
  {
    "VCAP names": "coupon_v2_key / sr_mwg_prod_key / jwt_public_key / ",
    "description": "ATTENTION!!! Please check Bluemix VCAP, the listed VCAP name(s) are empty or null."
  },
  {
    "service name": "MI9 AUTHORIZATION",
    "service status": "active"
  },
  {
    "service name": "COUPON V2",
    "service status": "inactive"
  },
  {
    "service name": "ITEM LOCATOR / DIGITAL RECEIPT",
    "service status": "inactive"
  },
  {
    "service name": "BI LOGS PRODUCT NOT FOUND",
    "service status": "active"
  },
  {
    "service name": "PRODUCT RECOMMENDATION",
    "service status": "active"
  }
]
```

-If no missing VCAP, just the services' status is shown

```sh
[
    {
        "service name": "COUPON V2",
        "service status": "active"
    },
    {
        "service name": "ITEM LOCATOR",
        "service status": "active"
    },
    {
        "service name": "BI LOGS PRODUCT NOT FOUND",
        "service status": "active"
    },
    {
        "service name": "PRODUCT RECOMMENDATION",
        "service status": "active"
    }
]
```