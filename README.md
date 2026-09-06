# Spring Boot JWT Project Management API

A secure RESTful Project Management API built with **Java and Spring Boot**, focused on **JWT-based authentication, role-based authorization, refresh-token management, and project-level access control**.

The API allows administrators and project managers to manage users, projects, memberships, and tasks while enforcing authorization based on both user roles and project ownership.

## Features

* JWT-based authentication
* Stateless authentication with Spring Security
* Role-based authorization
* Project-level authorization for managers
* Access and refresh token management
* Access-token revocation
* Refresh-token revocation
* User management
* Project management
* Project membership management
* Task management
* Password encryption using BCrypt
* JWT token blacklisting
* Input validation
* Global exception handling
* OpenAPI / Swagger documentation
* JPA / Hibernate persistence
* MySQL database integration

## Security Model

The application uses three roles:

| Role    | Responsibilities                                        |
| ------- | ------------------------------------------------------- |
| ADMIN   | Manage users, projects, and memberships                 |
| MANAGER | Manage tasks and memberships for their assigned project |
| MEMBER  | No access to protected API endpoints                    |

Authentication is handled using **JWT access and refresh tokens**.

After successful login, the client receives:

* Access token
* Refresh token

The access token is used for protected requests:

```text
Authorization: Bearer <access-token>
```

Authorization is enforced at two levels:

1. **Role-based authorization** through Spring Security
2. **Project-level authorization** through the service layer

For example, a MANAGER can only manage tasks belonging to the project assigned to that manager.

A MEMBER can authenticate but does not have permission to access the protected API functionality.

## JWT Authentication Flow

```text
Login
  ↓
Username + Password
  ↓
Spring Security Authentication
  ↓
Generate Access Token
  ↓
Generate Refresh Token
  ↓
Store Refresh Token + Current Access Token
  ↓
Return Tokens
```

The access token is short-lived, while the refresh token remains valid for a longer period.

When the access token expires:

```text
Refresh Token
     ↓
Validate Refresh Token
     ↓
Find Refresh Token in Database
     ↓
Revoke Previous Access Token
     ↓
Generate New Access Token
     ↓
Update Current Access Token
     ↓
Return New Access Token
```

The refresh token itself remains valid until it expires or is revoked.

## Token Revocation

The application maintains a token blacklist for revoked tokens.

When a new access token is generated using a refresh token, the previous access token is revoked.

Logout revokes both:

* Current access token
* Refresh token

This prevents revoked tokens from being used again.

## Request Flow

```text
Client
   ↓
Controller
   ↓
Spring Security / JWT Filter
   ↓
Service Layer
   ↓
Mapper
   ↓
Repository
   ↓
Database
```

For manager-specific task operations:

```text
JWT
 ↓
Authenticated Manager
 ↓
User
 ↓
Membership
 ↓
Assigned Project
 ↓
Task Project
 ↓
Authorization Check
```

This prevents a manager from accessing or modifying tasks belonging to another project.

## Authentication Endpoints

### Login

```http
POST /api/v1/auth/Login?username={username}&password={password}
```

A successful login returns:

```json
{
  "accessToken": "JWT_ACCESS_TOKEN",
  "refreshToken": "JWT_REFRESH_TOKEN"
}
```

Use the access token for protected endpoints:

```http
Authorization: Bearer <access-token>
```

### Refresh Access Token

```http
POST /api/v1/refresh?refreshToken={refreshToken}
```

The refresh token is validated and used to generate a new access token.

The previous access token is revoked and the newly generated access token becomes the current valid access token for that refresh-token session.

The refresh token remains valid until its own expiration or revocation.

### Logout

```http
POST /api/v1/auth/Logout
```

Send the current access token:

```http
Authorization: Bearer <access-token>
```

The application identifies the authenticated user, finds the associated refresh token, and revokes both the access and refresh tokens.

## Main API Areas

### Authentication

```text
POST /api/v1/auth/Login
POST /api/v1/auth/Logout
POST /api/v1/refresh
```

### Users

```text
POST   /api/v1/users
GET    /api/v1/users
GET    /api/v1/users/{id}
PUT    /api/v1/users/{id}
DELETE /api/v1/users/{id}
```

### Projects

```text
POST   /api/v1/projects
GET    /api/v1/projects
GET    /api/v1/projects/{id}
PUT    /api/v1/projects/{id}
DELETE /api/v1/projects/{id}
```

### Memberships

```text
POST   /api/v1/membership
GET    /api/v1/membership
GET    /api/v1/membership/{id}
PUT    /api/v1/membership/{id}
DELETE /api/v1/membership/{id}
```

### Tasks

```text
POST   /api/v1/tasks
GET    /api/v1/tasks
GET    /api/v1/tasks/{id}
PUT    /api/v1/tasks/{id}
DELETE /api/v1/tasks/{id}
```

## Technology Stack

* Java 21
* Spring Boot 4.1.1
* Spring Security 7
* JSON Web Tokens (JWT)
* JJWT
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* OpenAPI / Swagger

## Project Structure

```text
src/main/java
└── project_management__api
    ├── configuration
    ├── controller
    ├── dtos
    ├── entities
    ├── enums
    ├── exceptions
    ├── mapper
    ├── repositories
    ├── Security
    └── service
```

## Database Configuration

Configure the database in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/project_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

JWT configuration:

```properties
jwt.secret=YOUR_SECRET_KEY
jwt.access-expiration=900000
jwt.refresh-expiration=604800000
```

The default configuration represents:

```text
Access Token  → 15 minutes
Refresh Token → 7 days
```

Do not commit real database passwords or JWT secrets to the repository.

## Running the Application

Clone the repository:

```bash
git clone https://github.com/SadafJabbar/JWT-Project-Management-API.git
```

Run with Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The API will run on:

```text
http://localhost:8080
```

## API Documentation

OpenAPI documentation:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Authorization Example

A manager assigned to Project 1 can create a task for Project 1:

```json
{
  "title": "Implement User Dashboard",
  "description": "Create the dashboard for project users",
  "status": "TODO",
  "priority": "MEDIUM",
  "projectId": 1
}
```

If the same manager attempts to create, update, retrieve, or delete a task belonging to another project, the request is rejected.

This ensures that authentication alone is not enough — the user's relationship with the requested project is also verified.

Members do not have access to these protected management endpoints.

## Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Mapper
    ↓
Repository
    ↓
Database
```

### Controller

Handles HTTP requests and responses.

### Service

Contains business logic and authorization rules.

### Mapper

Converts between entities and DTOs without accessing repositories.

### Repository

Handles persistence and database operations using Spring Data JPA.

### Security

Handles JWT authentication, token validation, role-based authorization, and token revocation.

## Purpose

This project was built to practice and demonstrate backend development concepts including:

* REST API development
* Spring Boot architecture
* JWT authentication
* Access and refresh token management
* Spring Security
* Role-based access control
* Project-level authorization
* Service-layer authorization
* JPA and Hibernate
* DTO and mapper patterns
* Exception handling
* API documentation
* Password encryption
* Token revocation

## Status

🚧 **In Development**

Additional engineering practices and improvements will be added as the project evolves.
