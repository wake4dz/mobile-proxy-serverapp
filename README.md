# # ShopRite Mobile REST Api

ShopRite Mobile's REST api. It interfaces with [My Web Grocer's API](https://apidocs-fg75stg.staging.thefreshgrocer.com/v7#_Data_API) to provide a similar experience as the desktop application.

## Getting Started

We highly recommend using [eclipse](https://www.eclipse.org/downloads/). We've had luck running with version (4.7.3a). If you prefer another environment, please document how to get started using it so others may work in a more comfortable environment if Eclipse doesn't fit their needs.

Along with eclipse: I have the following plugins installed:

- git
- maven(m2e connection)
- [IBM Websphere](https://developer.ibm.com/wasdev/docs/developing-applications-wdt-liberty-profile/). Easiest way is to go to "Help -> Eclipse Market Place" and search for IBM Websphere V8.x. We will look to upgrade to 9 eventually, 8 is what's proven to work.
  - If you have any issues, I recommend downloading directly and installing from a [local file like so](https://www.ibm.com/support/knowledgecenter/en/SSHR6W/com.ibm.websphere.wdt.doc/topics/t_install_wdt_eclipse.html#installingfromdownloadedinstallationfiles).



### First Time WebSphere configuration

If you've never ran a Liberty Server, there's a few steps required to make it readily available.

- Window -> Preferences -> Server -> Runtime Environments -> Add Server and 

  TODO: Add Screen cast

## Configuring Application

You first need to edit the server.env file located here:[TAKE PICTURE]

You need to set the chain:

```sh
chain=ShopRiteProd
```

## Running Application

If it's your first time running the application, you're going to have to install the dependencies. We use [maven](https://maven.apache.org/). Normally eclipse will do this for you, if you have issues you can either run in terminal(in your projects root directory):

```sh
mvn clean install
```

or refer to the [m2e](http://www.eclipse.org/m2e/) documentation for issues in eclipse.

You can attempt to run the application by right clicking on the project in Eclipse -> run as -> Run On Server. If it's your first time, you will have to select the Liberty server that was configured in the previous steps. If this doesn't exist, insure that you've added a target runtime to the shoprite mobile project.

TODO: Add Screen Cast

## Testing Application

After running the application, it's likely useful to ensure your application is operating as we'd expect it to in production. The simplest test is to ensure that application is running:

We can use workflows that the mobile application currently uses for testing, such as authentication. The base url on local is:

http://localhost:9080/shopritemobileapplication/api/ 

There's two ways you can go about testing:

- run mobile application
- postman


## Deploy this application

#### Option 1: Eclipse

Import and deploy to Bluemix and/or a local Liberty server using [Eclipse for Java EE](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr2) with the [Bluemix plugin](https://marketplace.eclipse.org/content/ibm-eclipse-tools-bluemix). In your local Liberty server, be sure to add the jaxrs-2.0 feature to your server.xml
[Video: Deploy to Bluemix using Eclipse](https://www.youtube.com/watch?v=Ro0CSPeoFoY)

#### Option 2: Command Line
```
git clone https://github.com/IBM-Bluemix/java-web-service.git
cd java-web-service
mvn install
cf push <appname> -p target/JavaRESTAPI.war
```

### To add additional mapping to the current app server
####Run this cmd to map & extend resources of the current active app server.
cf map-route srdepmobileapp mybluemix.net --hostname srdepmobile-secondary

Enjoy! 

[Help](https://secure.shoprite.com/User/Register/3601?application=UMA)

## Reports

We are utilizing [spotbug](https://spotbugs.github.io) for static analyis. This
report will be generated each commit into Jenkins so we can track progress over
time.

In order to generate it yourself, the easiest way is to run a the maven site
command and use the run lifecyle. This will generate the site for the project,
and then host it on a Jetty webserver on localhost:8080. 

TODO: GIF THIS
