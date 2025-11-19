# Simple Employee API in Spring Boot

[![Build and Push Docker Image](https://github.com/HolbieEnjoyer/Springboot-backend-task/actions/workflows/docker.yml/badge.svg)](https://github.com/HolbieEnjoyer/Springboot-backend-task/actions/workflows/docker.yml)
## Overview
1. [About](#about)
2. [Technologies Used](#technologies-used)
3. [Installation](#installation)
4. [Usage](#usage)
5. [API Endpoints](#api-endpoints)


## About
This is a simple RESTful API for managing employee records, built using Spring Boot. The API

## Technologies Used
- Java 21
- Spring Boot
- Docker
- Gradle
- Postgres
  (more can be added later)

## Installation
1. Clone the repository:
   ```git clone https://github.com/HolbieEnjoyer/Springboot-backend-task```
2. Navigate to the project directory:
   ```cd Springboot-backend-task```
3. Build the project using Gradle: 
   ```./gradlew build```
4. Run the application:
   ```./gradlew bootRun```

## Usage
Once the application is running, you can access the API at `http://localhost:10030

## API Endpoints
### Authentication
- `POST /auth/login` - Authenticate a user and receive a JWT token
- `POST /auth/register` - Register a new user
### Employee Management
- `GET /employees` - Retrieve all employees
- `GET /employees/{email}` - Retrieve an employee by email