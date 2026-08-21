# Restaurant Reservation System
A backend-focused restaurant reservation system developed with **Java Spring Boot**.
The project provides user authentication, table management, reservation creation and conflict prevention with a clean layered architecture approach.

The main goal of this project is to design a maintainable backend system while applying real-world backend development practices such as JWT security, DTO mapping, validation, exception handling and database concurrency control.

<img width="1918" height="935" alt="Ekran görüntüsü 2026-08-20 145113" src="https://github.com/user-attachments/assets/990d28ed-c8b9-4cda-ac9c-6fc06e2a6c54" />

<img width="907" height="663" alt="Ekran görüntüsü 2026-08-20 185200" src="https://github.com/user-attachments/assets/993bce35-6f08-40cc-912a-feb8438a8a87" />

<img width="924" height="467" alt="Ekran görüntüsü 2026-08-20 185211" src="https://github.com/user-attachments/assets/a6e2c7f5-81fa-4bdc-93ee-6fa7fde2a51f" />

<img width="909" height="504" alt="Ekran görüntüsü 2026-08-20 185223" src="https://github.com/user-attachments/assets/7ad210b8-12a5-4c5d-8f82-8c4320d178d3" />

---

# Architecture Overview

The project architecture and design decisions are documented with UML and Mermaid diagrams.

The `Architecture` folder contains **10 UML and Mermaid diagrams**.
The diagrams are separated into different views to keep complex structures readable and understandable.

## System Architecture

<p align="center">
  <img src="./Architecture/UML%20Diagrams/Arc-2.png" width="100%">
</p>

<p align="center">
  <img src="./Architecture/Mermaid%20Diagrams/Arc-1.png" width="100%">
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
* Role-Based Access Control (RBAC) for endpoint authorization
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

```text
Restaurant-Reservation-System

├── Architecture
│   ├── Mermaid Diagrams
│   └── UML Diagrams
│
├── src/main/java
│
│   ├── application
│   │   ├── dto
│   │   ├── mapper
│   │   └── service
│   │
│   ├── domain
│   │   ├── entity
│   │   ├── enums
│   │   └── exception
│   │
│   ├── infrastructure
│   │   ├── persistence
│   │   └── security
│   │
│   └── presentation
│       ├── controller
│       └── advice
│
├── pom.xml
└── README.md
