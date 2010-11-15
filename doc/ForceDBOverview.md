# Overview of the Force.com Database

The Force.com database is a database service built from scratch to specifically target business applications. This makes it different from a normal relational database in a number of ways and it is important to understand those differences when you design applications for the database. 

The Force.com JPA provider allows you to use the JPA standard to access Force.com. It abstracts away the specific APIs, data model language and query language of Force.com and lets you implement your persistence layer in standard Java code. But as with all abstractions, you are better off if you have at least some understanding of the underlying system. This section gives a brief overview of the database, how it works and how it's different from normal databases.

## Schema and data modeling

The Force.com database has many of the same data types as other relational databases but the types and their names are shaped by business needs. For example, there is a phone number type and a special compound address type. In most cases you don't have to worry about this. Your Java types will be translated automatically. 

Relationships are supported. There are two types called lookup and master-detail. Lookup is the standard relationship and master-detail implies cascade delete. The special thing about relationships is that you can only join between entities that have explicit relationships. So your data model will affect what you can query. 

[This table][1] shows a complete list of how Java types are mapped to Force.com types and [this table][2] shows the reverse mappings. 

[1]: (http://tbd)
[2]: (http://tbd)

In Force.com terminology, you will hear database entities (tables) referred to as sObjects. An sObject is the same as a table or an entity. Fields are called fields and data entries are called records. 

DDL or schema definition is often referred to as just Metadata and the database has a complete Metadata API that allows you to create, update and delete database entities from any client. The Force.com JPA provider uses this API to automatically create and update the schema based on your application's JPA entity model.

## Database API and transactions

Clients (such as VMforce Java applications) access the Force.com database exclusively through a set of HTTP based APIs. The database can also be accessed using Apex, which is an embedded language runtime that runs inside Force.com application servers. The database does not offer any session-oriented wire protocol commonly offered by relational databases. Currently the following wire APIs are available:

* SOAP API - The most feature complete API. Includes full WSDL descriptions of the service and data model
* REST API - Currently in pilot and has a more limited feature set compared to SOAP API. It supports XML and JSON formats and general behave as you would expect from a REST API
* Bulk API - A REST API specifically intended for high-volume data operations.

All of these APIs demarcate transactions at the request boundary. A transaction can never span a single HTTP request. This means that the database cannot provide the full range of transaction semantics to remote clients. Full transaction semantics *are* supported in Apex however. So, if you need to implement advanced transactional logic, you can write it in Apex and call it as a stored procedure from your Java code.

In many cases that should not be necessary because you can batch up multiple records of different types in a single API call and have the database commit or rollback the whole set atomically. This lets you support the most common use cases, such as creating a parent record and a set of child records in a single transaction.

## Query

The Force.com database has its own query language called SOQL (Salesforce Object Query Language). It is similar to SQL with some additional syntax that makes it more object oriented. SOQL doesn't allow arbitrary joins. You can only join entities.

The Force.com JPA Provider lets you query in JPQL, the query language of JPA and you should not need to learn SOQL except if JPQL does not meet your needs. JPA has a "native query" feature that is meant for this purpose. For relational databases, the native query feature lets you write SQL that will be sent unmodified to the database. The Force.com JPA provider does the same, but it expects you to write SOQL instead of SQL.

## Primary Keys

Every entity in the Force.com database has a primary key and the primary key is always a string and is always auto-assigned. Primary keys look something like this: a0x30000000H7js. They come in two forms, a 15-character case-sensitive form and an 18-character case-insensitive form. The latter is always returned by API calls, but the former is also accepted in API requests.

As long as you go through the JPA provider or any other API, there should be no confusion. You will always be using the 18-character version.

The JPA standard allows you to define a variety of primary key types. These are not supported by the Force.com JPA provider. The only support primary key declaration is the following:

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	String id;
	
The `id` field name is reserved and cannot be used by non-primary-key fields.

## Standard Objects

The Force.com database has not been completely separated from its CRM legacy. You may see reference to both "Standard Objects" and "Custom Objects" in various places. Standard Objects are the built-in CRM entities and even if you can't see them in your particular database, they are still there and their names are reserved.

You will need to know about these reserved names when you define your JPA entities. If you use a reserved name, the JPA provider will assume you are modeling an existing entity instead of a new one which may not be what you intend. You can find a list of standard objects and their API names in the [Web Services Developer Guide][3].

[3]: (http://www.salesforce.com/us/developer/docs/api/index.htm)

TODO: Add list of reserved names here or show how to get it from the API

## Standard, reserved fields

Each entity in the Force.com database has a set of pre-defined fields, including the id mentioned above. These fields are sometimes referred to as audit fields. The following is a complete list of audit fields:

* Id
* IsDeleted
* Name
* CreatedDate
* CreatedById
* LastModifiedDate
* LastModifiedById
* SystemModstamp

You can find a more detailed description on [this page][4] in the Web Services Developer Doc

[4]: (http://www.salesforce.com/us/developer/docs/api/Content/system_fields.htm)

In addition, all the standard objects come with a complete set of standard fields. For example, Account is a standard object and it has a standard field called AnnualRevenue. The [Web Services Developer Guide][3] also contains a complete list of these standard fields for each of the standard objects.
