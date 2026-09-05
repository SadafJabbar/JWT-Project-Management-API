# Spring Boot JWT Project Management API

A secure RESTful Project Management API built with **Java and Spring Boot**, focused on **JWT-based authentication, role-based authorization, and project-level access control**.

The API allows administrators and project managers to manage users, projects, memberships, and tasks while enforcing authorization based on both user roles and project ownership.

## Features

* JWT-based authentication
* Stateless authentication with Spring Security
* Role-based authorization
* Project-level authorization for managers
* User management
* Project management
* Project membership management
* Task management
* Password encryption using BCrypt
* JWT token revocation through token blacklisting
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
| MEMBER  | Access regular project functionality                    |

Authentication is handled using **JWT tokens**.

After successful login, the client receives a JWT and sends it with protected requests:

```text
Authorization: Bearer <JWT>
```

Spring Security validates the token and establishes the authenticated user.

Authorization is then enforced at two levels:

1. **Role-based authorization** through Spring Security
2. **Project ownership authorization** through the service layer

For example, a MANAGER can only manage tasks belonging to the project assigned to that manager.

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

## Authentication

### Login

```http
POST /api/v1/auth/Login?username={username}&password={password}
```

A successful login returns a JWT token.

Use the token for protected endpoints:

```http
Authorization: Bearer <JWT>
```

### Logout

```http
POST /api/v1/auth/Logout
```

The JWT is added to the token blacklist and can no longer be used for authentication.

## Main API Areas

### Authentication

```text
POST /api/v1/auth/Login
POST /api/v1/auth/Logout
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
* Spring Boot
* Spring Security
* JWT
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
    ├── controllers
    ├── dtos
    ├── entities
    ├── enums
    ├── exceptions
    ├── mapper
    ├── repositories
    ├── security
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
jwt.expiration=86400000
```

Do not commit real passwords or JWT secrets to the repository.

## Running the Application

Clone the repository:

```bash
git clone <repository-url>
```

Navigate into the project:

```bash
cd Spring-Boot-JWT-Project-Management-API
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

OpenAPI documentation is available at:

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

## Purpose

This project was built to practice and demonstrate backend development concepts including:

* REST API development
* Spring Boot architecture
* JWT authentication
* Spring Security
* Role-based access control
* Service-layer authorization
* JPA and Hibernate
* DTO and mapper patterns
* Exception handling
* API documentation
* Automated testing
* Performance and load testing

## Status

🚧 **In Development**

Additional improvements and engineering practices are being added as the project evolves.
