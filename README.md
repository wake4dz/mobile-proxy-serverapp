# ShopRite Mobile Java Server

Wakefern Mobile App Java server acts as a proxy to MyWebGrocer's REST services on behalf of our ShopRite and FreshGrocer mobile applications. 

The following internal REST services are also exposed: 
- item locator
- product recommendation
- coupon 

## Runtime Requirements

The current runtime environment is:
- Java 8
- [ibm websphere liberty](https://github.com/wakefern/ibm-websphere-liberty-buildpack) for application server
- [ibm bluemix](https://www.ibm.com/cloud-computing/bluemix/node/4471) for application hosting

## Bluemix's Environment Variable
| Env Name   | Description                                                                                                                                                                                                                                                                                                                                                                                                        | Default Value | Possible Values                                                           |
|------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------------------------------------------------------------------|
| chain      | Set the destination for api calls, ShopRiteProd for calling MWG production, ShopRiteStage for calling MWG staging service                                                                                                                                                                                                                                                                                          | ShopRiteProd  | [ShopRiteProd,ShopRiteStage,FreshGrocerStage,FreshGrocerProd] |
| cors       | Strictly for development usage, bypassing the CORS requirement in webapp browser run                                                                                                                                                                                                                                                                                                                               | True          | [**True**, False]                                                         |
| url        | Set mwg api endpoint, mobile for calling mobileapi.shoprite.com, web for calling api.shoprite.com (default is mobile)                                                                                                                                                                                                                                                                                              | mobile        | [**mobile**, web]                                                         |                                                       |
| plastic_bag_fee | List of pseudo store ids delimited by comma that require plastic bag fees | BF80788 | [<...store_id>] |

## Deploy this application

### Production Release

The [Blue-Green](https://docs.cloudfoundry.org/devguide/deploy-apps/blue-green.html) deployment steps are as follows:

1. Once the team is ready for a new deployment, create a PCR request and a supervisor/manager approves the PCR.
1. Build a shopritemobileapplication.war file from the latest "develop" git branch 
1. Set CF Space to `ShopRite Mobile App - Prod`
1. Deploy the war to the secondary Prod servers
1. Map routes so traffic goes to primary and secondary servers with the route-in and the route-out approach
1. Run and test the latest local UI app against the secondary Prod servers (some end-to-end tests: sign out, login, put some items in the cart, checkout, etc)
1. Once the tests on the secondary servers are passed, then deploy the war to the primary Prod servers while the secondary Prod servers are running
1. Run and test the latest local UI app against the primary Prod servers ONLY (some end-to-end tests: sign out, login, put some items in the cart, checkout, etc)
1. Once the tests on the primary Prod servers are passed, then shut down the secondary servers.
1. Deploy with the Blue-Green way to minimize the servers down-time, and keep the primary and secondary Prod servers in the same war build release level

:warning: if there are no changes to the manifest file, use the "--no-manifest" instead of the "-vars-file" flag :warning:

:warning: The application name here is not specifically for production. When making a production release, make sure you validate that you're deploying to the right app name to avoid down-time :warning:

```

git clone https://github.com/wakefern/srmobile-serverapp.git
cd srmobile-serverapp
mvn install
cf push srmobile-serverapp --vars-file=./config/prod.yml -p target/shopritemobileapplication.war
```



### Development Release

1. Set CF Space to `ShopRite Mobile App - Dev`

Follow steps from production release. When pushing the war file to bluemix, update your `--vars-file` to use the ```config/dev.yml``` file:

```sh
cf push srmobile-serverapp --vars-file=./config/dev.yml -p target/shopritemobileapplication.war
```

### Future Enhancements

- [ ] update manifest.yml to have the correct app name
- [ ] add build date as environmental variable
- [ ] add deployment date as environmental variable
- [ ] add script to generate build and release dates
- [ ] consolidate release steps into a single executable
- [ ] [rolling deployment](https://docs.cloudfoundry.org/devguide/deploy-apps/rolling-deploy.html)

### To add additional mapping to the current app server

> Run this cmd to map & extend resources of the current active app server.

cf map-route srdepmobileapp mybluemix.net --hostname srdepmobile-secondary

Enjoy!
