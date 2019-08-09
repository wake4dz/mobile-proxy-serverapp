# Bluemix's VCAP environment variables

Bluemix's VCAP keeps track of the server's configurations. It directed the outbound traffic to correct MI9's endpoint for service; retains authorization token of the services used in the application, ie. Product Recommendations, Item Locator, Coupon... 

## Authorization Token Variables
- Note: auth.yml contains authorization tokens used in SR application, managed by Mark Covello, Danny Zheng, Loi Cao. Please seek assistant if new server deployment is performed.

| Env Name   | Description                                                                                                                                                                                                          | Default Value | Possible Values                                                           |
|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------------------------------------------------------------------|
| chain      | Set the destination for api calls, ShopRiteProd for calling MWG production, ShopRiteStage for calling MWG staging service                           | ShopRiteProd  | [ShopRiteProd,ShopRiteStage,FreshGrocerStage,FreshGrocerProd] |
| cors       | Strictly for development usage, bypassing the CORS requirement in webapp browser run                                                                                                                                                                                                                                                                                                                               | True          | [**True**, False]                                                         |
| url        | Set mwg api endpoint, mobile for calling mobileapi.shoprite.com, web for calling api.shoprite.com (default is mobile)                                                                                                                                                                                                                                                                                              | mobile        | [**mobile**, web]                                                         |                                                       |
| plastic_bag_fee | List of pseudo store ids delimited by comma that require plastic bag fees | BF80788 | [<...store_id>] |
| coupon_v2_token | Contains the Coupon service authorization token for ShopRite | [hidden..] |
| sr_mwg_stage_token | The authorization token key to MI9 (formerly MWG) STAGING environment for ShopRite | [hidden..] |
| sr_mwg_prod_token | The authorization token key to MI9 PRODUCTION environment for ShopRite | [hidden..] |
| tfg_mwg_prod_token | The authorization token key to MI9 staging environment for The Fresh Grocer | [hidden..] |
| item_locator_token | Contains the Item Locator service authorization token for ShopRite | [hidden..] |
| prod_not_found_login | Login credentials to Business Intelligent to log product not found SKU for analysis | [username:password] |
| sr_product_recommendation_token | Contains the Product Recommendation service authorization token for ShopRite | [hidden..] |
| tfg_product_recommendation_token | Contains the Product Recommendation service authorization token for The Fresh Grocer | [hidden..] |
