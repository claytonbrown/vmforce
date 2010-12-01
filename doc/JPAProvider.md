# Force.com Database JPA Provider

The Force.com JPA provider allows you to write Java applications that use JPA to persist data in the Force.com Database. This lets you write apps with a persistence layer that is intuitive and easy to understand for Java developers and it makes it much simpler to migrate your applications between different data stores.

That being said, it's important to understand that the Force.com database is different in several aspects from standard relational databases and the JPA provider reflects this difference. [This overview](ForceDBOverview.md) explains the basics of the Force.com database and how it's different. Make sure you understand these differences before you design your VMforce application. You should expect deviations from the JPA spec in the following areas:

* JPQL: Some query constructs are not supported. You will receive a proper error message if you try to use unsupported constructs. Most notably, you can't perform arbitrary joins between unrelated entities.
* Transactions: The Force.com database is a web service and transactions are demarcated at the request boundaries. A single transaction cannot span multiple requests and therefore the full set of transaction semantics you would expect from a regular database is not available. Consult this and future, more complete documentation for more details.
* Data types: Force.com supports most data types found in JPA, but there are some differences. Consult this and future, more complete docs for details.

VMforce uses the [DataNucleus Access Platform v2.1](http://www.datanucleus.org/products/accessplatform.html) implementation of the JPA specification. There are many aspects of the Force.com JPA provider that are directly provided by the DataNucleus framework and you may need to consult the DataNucleus documentation in some cases.

## Configuring the JPA Provider in your Spring Apps

To enable the Force.com JPA provider in a standard Spring MVC application you need to include dependencies to the following libraries:

* DataNucleus Access Platform
* Force.com SDK
* Additional Spring libraries for container transactions if you don't already have them

You can add these libraries to a standard Spring MVC 3.0.4 project by adding the following dependencies to the `pom.xml` file:

	<dependency>
		<groupId>com.salesforce.sdk.persistence</groupId>
		<artifactId>datanucleus-sfdc</artifactId>
		<version>20.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>org.datanucleus</groupId>
		<artifactId>datanucleus-core</artifactId>
		<version>2.1.0-release</version>
		<scope>runtime</scope>
	</dependency>
	<dependency>
		<groupId>org.datanucleus</groupId>
		<artifactId>datanucleus-enhancer</artifactId>
		<version>2.1.0-release</version>
		<scope>runtime</scope>
	</dependency>
	<dependency>
		<groupId>org.datanucleus</groupId>
		<artifactId>datanucleus-jpa</artifactId>
		<version>2.1.0-release</version>
		<scope>runtime</scope>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-tx</artifactId>
		<version>3.0.4.RELEASE</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-orm</artifactId>
		<version>3.0.4.RELEASE</version>
		<scope>runtime</scope>
	</dependency>
	<dependency>
		<groupId>cglib</groupId>
		<artifactId>cglib</artifactId>
		<version>2.2</version>
	</dependency>

You'll also need to add the VMforce snapshot repository location to the repositories section

	<repository>
		<id>com.force.maven.snapshot</id>
		<name>Force.com Maven Snapshot Repo</name>
		<url>http://repo.t.salesforce.com/archiva/repository/snapshots</url>
	</repository>

See the [`pom.xml` file in the VMforceSpringMVC](https://github.com/forcedotcom/vmforce/blob/master/samples/VMforceSpringMVC/pom.xml) sample app for a complete example.

DataNucleus adds code to persistent classes using class enhancement. You can set up Maven to do this enhancement as a post-compilation step by adding the DataNucleus enhancer plugin to the `pom.xml` file's `pluginRepositories` section:

	<pluginRepositories>
		<pluginRepository>
			<id>DataNucleus_2</id>
			<url>http://www.datanucleus.org/downloads/maven2/</url>
		</pluginRepository>
	</pluginRepositories>

The VMforceSpringMVC sample demonstrates all this configuration and you can use diff to see exactly what was done to the basic Spring MVC app that STS generates by default:

	$ cd [your_local_vmforce_repo]/samples/VMforceSpringMVC
	$ git diff -w 191e7bfd3dbb4b4a56afa4dc13713a4fa269de0c..HEAD .

This doc may not cover every detail in the sample app so if you're building your own project and run into issues, this diff can help show how the sample app was built.

### JPA persistence configuration

VMforce uses the standard JPA `persistence.xml` file for its configuration file. In the compiled and packaged application this file must reside in the root of the classpath, for example in the root of the `WEB-INF/classes` directory. In the VMforce samples shown here, the file resides in `src/main/resources/META-INF` and maven is configured to copy this file to its target destination as part of the build process.

This configuration file includes one or more persistence-unit elements. A persistence unit defines a set of classes and how to persist them. Each persistence unit has a unique name. In the following example, there is one unit named 
`forceDatabase`.

	<?xml version="1.0" encoding="UTF-8"?>
	<persistence xmlns="http://java.sun.com/xml/ns/persistence" 
	             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence 
  				 	http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd" 
	             version="1.0">
	    <persistence-unit name="forceDatabase">
	      <provider>com.salesforce.persistence.datanucleus.PersistenceProviderImpl</provider>
	      <properties>
	         <property name="datanucleus.ConnectionURL" 
	                   value="sfdc:${force.endPoint}/services/Soap/u/${force.apiVersion}"/>
	         <property name="datanucleus.ConnectionUserName" value="${force.userName}" />
	         <property name="datanucleus.ConnectionPassword" value="${force.password}" />
	         <property name="datanucleus.autoCreateSchema" value="true"/>
	         <property name="datanucleus.validateTables" value="false"/>
	         <property name="datanucleus.validateConstraints" value="false"/>
	         <property name="datanucleus.Optimistic" value="true"/>
	         <property name="datanucleus.datastoreTransactionDelayOperations" value="true"/>
	         <property name="datanucleus.jpa.addClassTransformer" value="false"/>
	         <property name="datanucleus.cache.level2.type" value="none"/>
	         <property name="sfdc.AllOrNothing" value="true"/>
	         <property name="sfdcConnectionName" value="JPAConfiguredConnection"/>
	      </properties>
	   </persistence-unit>
	</persistence>

This example is from the [VMforceSpringMVC sample app](https://github.com/forcedotcom/vmforce/tree/master/samples/VMforceSpringMVC/). The sample project defines a set of property placeholders, `force.userName`, `force.password`, etc. in this file. These properties are substituted with values during the build from the connector.properties file in the same directory. You can choose to configure your build differently and for example type the connector properties directly into the persistence.xml file. But it is recommended you keep sensitive information like passwords in separate files that you can exclude from version control. 

Besides the connector endpoint, version and credentials, you should only care about the following configurations in this file:

#### datanucleus.autoCreateSchema

Set to false if you don't want your schema created or updated automatically by DataNucleus each time you restart the server or run tests

#### datanucleus.Optimistic

Set this property to true for optimistic transactions. Optimistic concurrency control is a method that assumes that multiple transactions can complete without affecting each other, and that transactions can proceed without locking the data resources that they affect. Before committing, each transaction verifies that no other transaction has modified its data. If the check reveals conflicting modifications, the committing transaction rolls back. When this property is set to true, each Java class that models an entity used in a transaction should include a field with the following signature: 

	@Version 
	Calendar lastModifiedDate;

The `lastModifiedDate` field is a system field that is automatically created for every entity in the Force.com database. The @Version annotation enables JPA to use this date field to do `ifModifiedBefore()` checks on update and delete operations. If this check indicates that another operation has updated a record in the transaction, a `javax.persistence.OptimisticLockException` is thrown as the record in the transaction has stale data. If the `sfdc.AllOrNothing` property is enabled, the transaction is rolled back.
	
#### datanucleus.datastoreTransactionDelayOperations

Set this property to true to ensure that all JPA operations are buffered until either commit() is called on a transaction or flush() is called on an EntityManager. If the property is set to false, every operation handled by the EntityManager is independently committed to the database.

#### sfdc.AllOrNothing

Set this property to true to ensure that all changes are rolled back if any errors occur when persisting records. If the property is set to false, changes to records with no errors are committed even if there are errors persisting other records in the transaction. If a transaction includes an insert, update, and a delete operation, this property applies to each operation separately. For example, if the insert and delete operations have no errors, but the update operation has at least one error, the insert and operations are committed, while the update operation doesn't change any records due to the error.

### Setting up Spring components

The Spring framework will handle instantiation of the EntityManager and transactions for you and you'll need to add some configuration to your basic Spring MVC Web app to make this work. It's a good idea to keep this additional configuration in a separate Spring config file, to separate it as much as possible from the standard config. In the VMforceSpringMVC sample app this config is factored into the [force-config.xml](../samples/VMforceSpringMVC/src/main/webapp/WEB-INF/spring/force-config.xml) file which looks like this:

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:context="http://www.springframework.org/schema/context"
	       xmlns:tx="http://www.springframework.org/schema/tx"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="
	        http://www.springframework.org/schema/beans
	        http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	        http://www.springframework.org/schema/context
	        http://www.springframework.org/schema/context/spring-context-3.0.xsd
	        http://www.springframework.org/schema/tx
	        http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

	    <tx:annotation-driven />
    
	    <context:component-scan base-package="com.vmforce.samples" />

		<context:property-placeholder location="classpath:connector.properties" />
		
	    <!-- Drives transactions using local JPA APIs -->
	    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
	        <property name="entityManagerFactory" ref="entityManagerFactory" />
	    </bean>
    
	    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalEntityManagerFactoryBean">
	        <property name="persistenceUnitName" value="forceDatabase" />
	    </bean>

	    <bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor" />

	 	<bean id="forceService" class="com.salesforce.connector.SFDCServiceConnector">
	    	<property name="sfdcConnectionName" value="JPAConfiguredConnection"/>
	    </bean>

	</beans>

You'll then need to add a reference to this file in the [root-config.xml](../samples/VMforceSpringMVC/src/main/webapp/WEB-INF/spring/root-config.xml):

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	
		<!-- Root Context: defines shared resources visible to all other web components -->
		
		<import resource="force-config.xml" />

	</beans>

As mentioned earlier, check out the sample app and the diff from the checkin of the plain spring MVC app to see all the detailed differences.

## Defining JPA Entities

JPA lets you create entities as normal Java bean style classes (sometimes called POJOs) and use annotations to control special behavior where necessary. The Force.com JPA provider follows the standard conventions with various limitations and some customizations.

Probably the most important constraint is how you define the primary key. You *must* declare your primary key in the following way:

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	String id;

That is: 

* Only the `GenerationType.IDENTITY` strategy is supported
* The primary key field name must be `id`
* The primary key Java type must be `String`

If you are using optimistic concurrency (see above for how to turn it on/off), you also *must* include a `lastModifiedDate` field in your entity in the following way:

	@Version 
	java.util.Calendar lastModifiedDate;

### Default Type Mappings

The JPA provider will map the fields on your entities from Java types to Force.com database field types. This table lists the mappings from Java to Force.com types:

	Java type					Default Force.com type
	-----------------------------------------------------------------------
	boolean						Checkbox
	byte						Text (255)
	char						Text (255)
	short						Number (18,0)
	int							Number (18,0) 
	long						Number (18,0) 
	double						Number (18,2) 
	float						Number (18,2) 
	Boolean						Checkbox 
	Byte						Text (255) 
	Character					Text (255)
	Short						Number (18,0) 
	Integer						Number (18,0) 
	Long						Number (18,0) 
	Double						Number (18,2) 
	Float						Number (18,2) 
	String						Text (255)
	java.util.Date				Date 						
	Calendar					Date/Time 					
	GregorianCalendar			Date/Time
	BigInteger					Number (18, 0)
	BigDecimal					Currency
	String[]					Picklist (Multi-Select) for selected values
	enum						Picklist
	URL							URL

and this table has the same mappings but listed by Force.com type:

	Force.com type				Default Java type
	-----------------------------------------------------------------------
	Auto Number					Currently not supported
	Formula						Currently not supported
	Lookup Relationship			Any @Entity annotated Java class 
	Master-Detail Relationship	Any @Entity annotated Java class
	Checkbox					boolean/Boolean
	Currency					BigDecimal
	Date						Date
	Date/Time					Calendar/GregorianCalendar
	Number						Short/short, Integer/int, Long/long, 
								Double/double, Float/float, BigInteger
	Percent						Integer/int
	Phone						String
	Picklist					enum for available values,
								String for selected value
	Picklist (Multi-Select) 	enum for available values, 
								String[] for selected values
	Text						byte, String, char/Character
	Text Area					String
	Text Area (Long)			Currently not supported
	Text Area (Rich)			Currently not supported
	URL							URL

This default mapping behavior can be customized with annotations. The JPA provider understands standard JPA annotations and also defines two custom annotations: `@CustomObject` and `@CustomField` that are used to customize mappings and define features that are not part of JPA. 

### Primary Key

In Force.com, entities always have a primary key, it is always a String and it is always assigned by the database. Therefore, your entities must always contain a primary key field defined as follows:

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	String id;

### The `@Basic` annotation

JPA defines a set of Java types that must be persisted automatically even if they are not annotated (such as String, int, double, etc.). If you want other types to be persisted you can use the `@Basic` annotation. Force.com supports this feature for ?? fields.

### Date types

Force.com has 2 different date types: Date and Date/Time. The former does not include time of day. By default, the JPA provider maps `java.util.Calendar` to Date/Time and `java.util.Date` to Date. You can customize this behavior using the JPA `@Temporal` annotation. For example, the following two Java fields will *both* be mapped to a Date/Time field in Force.com

	java.util.Calendar lastUpdated;
	
	@Temporal(TemporalType.TIMESTAMP)
	java.util.Date dateCreated;

Similarly, you can use the `@Temporal` annotation to force a calendar to become a Date field. In this example, both fields will be mapped to Date fields:

	java.util.Date birthDate;
	
	@Temporal(TemporalType.DATE)
	java.util.Calendar transactionDate;

`java.util.GregorianCalendar` can be used instead of `java.util.Calendar`.

### Length of text fields

The length of text fields is controlled by the `length` attribute of the `@Column` annotation. For example:

	@Column(length=1)
	String flag;

defines a text field called `flag` with length 1. The default length is 255.

### Precision and scale of number types

The precision and scale of number fields are controlled by the `@Column` annotation. For example:

	@Column(precision=1, scale=0)
	int digit;

Defines an integer that can range from 0 to 9. The default precision and scale are 18 and 0 for integers and 18 and 2 for floats and doubles (see table above).

### Enumeration (picklist)

Force.com supports enumerations as a data type. You will see these referred to as picklists and multi-select picklists in Force.com terminology. Here's how you can define a field that takes its value from an enumeration in the following way:

	public enum Variety { Cabernet_Sauvignon, Syrah, Pinot_Noir, Zinfandel }

    @Enumerated
    private Variety variety;

By default, Force.com will store ordinal values for your selection. For example if you set `variety` to `Syrah`, it will be stored as the value `1` in the database. To store the value strings instead of ordinals, you can do:

	public enum Variety { Cabernet_Sauvignon, Syrah, Pinot_Noir, Zinfandel }

	@Enumerated(EnumType.STRING)
	private Variety variety;

Multi-value selections is also supported. If you want to pick multiple varietals for a single wine, you simply use an array type instead:

	public enum Variety { Cabernet_Sauvignon, Syrah, Pinot_Noir, Zinfandel }

	@Enumerated(EnumType.STRING)
	private Variety[] variety;

### Relationships

The Force.com database supports relationships between entities. You can perform join queries on related entities, but not on unrelated ones. Force.com defines two types of relationships: Lookup and master-detail.

#### Lookup relationship

A lookup relationship is a foreign key to another entity. As opposed to the master-detail relationship, lookups do not trigger cascading deletes. I.e. if record A references record B, record A will not be deleted if record B is. A lookup relationship is always defined as a many-to-one relationship on the single cardinality side. For example, if the entity `Wine` has a many-to-one relationship to the entity `Producer`, the relationship is defined on the `Wine` entity as follows:

	@ManyToOne
	Producer producer;

If you want the field name in the Force.com database to be something other than `producer`, you can use the `@Column` annotation

	@Column(name = "wine_producer")
	@ManyToOne
	Producer producer;

Bi-directional lookups are fully supported in Force.com, but as of 20.0.1, they are not mapped properly in JPA. This should be fixed in the next release. This means that right now you can't navigate a relationship from the other side using the `@OneToMany` annotation.

#### Master-detail relationship

A master-detail relationship is similar to a lookup relationship, with the difference that all children are deleted when the parent is deleted (i.e. it has cascade delete). Use the following construct to construct this relationship:

	@ManyToOne
	@CustomField(type = FieldType.MasterDetail)
	Producer producer;

### Making your entity collaborative with Chatter

Force.com has collaboration built-in. You can enable feeds for every entity in your database. Users can follow entities and be updated when they change. To enable Chatter on an entity, you use the `@CustomObject(enableFeeds=true)` annotation on the entity class. For example:

	@Entity
	@CustomObject(enableFeeds=true)
	public class Wine {

	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
		private String id;
		
		String name;
	
	}

Once feeds is enabled for an entity, you can specify which fields should trigger feed updates using the `@CustomField(feedsEnabled=true)`. For example:

	@CustomField(enableFeeds=true)
	private String myField;

	public String getMyField() {
		return myField;
	}
	
	public void setMyField(String value) {
		myField = value;
	}

### Other Force.com specific types

#### Email

If you want to create a Force.com `email` field, use `@CustomField(type=FieldType.Email)` with a `String` Java type. For example:

	@CustomField(type=FieldType.Email)
	String orderEmail;

#### Phone

If you want to create a Force.com `phone` field, use  `@CustomField(type=FieldType.Phone)` with a `String` Java type. For example:

	@CustomField(type=FieldType.Phone)
	String businessPhone;

#### Percent

If you want to create a Force.com `percent` field, use `@CustomField(type=FieldType.Percent)` with an `int` or `Integer` Java type. For example:

	@CustomField(type=FieldType.Percent)
	int mainVarietalPercent;

 
## Writing persistence code

coming soon...
