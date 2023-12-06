# Assignment-LPT
Java Backend Assignment (Spring Boot): Develop REST APIs for booking consultation sessions, canceling sessions before a specific time, and rescheduling sessions before the scheduled commencement time.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Configuration](#database-configuration)
- [API Endpoints](#api-endpoints)

## Prerequisites

- Java 11 or later
- Maven
- Git

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/shubhampawar672/Assignment-LPT.git
   
2. Change directory to the project folder:

   ```bash
   cd Assignment-LPT


3. Build the application:

   ```bash
   mvn clean install



4. Run the application



The application will be running on http://localhost:8080.





## Database Configuration
The application uses an H2 in-memory database by default. You can access the H2 console at http://localhost:8080/h2-console with the following credentials:

spring.datasource.url=`jdbc:h2:mem:SessionDB`
spring.datasource.driverClassName=`org.h2.Driver`
spring.datasource.username=`sa`
spring.datasource.password=


   
