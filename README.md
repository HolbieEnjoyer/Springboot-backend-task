# Simple Employee API in Spring Boot

[![Build and Push Docker Image](https://github.com/HolbieEnjoyer/Springboot-backend-task/actions/workflows/docker.yml/badge.svg)](https://github.com/HolbieEnjoyer/Springboot-backend-task/actions/workflows/docker.yml)

## Overview
1. [About](#about)
2. [Technologies Used](#technologies-used)
3. [Installation](#installation)
4. [Configuration](#configuration)
5. [Usage](#usage)
   - [User Credentials in Prod](#user-credentials-in-prod)
6. [API Endpoints](#api-endpoints)
    - [Authentication](#authentication)
    - [Employee Management](#employee-management)
7. [Authorization & Roles](#authorization--roles)
8. [Deployment](#deployment)

## About
This is a comprehensive RESTful API for managing employee records with role-based access control (RBAC). Built using Spring Boot, the API provides secure authentication via JWT tokens and supports multiple user roles including regular employees, admins, and a super admin. The system includes features for employee registration, profile management, promotion/demotion, and advanced filtering capabilities.

## Technologies Used
- **Java 21** - Core programming language
- **Spring Boot 3.5.7** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations with specification-based filtering
- **PostgreSQL** - Primary database
- **Flyway** - Database migration management
- **JWT (JSON Web Tokens)** - Secure authentication mechanism
- **SpringDoc OpenAPI** - API documentation (Swagger UI)
- **Gradle** - Build automation
- **Docker** - Containerization
- **Render** - Cloud deployment platform
- **GitHub Actions** - CI/CD pipeline

## Installation

### Prerequisites
- Java 21 or higher
- Docker (optional, for containerized deployment)
- PostgreSQL database

### Local Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/HolbieEnjoyer/Springboot-backend-task
   ```

2. Navigate to the project directory:
   ```bash
   cd Springboot-backend-task
   ```

3. Build the project using Gradle:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

### Docker Setup
1. Build the Docker image:
   ```bash
   docker build -t employee-api .
   ```

2. Run the container:
   ```bash
   docker run -p 10030:10030 employee-api
   ```

## Configuration
Create a `.env` file in the project root with the following variables:

```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/employee_db
DB_USER=your_username
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET_KEY=your_jwt_secret_key

# Super Admin Configuration
ADMIN_USERNAME=admin@company.com
ADMIN_PASSWORD=yourpassword
```

## Usage
Once the application is running, you can access:
- **API Base URL**: `http://localhost:10030`
- **Swagger UI**: `http://localhost:10030/swagger-ui.html`
- **API Docs**: `http://localhost:10030/v3/api-docs`

All protected endpoints require a JWT token in the `Authorization` header:
```
Authorization: Bearer <your_jwt_token>
```

### User Credentials in Prod
> [!WARNING]
> Normally, the super admin credentials are set via environment variables (`ADMIN_USERNAME` and `ADMIN_PASSWORD`) during deployment.
> For the purposes of testing, I am sharing them here, but in a real-world scenario, these should be kept secret.

|          | Super Admin | All Other Users 
|----------|-------------|-----
| Email    | root@seriouscompany.org            | Obtain via /employees/list while logged in as super admin
| Password | Sup3rS3cur3P@ssw0rd!            | Password123

## API Endpoints

### Authentication
Base path: `/api/v1/auth`

#### Register Employee
```http
POST /api/v1/auth/register
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "password": "SecurePassword123"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@company.com",
  "role": "EMPLOYEE",
  "createdAt": "2025-11-20T10:30:00"
}
```

#### Login
```http
POST /api/v1/auth/login
```

**Request Body:**
```json
{
  "email": "john.doe@company.com",
  "password": "SecurePassword123"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@company.com",
  "role": "EMPLOYEE",
  "createdAt": "2025-11-20T10:30:00",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": "86400000"
}
```

---

### Employee Management
Base path: `/api/v1/employees`

All endpoints below require authentication.

#### Get Current User Profile
```http
GET /api/v1/employees/me
```

**Headers:**
```
Authorization: Bearer <token>
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@company.com",
  "role": "EMPLOYEE",
  "createdAt": "2025-11-20T10:30:00"
}
```

#### View Employee by Email
```http
GET /api/v1/employees/view/{email}
```

**Parameters:**
- `email` (path) - Employee email address

**Response:** `200 OK`
```json
{
  "id": 2,
  "name": "Jane Smith",
  "email": "jane.smith@company.com",
  "role": "ADMIN",
  "createdAt": "2025-11-15T09:00:00"
}
```

**Note:** Super admin information cannot be accessed via this endpoint.

#### Update Own Profile
```http
PUT /api/v1/employees/updateMyInfo
```

**Request Body:**
```json
{
  "name": "John Updated Doe",
  "email": "john.updated@company.com",
  "password": "NewSecurePassword123"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "John Updated Doe",
  "email": "john.updated@company.com",
  "role": "EMPLOYEE",
  "createdAt": "2025-11-20T10:30:00"
}
```

**Note:** Super admin account cannot be updated via this endpoint.

#### Delete Own Account
```http
DELETE /api/v1/employees/deleteMyAccount
```

**Response:** `204 No Content`

**Note:** Super admin account cannot be deleted.

#### List Employees (with Filtering & Pagination)
```http
GET /api/v1/employees/list
```

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Page size
- `orderByDate` (optional, default: "desc") - Sort order ("asc" or "desc")
- `role` (optional) - Filter by role (e.g., "EMPLOYEE", "ADMIN")
- `dateJoinedBefore` (optional) - Filter employees joined before date (ISO format: YYYY-MM-DD)
- `dateJoinedAfter` (optional) - Filter employees joined after date (ISO format: YYYY-MM-DD)

**Example Request:**
```http
GET /api/v1/employees/list?page=0&size=20&orderByDate=asc&role=ADMIN&dateJoinedAfter=2025-01-01
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane.smith@company.com",
      "role": "ADMIN",
      "createdAt": "2025-11-15T09:00:00"
    }
  ],
  "currentPage": 0,
  "totalItems": 1,
  "totalPages": 1
}
```

---

### Admin Operations
**Required Role:** `ADMIN` or `SUPERADMIN`

#### Delete Employee
```http
DELETE /api/v1/employees/delete/{id}
```

**Parameters:**
- `id` (path) - Employee ID

**Response:** `204 No Content`

**Authorization Rules:**
- Super admin cannot be deleted
- Only super admin can delete admin users
- Admins can delete regular employees

#### Promote Employee
```http
PUT /api/v1/employees/promote/{id}
```

**Parameters:**
- `id` (path) - Employee ID to promote

**Response:** `200 OK`
```json
{
  "id": 3,
  "name": "Bob Johnson",
  "email": "bob.johnson@company.com",
  "role": "ADMIN",
  "createdAt": "2025-11-18T14:20:00"
}
```

**Note:** Promotes an employee to the next role level. Super admin cannot be promoted.

---

### Super Admin Operations
**Required Role:** `SUPERADMIN`

#### Demote Employee
```http
PUT /api/v1/employees/demote/{id}
```

**Parameters:**
- `id` (path) - Employee ID to demote

**Response:** `200 OK`
```json
{
  "id": 3,
  "name": "Bob Johnson",
  "email": "bob.johnson@company.com",
  "role": "EMPLOYEE",
  "createdAt": "2025-11-18T14:20:00"
}
```

**Note:** Demotes an employee to a lower role level. Super admin cannot be demoted.

---

## Authorization & Roles

### Role Hierarchy
1. **SUPERADMIN** - Full system access, cannot be modified or deleted
2. **ADMIN** - Can manage regular employees, promote users
3. **EMPLOYEE** - Basic access, can manage own profile

### Role-Based Access Control

| Endpoint | EMPLOYEE | ADMIN | SUPERADMIN |
|----------|----------|-------|------------|
| Register/Login | ✅ | ✅ | ✅ |
| Get Self Profile | ✅ | ✅ | ✅ |
| View Employee | ✅ | ✅ | ✅ |
| Update Own Profile | ✅ | ✅ | ❌ |
| Delete Own Account | ✅ | ✅ | ❌ |
| List Employees | ✅ | ✅ | ✅ |
| Delete Employee | ❌ | ✅* | ✅ |
| Promote Employee | ❌ | ✅ | ✅ |
| Demote Employee | ❌ | ❌ | ✅ |

*Admins can only delete regular employees, not other admins.

### Special Restrictions
- Super admin account is protected from all modification and deletion operations
- Super admin information cannot be viewed via the `/view/{email}` endpoint
- Admins cannot delete or modify other admin accounts (only super admin can)

## Deployment

### Docker Deployment
The application is containerized and can be deployed using Docker. A GitHub Actions workflow automatically builds and pushes Docker images on each commit.

### Render Deployment
The application can be deployed on Render:
1. Connect your Dockerhub to Render
2. Configure environment variables in Render dashboard
3. Deploy from the Docker image

### Environment Variables for Production
Ensure the following environment variables are set in your production environment:
- `SPRING_DATASOURCE_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET_KEY`
- `ADMIN_USERNAME`
- `ADMIN_PASSWORD`

---

## Error Handling

The API returns standard HTTP status codes:

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `204 No Content` - Successful deletion
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

**Error Response Format:**
```json
{
  "status": 400,
  "message": "Password is mandatory",
  "timestamp": "2025-11-19T21:44:32.435822777",
  "errors": {
    "password": "Password is mandatory",
    "email": "Email format should be valid"
  }
}
```

---

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License.

---

**Questions or Issues?** Please open an issue on the GitHub repository.