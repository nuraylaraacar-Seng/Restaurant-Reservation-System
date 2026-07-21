# Restaurant Reservation System

A backend-focused restaurant reservation system developed with **Java Spring Boot**.
The project provides user authentication, table management, reservation creation and conflict prevention with a clean layered architecture approach.

The main goal of this project is to design a maintainable backend system while applying real-world backend development practices such as JWT security, DTO mapping, validation, exception handling and database concurrency control.

---

# Architecture Overview

The project architecture and design decisions are documented with UML and Mermaid diagrams.

The `Architecture` folder contains **10 UML and Mermaid diagrams**.
The diagrams are separated into different views to keep complex structures readable and understandable.

## System Architecture

<p align="center">
  <img src="./Architecture/UML%20Diagrams/arc-2.png" width="100%">
</p>

## Architecture Documentation

<p align="center">
  <b>
  UML and Mermaid diagrams include system structure, package organization,
  entity relationships and reservation flow details.
  </b>
</p>

---

# Features

## Authentication & Authorization

* User registration
* User login
* JWT based authentication
* RSA asymmetric key encryption for JWT signing
* BCrypt password hashing
* Stateless security architecture

## Table Management

* Create restaurant tables
* List available tables
* Update table information
* Delete tables
* Table capacity management
* Active/inactive table status

## Reservation Management

* Create reservations
* Prevent overlapping reservations
* Validate guest capacity
* Reservation status management

  * CONFIRMED
  * CANCELLED
  * COMPLETED

## Error Handling

* Global exception handling
* Custom business exceptions
* Standardized error responses
* Validation error management

---

# Technology Stack

## Backend

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Hibernate

## Database

* PostgreSQL

## Security

* JWT
* RSA Public/Private Key Authentication
* BCrypt Password Encoder

## Development Tools

* Maven
* Lombok
* MapStruct
* JUnit 5
* Mockito
* Swagger / OpenAPI

---

# Architecture Design

The project follows a layered architecture approach with clear separation between responsibilities.

```
src/main/java

├── application
│   ├── dto
│   ├── mapper
│   └── service
│
├── domain
│   ├── entity
│   ├── enums
│   └── exception
│
├── infrastructure
│   ├── persistence
│   └── security
│
└── presentation
    ├── controller
    └── advice
```

## Layer Responsibilities

### Domain Layer

Contains the core business models and rules.

Includes:

* Entities
* Enums
* Domain exceptions
* Business validations

### Application Layer

Handles application workflows.

Includes:

* DTO objects
* Service classes
* Entity mapping

### Infrastructure Layer

Contains technical implementations.

Includes:

* Database repositories
* JWT configuration
* Security configuration

### Presentation Layer

Responsible for external communication.

Includes:

* REST Controllers
* Exception handling
* Web endpoints

---

# Reservation Conflict Prevention

One of the main challenges of reservation systems is preventing double booking.

This project handles concurrent reservation requests by using database locking mechanisms.

Implementation:

* `Pessimistic Lock`
* Transaction management
* Conflict checking before reservation creation

Reservation flow:

```
User Request
      |
      v
Check Table Availability
      |
      v
Lock Database Record
      |
      v
Check Existing Reservations
      |
      v
Create Reservation
```

This prevents multiple users from reserving the same table at the same time.

---

# Security Design

Authentication flow:

```
User
 |
 | Login Request
 v
Auth Controller
 |
 v
Auth Service
 |
 v
JWT Generation
 |
 v
Access Token
```

JWT tokens are generated using RSA key pairs:

* Private key → Token signing
* Public key → Token verification

The application uses stateless authentication without server-side sessions.

---

# Database Model

Main entities:

```
User

 |
 | 1:N

Reservation

 |
 | N:1

RestaurantTable
```

Entities:

* User
* RestaurantTable
* Reservation

Common fields such as:

* id
* createdAt
* updatedAt

are managed through a shared `BaseEntity`.

---

# API Documentation

Swagger UI is available for testing and exploring REST endpoints.

Main endpoints:

## Authentication

```
POST /api/auth/register
POST /api/auth/login
```

## Tables

```
GET    /api/tables
POST   /api/tables
PUT    /api/tables/{id}
DELETE /api/tables/{id}
```

## Reservations

```
POST /api/reservations
```

---

# Testing

Unit tests are implemented using:

* JUnit 5
* Mockito

Test scenarios include:

* Successful reservation creation
* Reservation conflict detection
* Missing table validation
* Capacity validation

Example:

```
createReservation_ShouldReturnResponse_WhenEverythingIsValid()

createReservation_ShouldThrowConflictException_WhenTableIsAlreadyBooked()

createReservation_ShouldThrowBusinessException_WhenCapacityIsInsufficient()
```

---

# Project Structure

```
restaurant-reservation

├── application
├── domain
├── infrastructure
├── presentation
├── resources
└── tests
```

---

# Future Improvements

Possible improvements:

* Role based endpoint authorization
* Reservation update and cancellation APIs
* Pagination support
* Docker deployment
* CI/CD pipeline
* Integration tests with Testcontainers
* Refresh token mechanism

---

# Author

**Nuray Lara Açar**

Software Engineering Student
Backend Developer focused on Java & Spring Boot
