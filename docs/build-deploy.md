# Build + Deployment

srmobile-serverapp is the REST Api that srmobile-webapp depends upon for communicating with [MyWebGrocer](https://apidocs.shoprite.com/v7). We're currently using [Maven](https://maven.apachge.org) for dependency management and building. Our target runtime environment is [Websphere](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/cwlp_about.html), running on [ibm cloud](https://www.ibm.com/cloud/). 

## Base Requirements

- [maven](https://maven.apache.org)
- [java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [patience](https://twitter.com/iamdevloper?lang=en)

## Building

The goal is to generate a [war](https://en.wikipedia.org/wiki/WAR_(file_format)). This war can then be used to run on any local runtime environment, though websphere is recommended. In order to build the war, you use the maven install [goal](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html).

```sh
mvn install;
```

- [ ] @todo create GIF

## Deployment

The long term goal is to eliminate the need for manual deployments. The short term goal is to share *how* to deploy manually. There are numerous deployment pipelines available to get our generated war to ibm cloud, the easiest is through [cloud foundry](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html). While I've had limited success with ibm's [cli](https://console.bluemix.net/docs/cli/index.html) tool, it extends cloud foundry's and hasn't added any "must have" features. This could change, and I encourage you to try both. 

### Requirements

- [Cloud Foundry CLI](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html)
- [IBM Cloud CLI](https://console.bluemix.net/docs/cli/index.html)
- [ibm cloud account](https://console.bluemix.net/registration/)
- [developer access](https://console.bluemix.net/docs/iam/users_roles.html#userroles)

I recommend starting with cloud foundry. If you are unsure if you have access as a developer, then reach out to @mcovello or @johnpearson

### Deployment Steps

- [ ] generate war file
- [ ] cf push
- [ ] celebrate!

Cloud Foundry has a [push](https://docs.cloudfoundry.org/devguide/deploy-apps/deploy-app.html) command. This is how we deploy manually today, and how we will deploy with continuous integration.

For a development build, you can deploy like so:

```sh
cf push srmobile-serverapp -p target/shopritemobileapplication.war
```

