# Wex Purchases Control
Author: Lucas Moreira Carneiro de Miranda

The objective of this project is to implement a Java application that exposes a REST microservice with two endpoints:

1) <b>Record Purchase:</b> Receives data associated with a purchase in USD following some validation rules and stores them in a database, returning a unique identifier number for that purchase.

2) <b>Retrieve a Recorded Purchase:</b> Receives a purchase identification number and additional data to convert the original purchase amount from USD to a specified currency. Conversion rates use the Treasury Reporting Rates of Exchange API based on the exchange rate active for the date of the purchase, as per the link:


    https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange





## Rules for Recording a Purchase:

It's necessary to send an HTTP POST request to <i>/wex-purchases-control/store</i> with a JSON containing the following mandatory fields:
- <b>description:</b> Descriptive text of the purchase with at least 1 character and a maximum length of 50 characters.
- <b>transactionDate:</b> Date in Year/Month/Day format that is less than or equal to the current date.
- <b>purchaseAmountUSD:</b> Purchase amount with a maximum of 2 decimal digits.


Here is an example JSON file:
```
{
    "description": "Maternity parking ticket", 
    "transactionDate": "1986-08-01", 
    "purchaseAmountUSD": 3.14
}
```

## Rules for Retrieving Purchase Data:

When a purchase is recorded, a unique identifier is generated and sent in the service response. This number is needed to query a recorded purchase. The HTTP request must use the GET method for the URL <i>/wex-purchases-control/get</i> In addition to this identifier, the following are also required:
- <b>id:</b> Purchase identifier.
- (optional) <b>country:</b> Country associated with a given exchange rate.
- (optional) <b>currency:</b> Currency associated with a given exchange rate.

Note: The values of Country and Currency must correspond to the names used by the Treasury Reporting Rates of Exchange API. If Country and Currency are not defined, the purchase value is returned without conversion to a specific currency.


## Project Structure and Technologies Used

The project was developed using Java, Spring Boot, MySQL, Docker, Docker Compose, and Kubernetes. Following the Model View Controller (MVC) standard concept, the business logic was organized within the model package and encapsulated in service layers.

The two main services are:

- <b>PurchaseTransactionService:</b> Service responsible for managing the business rules for purchase transactions.
- <b>CurrencyExchangeRateService:</b> Service that performs currency conversion based on a date.

The services are defined by interfaces, in order to expose the necessary functionalities to meet the project's specifications, allowing for the modularization of each functionality and facilitating future changes in implementation.

For database access, a Data Access Object (DAO) class was created that accesses the database using SQL.

The application contains in <i>resources/sql/</i> the SQL file  <b>create_purchase_transaction_table.sql</b>  for creating the table used by the project, in order to comply with the specification:

<blockquote>
Your application repository should be fully functional without installing separate databases, web servers, or servlet containers (e.g., Jetty, Tomcat, etc).
</blockquote>

### Spring
The choice to use the Spring framework and Spring Boot is due to their robustness and industry-standard status in Java backend development. They simplify the process by bundling the project and its dependencies into a single executable file, thus enhancing reliability and modularity. This setup is ideal for deploying scalable microservices and ensures seamless integration with tools such as Docker and Kubernetes.

### MySQL Database and the Use of SQL

The decision to use SQL was made for greater control over persisted data. This decision was motivated by the following reasons:

- To prevent any unplanned changes in the database.
- To ensure the integrity and quality of the recorded data.

Thus, the project uses a MySQL relational database.


### Docker and Docker Compose

There are numerous advantages to using Docker, worth mentioning the main ones:
- Consistency of Environments: Same environment for development, testing, and production, reducing compatibility issues.
- Dependency Isolation: Separate containers for each dependency, avoiding conflicts.
- Simplified Configuration: Docker Compose facilitates the configuration and execution of multiple services.
- Efficient Deployment: Quick setup and initialization of environments.
- Scalability: Facilitates the scalability and replication of services.


### Kubernetes
Configurations were created to allow the application to be published using Kubernetes, which offers automatic scalability and efficient container management, optimizing the deployment and operation of large-scale distributed applications.


### Unit and Integration Tests
Unit Tests and Integration Tests were developed. The integration tests were carried out using <b>SpringBootTest</b>, <b>Spring MockMvc</b>, and the <b>org.testcontainers</b> library for MySQL. This way, it is possible to test the integration of the project with both the Treasury Reporting Rates of Exchange API and the database access.

Unit tests are executed when generating the package but can be run independently using the MAVEN command:

```
./mvnw -f app/pom.xml test
```

To execute the integration tests, it is necessary to run the MAVEN command:
```
./mvnw -f app/pom.xml verify
```



## List of Decisions Made


### 1. Do not round inputted monetary information (Purchase transactions)

To avoid issues related to rounding, the purchase transaction validation expects the monetary values to be already rounded.


### 2. Allow get Purchase by id only 

Allow retrieval of the purchase only in the original USD value. This way, it is possible to verify if the purchase was recorded correctly and not depend on the external API for a query.


### 3. Purchase Timezone

It was assumed that all requests use a standard reference timezone, not delegating to the application the need to handle date conversions based on time zones.

### 4. Duplicate Information

The project does not perform any validation on duplicate requests, allowing the recording of purchase transactions with the same data, assuming that HTTP POST calls are not idempotent.


## Compilation and Execution of the Project

Initially, it is necessary to compile the project's jar package. Maven is included in the project through Maven Wrapper. Execute the Maven command in the project root directory:
```
./mvnw -f app/pom.xml clean package
```

Ensure that at the end of the process you have the file: /wex-purchases-control/app/target/wex-purchases-control-1.0.0.jar

### Executing the Application Independently

It is possible to execute the application independently through Docker or directly via Spring Boot. For this, a MySQL database must be available and accessible for the application to communicate as per the application.yml file. In addition, the following environment variables need to be defined (see .env.example):

- MYSQLDB_DATABASE
- MYSQLDB_USER
- MYSQLDB_PASSWORD

However, this will not be the recommended way to publish the application and is only described here to indicate that it is possible.


### Docker Compose

With Docker Compose, it is possible to launch the application along with MySQL in a multi-container. Command to start the application using Docker Compose:

1) Ensure you have Docker configured, such as Docker Desktop or an appropriate environment. Make sure you have the file /wex-purchases-control/app/target/wex-purchases-control-1.0.0.jar (MAVEN command ./mvnw -f app/pom.xml clean package)
2) Configure the .env file using the .env.example file as a reference
3) In the project root folder, execute the command: docker-compose up
4) The docker-compose configuration dictates that the database needs to be accessible before starting the application. Therefore, it may be necessary to wait a few additional seconds for the wex-purchases-control-app to start


Example of .env file
```
MYSQLDB_USER=lucas
MYSQLDB_PASSWORD=miranda
MYSQLDB_ROOT_PASSWORD=lucas.miranda
MYSQLDB_DATABASE=wex-purchases-db
MYSQLDB_DOCKER_PORT=3306
APP_DOCKER_PORT=8080
APP_EXTERNAL_PORT=8080
```

### Using Kubernetes


1) <b>Kubernetes Environment Setup:</b> Ensure that you have a properly configured Kubernetes environment ready for deployment. This includes having Kubernetes and kubectl installed and a running Kubernetes cluster (like Minikube for local setups or a cloud-based Kubernetes service)

2) <b>Building the Project JAR File:</b>Ensure that you have the JAR file created by Maven at <b>/wex-purchases-control/app/target/wex-purchases-control-1.0.0.jar</b> You can build it using the following Maven command: 
```
./mvnw -f app/pom.xml clean package
```
3) <b>Building the Docker Image:</b> In the /wex-purchases-control/app/ folder, build the Docker image of the application with the following command. This command creates a Docker image from your application, tagging it as v1.0.
```
docker build -t wex-purchases-control-app:v1.0 .
```
4) <b>Creating Kubernetes Secrets:</b> Create Kubernetes Secrets for storing database access credentials. Secrets in Kubernetes are used to securely store and manage sensitive data like usernames, passwords, and database credentials.
   Example:
```
kubectl create secret generic mysql-secret --from-literal=MYSQLDB_USER=lucas --from-literal=MYSQLDB_PASSWORD=miranda --from-literal=MYSQLDB_ROOT_PASSWORD=lucas.miranda
```
5) <b>Deploying the MySQL Database:</b> Return to the <i>/wex-purchases-control/</i> root folder. Deploy the MySQL database using the <b>mysql-deployment.yml</b> file. This file contains the configuration for setting up a MySQL database in your Kubernetes cluster.
```
kubectl apply -f kubernetes/mysql-deployment.yml
``` 
6) <b>Deploying the Application: </b>Deploy the application using the <b>app-deployment.yml</b> file. This file contains the necessary configuration to launch and run your application on Kubernetes.
```
kubectl apply -f kubernetes/app-deployment.yml
```
7) <b>Verifying Deployment:</b> After deploying the database and the application, verify that everything is running correctly by using commands such as kubectl get pods. This will help you ensure that your application and database pods are up and running in the Kubernetes cluster.


### Testing:

To test the deployed application, use the following CURL commands:
#### Send a Record

```
curl -i -X POST -H "Content-Type: application/json" --data '{"description": "Uber from A to B", "transactionDate": "2023-11-08", "purchaseAmountUSD": 3.14}' --location 'http://localhost:80/wex-purchases-control/store'
```

#### Retrieve Purchases with a Currency and Country
```
curl -i -X GET --location 'http://localhost:80/wex-purchases-control/get?id=1&country=United%20Kingdom&currency=Pound'
```

```
curl -i -X GET --location 'http://localhost:80/wex-purchases-control/get?id=1&country=Brazil&currency=Real'
```

#### Retrieve Purchases without Converting to Another Currency


```
curl -i -X GET --location 'http://localhost:80/wex-purchases-control/get?id=1'
```
