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
The application uses an H2 in-memory database by default. 

- JDBC URL: jdbc:h2:mem:SessionDB
- Username: sa
- Password: 



## API Endpoints

1. Book a Session
Endpoint: `POST /api/session/book`

   ```bash
   {
     "userId": 1,
     "mentorId": 101,
     "sessionDate": "2023-12-20T12:30:00",
     "frequency": "weekly",
     "bookedAt": "2023-12-06T10:05:00"
   }


   ```bash
   {
     "userId": 2,
     "mentorId": 102,
     "sessionDate": "2023-12-25T12:00:00",
     "frequency": "biweekly",
     "bookedAt": "2023-12-07T10:05:00"
   }


2. Cancel a Session
Endpoint: `DELETE /api/session/cancel`


