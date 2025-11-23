# Telecom Backend System

**18-Nov-2025 — Completed Spring Boot project setup and NeonDB integration**

## Project Overview
This backend application is part of the Telecom Subscriber & Billing System.
It is built using Spring Boot (Java 17) and uses NeonDB (PostgreSQL) as the cloud database.

## Sprint 1 – Work Completed
- Spring Boot project created using Spring Initializr
- Java 17 configured and validated
- Git repository initialized
- Integrated NeonDB with the application
- Application successfully starts on port 8080

## Database
- NeonDB (PostgreSQL)
- Connection properties stored in `application.properties`

## Tech Stack
- Java 17
- Spring Boot 3.5
- PostgreSQL (NeonDB)
- Maven  

**19-Nov-2025 — Completed Spring Boot project setup and NeonDB integration**

## Swagger-UI
**20-Nov-2025 — Added Swagger & Validation Setup**
- Added Swagger/OpenAPI documentation using Springdoc
- Created OpenApiConfig for API documentation
- Updated pom.xml with Swagger/OpenAPI dependencies
- Added validation support using spring-boot-starter-validation
- Verified Swagger UI loads correctly at /swagger-ui/index.html
- 
## Postman and adding logs
**21-Nov-2025 — Implemented Subscriber Module & Completed Sprint-1**
- Edit implemented complete Subscriber CRUD Module
- Entity, DTOs, Repository
- Service + ServiceImpl
- Controller with REST endpoints
- Added logging using @Slf4j
- Added Global Exception Handling (GlobalExceptionHandler)
- Added temporary SecurityConfig to allow Swagger/API access
- Added JUnit tests for SubscriberServiceImpl
- Added Postman collection for Subscriber APIs
- Sprint-1 subscriber functionality fully completed