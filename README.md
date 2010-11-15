# Alpha VMforce SDK Home

This is the home for alpha testers of VMforce. It is highly experimental and currently incomplete. Here's what you can do:

- Get sample VMforce code showing how to set up a Spring Web project with maven repositories and all
- Use the Issues Tab to file issues related to VMforce

## What works?

- When set up with username and password in a config file, you app can connect to Force.com APIs using WSC. WSC connection objects are auto-wired into your code
- You can define JPA entities in Java that will be created as Custom Objects in Force.com. Only straight entities with no relationships works well
- You can query data using JPQL
- You can make changes to data using simple atomic transactions. Advanced transaction semantics does not work
- You can make HTTP calls to other services
- You can configure SSO and access control using Spring Security. Users will be sent to Salesforce login page and your code can use Spring Security to check for user authentication and authorization

## What doesn't work?

- OAuth Single Sign-on is a bit tricky to set up for VMforce deployed apps. We will document/fix this soon
- Relationships doesn't really work yet (you can use @ManyToOne, but not @OneToMany). Coming soon.
- If you make destructive changes to your schema (e.g. remove a field) or if you want to delete/rename an entity, you will have to log into the org and use the UI to delete the custom object. The JPA implementation can only create and update entities at this point.
- Transactions are simplistic. The SDK does not maintain a transaction on the server side, so it only supports what can be accomplished by caching changes in Java until the transaction commits and then sending all changes in a single web service request. This only works for up to 200 changes and only for a single operation type (i.e. insert only or delete only, but not both). This may not be fixed any time soon.
- Parts of JPQL is not implemented yet. 


## Getting Started with a sample running on localhost

### Install the latest version of SpringSource Tool Suite

SpringSource Tool Suite is the primary IDE for VMforce. This is the only IDE we test with right now. [Get the latest version here](http://www.springsource.com/developer/sts).

### Get a Force.com Developer Account

(if you don't already have one).

Developer accounts are also called developer organizations because you're account lives inside its own organization. You can think of an organization as a combination of a database and a domain of users. Normally all employees in a company use a single organization for CRM, service & support, collaboration, 3rd party installed and custom-built applications. In development mode, you as a developer has the whole organization to yourself.

Your Force.com Developer Organization is the database for your VMforce application and it is also responsible for user management, authentication and application configuration.

Technically, it is possible to use the SDK with any Force.com organization hosted on the [production pods](https://login.salesforce.com), the sandbox pods (accessed through https://test.salesforce.com) or the experimental VMF instances, which are small Force.com instances used specifically for VMforce. You can build Java applications with the SDK without ever deploying them to VMforce.

To deploy your app on VMforce, you will need to have an account (organization) on the special VMF instance. Please contact jjoergensen@salesforce.com to get an org on this instance. But note that it is not necessary to get started.

### Clone this project

If you don't have the git client and are not using git on a regular basis, you can just download [the whole project as a zip file](https://github.com/forcedotcom/vmforce/zipball/master). Obviously it will be easier to track changes and stay up to date if you use git.

This repository contain various sample projects. During the alpha phase we will constantly tweak these samples and add new ones, so expect a moving target.

### Import the VMforceSpringMVC sample

We use SpringSource Tool Suite which comes with maven integration and other good stuff, so that's what we'll assume here:

1. In STS, choose "File -> Import..."
1. Select Existing Maven project
1. Navigate to the sample you want to import, (we'll assume SpringMVCWithSecurity). Highlight directory and Finish. Click Ok to start import

The project should import and compile. The compilation may fail until next step is completed.

### Set up the connector.properties file

Go to the directory src/main/resources and copy the example properties file to connector.properties

	$ cd src/main/resources
	$ cp connector.properties.example connector.properties

Edit the connector.properties files and insert your Force.com username and password.

If you are NOT using an account on the special VMF instance, you will need to append your security token to the password. [This page](https://help.salesforce.com/apex/HTViewHelpDoc?id=user_security_token.htm&language=en) explains how to get your security token sent to you by email.

If you ARE using a VMF instance, you currently shouldn't need a security token, but this may change in the near future.

For now, you can ignore the OAuth consumer key and secret. We will shortly document how to use these.

<!--
### Step 5 (Optional): Register your application in your org and configure application keys

This step is currently only needed if the sample uses OAuth for single sign-on. This step is currently only supported for local deployments.

1. Log into your org
2. Click on your name in upper-right corner and select Setup
3. Go to App Setup -> Develop -> Remote Access and Click New to create a new application entry
4. Type the name of the sample app in the Application Field prefixed with "local:", e.g. "local:SpringMVCWithSecurity". This name is not used, but this keeps it unique for future purposes
5. Assuming you will run your app on http://localhost:8080, set the callback URL to http://localhost:8080/<appname>/\_auth for example: http://localhost:8080/SpringMVCWithSecurity/\_auth.
6. Select "No user approval required".
7. Click Save. Rest of fields can be left empty
8. Copy and paste the application key and secret from the result page to the corresponding variable in the connector.properties file.
-->

### Build your app

STS should build your app automatically. But it is worthwhile mentioning what happens during a build:

* All Java code is compiled
* The files in the src/main/resources directory are copied to the build target using filtering. Specifically, variables in persistence.xml will be substituted with their values found in connector.properties. Note that connector.properties serve a dual purpose. It is used for this filtering step, but it is also copied to the build target and used as a Spring property file. So all the parameters in connector.properties can be used in you Spring bean files.
* The DataNucleus enhancer maven plugin enhances the JPA entity classes

Everything ends up in the target directory. From here it can be packaged as a war or deployed to a local app server.

Note that if you make changes to your JPA entity classes, you must do a clean build. You run this from the Project -> Clean... STS menu item.

If you have made changes to your project dependencies or otherwise messed with the pom.xml maven file, it's a good idea to do the following: Right-click on your project in the navigation pane and select Maven -> Update Project Configuration.

### Deploy your app locally

Local deployment is an important part of VMforce development. Your app will run on a local tomcat server but it will still use your Force.com developer account hosted in the cloud as its database. The benefit is that you can more quickly redeploy code and you can use standard debugging tools such as the Eclipse debugger. The limitation is you cannot develop while offline. We do not offer a locally hosted version of the Force.com database and probably never will.

Here are the steps to configure the tc Server and deploy your app:

1. There should be a "Servers" pane in the lower left part of STS. Right-click in this pane and select New -> Server
1. Select SpringSource tc Server 2.0 and click next, then click finish on the following dialog
1. Right-click on your new server in the Servers pane and select "Publish"
1. Now double-click on the server to open the configuration page
1. On the configuration page, in the Server Locations section, select "Use workspace metadata". This prevents your central tc Server configuration from getting hosed by individual projects
1. Save the configuration page and close it.
1. Select the VMforceSpringMVC app in the navigator, right-click and select Run As -> Run on Server
1. Select your newly created server and click finish.

tc Server should now start up and deploy your app. If you're lucky it will even open the front page of your web app in the STS builtin browser or your system's default browser. Your app should be running on [http://localhost:8080/VMforceSpringMVC](http://localhost:8080/VMforceSpringMVC).

### Summary

From here you can go explore all the possible apps you can build with the VMforce SDK.

## Deploy your app to VMforce

Remember, you can only do this if you have an account on the special VMF instance.

### Install the VMforce STS plugin

The SpringSource team has built a VMforce plugin for STS that allows you to deploy and manage your application in the cloud just as if it was a local server. Here are the steps to installing the plugin into STS:

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


