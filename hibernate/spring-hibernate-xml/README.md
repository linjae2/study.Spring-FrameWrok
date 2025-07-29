## Abstract

This project is a sample of a Spring/Hibernate project with **XML configuration**. It is aimed to compare Spring/Hibernate configuration with another project, **annotation-driven**, in parallel. 

## Project structure

```
Project
 |  src
 |   |-main
 |   |   |-java
 |   |   |   |-com
 |   |   |       |-dohbedoh
 |   |   |           |-SpringHibernateXml.java
 |   |   |           |-dao
 |   |   |               |-AbstractDAO.java
 |   |   |               |-CompanyDAO.java
 |   |   |               |-impl
 |   |   |                   |-CompanyDAOImpl.java
 |   |   |           |-model
 |   |   |               |-Company.java
 |   |   |           |-service
 |   |   |               |-CompanyService.java
 |   |   |-resources
 |   |       |-logback.xml
 |   |       |-spring-hibernate-xml.xml
 |   |       |-config
 |   |       |   |-daos.xml
 |   |       |   |-datasource.xml
 |   |       |   |-hibernate.xml
 |   |       |   |-transaction.xml
 |   |       |-database
 |   |       |   |-bootstrap.sql
 |   |       |   |-database.properties
 |   |       |-hibernate
 |   |        	 |-hibernate.cfg.xml
 |   |        	 |-com
 |   |        	     |-dohbedoh
 |   |  		         |-model
 |   |  				     |-Company.hbm.xml
 |   |-test
 |   |   |-java
 |   |   |   |-com
 |   |   |       |-dohbedoh
 |   |   |           |-dao
 |   |   |  			 |-CompanyDAOTest.java
 |   |   |           |-service
 |   |   |  			 |-CompanyServiceTest.java
 |   |   |-resources
 |   |       |-logback-test.xml
 |   |       |-spring-hibernate-xml-test.xml
 pom.xml
```

## Database

The project is using a simple MySQL database. The SQL file *resources/database/bootstrap.sql* contains the queries necessary to create the database and the required tables.

###Data Model

A simple data model is used to play with Hibernate configuration. Basically, simple tables with primary/foreign keys. The following tables are created.

####Address Table: A table containing addresses *ADDRESS*

| Field               | Type          | NULL  | KEY                 |
| ------------------- |:-------------:|:-----:|:-------------------:|
| ADDRESS_ID          | smallint(6)   |  NO   |      PRIMARY        |
| ADDRESS_NUMBER      | varchar(4)    |  NO   |                     |
| ADDRESS_LINE_1      | varchar(12)   |  NO   |                     |
| ADDRESS_LINE_2      | varchar(12)   |  YES  |                     |
| ADDRESS_ZIP_CODE    | varchar(5 )   |  NO   |                     |
| ADDRESS_CITY        | varchar(12)   |  NO   |                     |
| ADDRESS_STATE       | varchar(12)   |  NO   |                     |
| ADDRESS_COUNTRY     | varchar(30)   |  NO   |                     |

####Contact Table: A table containing contact details *CONTACT*

| Field               | Type          | NULL  | KEY                 |
| ------------------- |:-------------:|:-----:|:-------------------:|
| CONTACT_ID          | smallint(6)   |  NO   |      PRIMARY        |
| CONTACT_PHONE_NUMBER| varchar(12)   |  NO   |                     |
| CONTACT_FAX_NUMBER  | varchar(12)   |  NO   |                     |
| CONTACT_EMAIL       | varchar(30)   |  NO   |                     |

####Company Table: A table containing companies *COMPANY* that references *ADDRESS* and *CONTACT*

| Field               | Type          | NULL  | KEY                 |
| ------------------- |:-------------:|:-----:|:-------------------:|
| COMPANY_ID          | smallint(6)   |  NO   |      PRIMARY        |
| COMPANY_NAME        | varchar(32)   |  NO   |                     |
| COMPANY_ADDRESS_ID  | smallint(12)  |  NO   |  ADDRESS.ADRESS_ID  |
| COMPANY_CONTACT_ID  | smallint(30)  |  NO   |  CONTACT.CONTACT_ID |

###Configuration

The properties to access to the database are located in *resources/database/database.properties*

####database.properties
```
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:8084/dohbedoh
jdbc.username=root
jdbc.password=admin
```

We use the mysql-connector-java, a JDBC driver for MySQL.

####pom.xml
```xml
...
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>${mysql-connector.version}</version>
</dependency>
...
```

We use the **mysql-connector-java** (JDBC driver for MySQL), and a *Apache DBCP2* (database connection pool).

####pom.xml
```xml
...

	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>${mysql-connector.version}</version>
	</dependency>
	
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-dbcp2</artifactId>
        <version>${apache-dbcp.version}</version>
    </dependency>

...
```

The data source is configured in *resources/config/datasource.xml* and make use of the properties set up in *resources/database/database.properties*.

####datasource.xml
```xml
...
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="location">
            <value>database/database.properties</value>
        </property>
    </bean>

    <bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

...
```

## Hibernate

####Project Hibernate Structure
```
 resources
    |-hibernate
        |-hibernate.cfg.xml
            |-com
                |-dohbedoh
                    |-model
                        |-Company.hbm.xml
```

The entry point of Hibernate configuration is  *resources/hibernate.cfg.xml*. 

```xml
...
	<hibernate-configuration>
		<session-factory>

			<property name="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</property>
			<property name="show_sql">true</property>
			
			...
			<mapping resource="hibernate/com/dohbedoh/model/Company.hbm.xml"></mapping>
			...
			
		</session-factory>
	</hibernate-configuration>
...
```

Each model configuration is located in the corresponding package under *resources/hibernate*. For example, the mapping of *com/dohbedoh/model/Company.java* is defined in *resources/hibernate/com/dohbedoh/model/Company.hbm.xml*.

####Company.hbm.xml
```xml
...
<hibernate-mapping>
    <class name="com.dohbedoh.model.Company" table="COMPANY">
        <meta attribute="class-description">
            This class contains the Company details.
        </meta>
        <id name="companyId" type="int">
            <column name="COMPANY_ID" precision="5" scale="0"/>
            <generator class="native"/>
        </id>
        <property name="companyName" type="string">
            <column name="COMPANY_NAME" length="32" not-null="true"/>
        </property>
        <property name="addressId" type="int">
            <column name="COMPANY_ADDRESS_ID" precision="5" not-null="true"/>
        </property>
        <property name="contactId" type="int">
            <column name="COMPANY_CONTACT_ID" precision="5" not-null="true"/>
        </property>
    </class>
</hibernate-mapping>
...
```

## Spring

####Project Spring Structure
```
 resources
    |-spring-hibernate-xml-sandbox.xml
       |-config
           |-daos.xml
           |-datasource.xml
           |-hibernate.xml
           |-transaction.xml
```

The configuration is divided in separate configuration files:
- Data Source in *resources/config/datasource.xml*
- Hibernate in *resources/config/hibernate.xml*
- Transaction Manager in *resources/config/transaction.xml*
- DAOs in *resources/config/daos.xml*

Each configuration is imported in the spring configuration entry point *resources/spring-hibernate-xml-sandbox.xml*.

####spring-hibernate-xml-sandbox.xml
```xml
...
    <import resource="config/datasource.xml" />
    <import resource="config/hibernate.xml" />
    <import resource="config/transaction.xml" />
    <import resource="config/daos.xml" />
...
```

## Testing

COming soon...
