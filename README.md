Alpha VMforce SDK Home
======================

This is the home for alpha testers of VMforce. It is highly experimental and currently incomplete. Here's what you can do:

- Get sample VMforce code showing how to set up a Spring Web project with maven repositories and all
- Use the Issues Tab to file issues related to VMforce

Not yet available:

- Proper documentation (coming)
- Full source code for the SDK (coming)

What works?
-----------

- When set up with username and password in a config file, you app can connect to Force.com APIs using WSC. WSC connection objects are autowired into your code
- You can define JPA entities in Java that will be created as Custom Objects in Force.com. Only straight entities with no relationships works well
- You can query data using JPQL
- You can make changes to data using simple atomic transactions. Advanced transaction semantics does not work
- You can make HTTP calls to other services
- You can configure SSO and access control using Spring Security. Users will be sent to Salesforce login page and your code can use Spring Security to check for user authentication and authorization

What doesn't work?
------------------

- OAuth Single Sign-on is a bit tricky to set up for VMforce deployed apps. We will document/fix this soon
- Relationships doesn't really work yet. Coming soon.
- If you make destructive changes to your schema (e.g. remove a field) or if you want to delete/rename an entity, you will have to log into the org and use the UI to delete the custom object. The JPA implementation can only create and update entities at this point.
- Transactions are simplistic. The SDK does not maintain a transaction on the server side, so it only supports what can be accomplished by caching changes in Java until the transaction commits and then sending all changes in a single web service request. This only works for up to 200 changes and only for a single operation type (i.e. insert only or delete only, but not both). This may not be fixed any time soon.
- Many parts of JPQL are not implemented yet. 



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

