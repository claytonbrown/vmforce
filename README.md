Force.com SDK for Java
======================

This project is highly experimental at this point and not for the faint of heart. The project currently only contains sample Force.com Java applications that uses a early build of the SDK published to a temporary maven repository. Going forward, this project will be the home for the SDK code itself.

If you decide to try out the samples or build something yourself, you can use the issue tracker to let us know about problems.

What can you do with the SDK?
-----------------------------

The SDK builds upon the Force.com Web Service Connector toolkit that has been available for some time. You can use this SDK to build Spring Web applications that uses Force.com as a data store and user management system.

At this point, all code in the SDK is experimental and incomplete. It is published here for the purpose of testing how the project should be hosted and managed going forward and to give access to those brave souls who want to experiment at this early stage.


Getting Started
---------------

### Step 1: Get a Force.com Developer Edition org

(if you don't already have one).

Go to http://developer.force.com and sign up for an org. You should receive your username and password promptly by email. Once you have your credentials, log into your new org (that's what we call your workspace on Force.com, it's short for organization) and get a security token. Here's a convenient link to the security token page:

https://login.salesforce.com/secur/frontdoor.jsp?retUrl=_ui/system/security/ResetApiTokenEdit

By resetting the token, you will receive an email with a working token. You will need to append this token to you password in your application configuration.

### Step 2: Clone this project

If you're not a git user, you can also just download the code using the big download button on this page in the upper right corner

### Step 3: Import a sample into your favorite IDE

We use SpringSource Tool Suite which comes with maven integration and other good stuff, so that's what we'll assume here:

1) Open the IDE and choose "File -> Import..."
2) Select Existing Maven project
3) Navigate to the sample you want to import, e.g. SpringMVCWithSecurity. Highlight directory and Finish. Click Ok to start import

The project should import and compile with no errors. Now to the final step.

### Step 4: Configure Force.com connector information

Each sample project should have a file src/main/resources/dev/connector.properties.example. Copy this file to connector.properties and insert your username and password in the properties in this file. Remember to append the security token to your password.

If the sample app uses OAuth (the SpringMVCWithSecurity does), you will also need to configure an OAuth consumer in your org and add the consumer key and secret to the properties file. See the README for the SpringMVCSecurity sample for how to do this.

### Step 5: Deploy your app

All the samples should work out of the box as long as the connector.properties are properly configured and unless otherwise noted on the samples README page. That means you can deploy your app to a local web app server such as tomcat or tcServer and it should all just work.
 
