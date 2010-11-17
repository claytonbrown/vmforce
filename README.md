# Alpha VMforce SDK Home

This is the home for alpha testers of VMforce. It is still somewhat experimental and certainly incomplete. Here's what this project is currently for:

- Get sample VMforce code showing how to set up various sample VMforce projects based on Spring MVC and other project types.
- Use the Issues Tab to file issues related to VMforce

## What works?

- Build Spring MVC based web apps that use a Force.com JPA provider to connect to Force.com as a database
  - Simple entities and many-to-one relationship navigations work. One-to-many is coming soon.
  - You can define your entities in Java and the JPA provider will create and update the Force.com schema. It will not delete fields or entities if you delete them in Java.
- You can query data using JPQL. Not everything JPQL is supported.
- You can make changes to data using simple atomic transactions. Advanced transaction semantics does not work
- You can make HTTP calls to other services
- You can configure SSO and access control using Spring Security. Users will be sent to Salesforce login page and your code can use Spring Security to check for user authentication and authorization. This requires you to set up OAuth which is still a bit tricky.

## What doesn't work?

- OAuth Single Sign-on is a bit tricky to set up for VMforce deployed apps. We will document/fix this soon
- Relationships doesn't really work yet (you can use @ManyToOne, but not @OneToMany). Coming soon.
- If you make destructive changes to your schema (e.g. remove a field) or if you want to delete/rename an entity, you will have to log into the org and use the UI to delete the custom object. The JPA implementation can only create and update entities at this point.
- Transactions are simplistic. The SDK does not maintain a transaction on the server side, so it only supports what can be accomplished by caching changes in Java until the transaction commits and then sending all changes in a single web service request. This only works for up to 200 changes and only for a single operation type (i.e. insert only or delete only, but not both). This may not be fixed any time soon.
- Parts of JPQL is not implemented yet. 

# Getting Started

This is a quick guide to configuring your first VMforce project, deploy it on your own local tcServer and then deploy it to VMforce.

## Install the latest version of SpringSource Tool Suite

[Get the latest version here](http://www.springsource.com/developer/sts).

## Get a Developer Account

If you're reading this, you should be part of the VMforce alpha program and you should have received login credentials to a VMforce developer account on a special VMforce instance of Force.com. The login endpoint for this instance is [https://vmf01.t.salesforce.com](https://vmf01.t.salesforce.com).

If you haven't received your account, please talk to your contact at Salesforce.com or VMWare to get set up.

## Clone this project

If you don't have the git client and are not using git on a regular basis, you can just download [the whole project as a zip file](https://github.com/forcedotcom/vmforce/zipball/master). Obviously it will be easier to track changes and stay up to date if you use git.

This repository contain various sample projects. During the alpha phase we will constantly tweak these samples and add new ones, so expect a moving target.

## Import the VMforceSpringMVC sample

We use SpringSource Tool Suite which comes with maven integration and other good stuff, so that's what we'll assume here:

1. In STS, choose "File -> Import..."
1. Select Existing Maven project
1. Navigate to the sample you want to import, (we'll assume VMforceSpringMVC). Highlight directory and Finish. Click Ok to start import

The project should import and compile. The compilation may fail until next step is completed.

## Set up the connector.properties file

Go to the directory src/main/resources and copy the example properties file to connector.properties

	$ cd src/main/resources
	$ cp connector.properties.example connector.properties

Edit the connector.properties files and insert your Force.com username and password.

For now, you can ignore the OAuth consumer key and secret. We will shortly document how to use these.

## Build your app

STS should build your app automatically. But it is worthwhile mentioning what happens during a build:

* All Java code is compiled
* The files in the src/main/resources directory are copied to the build target using filtering. Specifically, variables in persistence.xml will be substituted with their values found in connector.properties. Note that connector.properties serve a dual purpose. It is used for this filtering step, but it is also copied to the build target and used as a Spring property file. So all the parameters in connector.properties can be used in you Spring bean files.
* The DataNucleus enhancer maven plugin enhances the JPA entity classes

Everything ends up in the target directory. From here it can be packaged as a war or deployed to a local app server.

Note that if you make changes to your JPA entity classes, you must do a clean build. You run this from the Project -> Clean... STS menu item.

If you have made changes to your project dependencies or otherwise messed with the pom.xml maven file, it's a good idea to do the following: Right-click on your project in the navigation pane and select Maven -> Update Project Configuration.

## Deploy your app locally

Local deployment is an important part of VMforce development. Your app will run on a local tomcat server but it will still use your Cloud based Force.com developer account as its database. By running your app on a local server you can more quickly redeploy code and you can use standard debugging tools such as the Eclipse debugger. Using a cloud database for development means that you'll still have to be online to develop, but the benefit is that the database is managed for you and you won't have to deal with any incompatibility problems when you migrate from development to production.

Here are the steps to configure a local tcServer and deploy your app:

1. There should be a "Servers" pane in the lower left part of STS. Right-click in this pane and select New -> Server
1. Select SpringSource tcServer 2.0 and click next, then click finish on the following dialog
1. Right-click on your new server in the Servers pane and select "Publish"
1. Now double-click on the server to open the configuration page
1. On the configuration page, in the Server Locations section, select "Use workspace metadata". This prevents your central tc Server configuration from getting hosed by individual projects
1. Save the configuration page and close it.
1. Select the VMforceSpringMVC app in the navigator, right-click and select Run As -> Run on Server
1. Select your newly created server and click finish.

tcServer should now start up and deploy your app. It will even open the front page of your web app in the STS builtin browser or your system's default browser. Your app should be running on [http://localhost:8080/VMforceSpringMVC](http://localhost:8080/VMforceSpringMVC).

## Deploy your app to VMforce

When you have tested your app locally, the time has come to deploy it to the cloud, so you can perform user testing and final production rollout. You can deploy your app to VMforce using either the VMforce plugin for SpringSource Tool Suite or the vmc command-line interface.

### Install the VMforce STS plugin

The VMforce plugin for STS allows you to deploy and manage your application in the cloud just as if it was a local server. Here are the steps to installing the plugin into STS:

1. Select Help -> Install New Software...
1. Select Add
1. In the dialog, type "VMforce" as name and "http://dist.springsource.com/snapshot/TOOLS/nightly/vmforce" as location and click ok.
1. Select "Core / VMforce Integration" in the list
1. Click next, accept terms and click finish.
1. Restart STS

### Add a VMforce Cloud Server

1. In the Servers pane, right-click and select New -> Server
1. In the filter text box, type "VMforce" and select the VMforce Cloud Server from the list
1. Leave the hostname as localhost and click next.
1. Type in your Force.com Developer Account credentials and set the URL to: https://api.alpha.vmforce.com
1. Use the "Check Credentials" button to tests that it's configured correctly and click Finish.

### Deploy your application to VMforce

You're now ready to deploy your application:

1. Right-click your application and select Run As -> Server just like before
1. Select the VMforce Cloud as Server
1. Click Finish.
1. On the launch application dialog, change the name of your app to include something unique, so it doesn't clash with other names. All apps are currently deployed in a single namespace.
1. You're done. STS will open a browser on your app's front page. If you see a 404, try refresh a few seconds later.

### Using the command-line tool

VMforce also comes with a [command line tool called vmc](http://rubygems.org/gems/vmc). It's a ruby program so you'll need Ruby on your machine. Install by running

	$ sudo gem install vmc

You can get help with

	$ vmc help
	
For example, to list your deployed apps:

	$ vmc login
	Email: jespertest2@vmforce.com
	Password: ********
	successfully logged in

	$ vmc apps
	No applications available.

To deploy an app like VMforceSpringMVC:

	$ cd home/of/VMforceSpringMVC
	$ mvn package
	$ cd target
	$ vmc push

