Alpha VMforce SDK Home
======================

This project is highly experimental at this point and not for the faint of heart. The project currently only contains sample Force.com Java applications that uses a early build of the SDK published to a temporary maven repository. Going forward, this project will be the home for the SDK code itself.

If you decide to try out the samples or build something yourself, you can use the issue tracker to let us know about problems.

What can you do with the SDK?
-----------------------------

The SDK builds upon the Force.com Web Service Connector toolkit that has been available for some time. You can use this SDK to build Spring Web applications that uses Force.com as a data store and user management system.

At this point, all code in the SDK is experimental and incomplete. It is published here for the purpose of testing how the project should be hosted and managed going forward and to give access to those brave souls who want to experiment at this early stage.


Getting Started
---------------

### Step 1: Get an org

(if you don't already have one).

Technically, it is possible to use the SDK with any Force.com organization hosted on the production pods (accessed through https://login.salesforce.com), the sandbox pods (accessed through https://test.salesforce.com) or the experimental VMF instances, which are small Force.com instances used specifically for VMforce.

You can start building apps right away and point it to one of your existing orgs on sandbox or production as long as you deploy your app on a local tomcat server. If you deploy your app to the VMforce cloud servers, you will need to use an org on the VMF instance. Please contact jjoergensen@salesforce.com to get an org on this instance. But note that it is not necessary to get started.

### Step 2: Clone this project

If you're not a git user, you can just download the code using the big download button on this page in the upper right corner

### Step 3: Import a sample into your favorite IDE

We use SpringSource Tool Suite which comes with maven integration and other good stuff, so that's what we'll assume here:

1) Open the IDE and choose "File -> Import..."
2) Select Existing Maven project
3) Navigate to the sample you want to import, e.g. SpringMVCWithSecurity. Highlight directory and Finish. Click Ok to start import

The project should import and compile with no errors.

### Step 4: Configure Force.com connector information

Each sample project should have a file src/main/resources/dev/connector.properties.example. Copy this file to connector.properties and insert your username and password in the properties in this file. You may need to append your security token to this password.

### Step 5 (Optional): Register your application in your org and configure application keys

This step is currently only needed if the sample uses OAuth for single sign-on. This step is currently only supported for local deployments.

1) Log into your org
2) Click on your name in upper-right corner and select Setup
3) Go to App Setup -> Develop -> Remote Access and Click New to create a new application entry
4) Type the name of the sample app in the Application Field prefixed with "local:", e.g. "local:SpringMVCWithSecurity". This name is not used, but this keeps it unique for future purposes
5) Assuming you will run your app on http://localhost:8080, set the callback URL to http://localhost:8080/<appname>/_auth for example: http://localhost:8080/SpringMVCWithSecurity/_auth.
6) Select "No user approval required".
7) Click Save. Rest of fields can be left empty
8) Copy and paste the application key and secret from the result page to the corresponding variable in the connector.properties file.


### Step 7: Deploy your app

All the samples should work out of the box as long as the connector.properties are properly configured and unless otherwise noted on the samples README page. That means you can deploy your app to a local web app server such as tomcat or tcServer and it should all just work.

