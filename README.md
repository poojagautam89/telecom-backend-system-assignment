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

# Flyway Migrations (Assignment Requirement)
25-Nov-2025 - Sprint-1 (V1 Migration)
**V1__create_subscriber_table.sql**
- Created `subscribers` table
- Structure exactly matches Assignment class diagram

## Sprint-2 work start
26-Nov-2025 Sprint-2 (V2 Migration)
**V2__create_sim_and_plan.sql**
- Created `plan` table
- Created `sim` table
- Added required foreign keys:
    - SIM → Subscriber
    - SIM → Plan
      - SIM → Subscriber = Many-to-One
        (One subscriber can have multiple SIMs)
      - SIM → Plan = Many-to-One
        (One plan can be assigned to multiple SIMs)

    - Added basic indexes
    - Migration executed successfully on NeonDB
## Sprint-2 – Work Completed
**27 Nov 2025 — SIM Activation Module**
- Implemented SIM activation API
- Added SIM entity, DTO, repository
- Added Service + ServiceImpl
- Implemented SIM lifecycle states (ACTIVE / BLOCKED)

**28-Nov-2025 — Plan Module Completed**
- Implemented Plan CRUD APIs
- Added plan allowances (DATA / SMS / CALL)
- Added API to assign a plan to SIM


**01-Dec-2025 — Sprint-2 Documentation**
- Updated Postman collection
- Added tests for SIM and Plan services

## Sprint-3 Work Start (Up to 02-Dec-2025)
**02-Dec-2025 — Usage Tracking Module (V3 Migration)**
- Created `usage` table (CALL / DATA / SMS)
- Added foreign key: Usage → SIM
- Created Usage Entity, DTO, Repository
- Added API to log telecom usage

**03-Dec-2025 — Billing Engine Development**
- Created Billing Entity, DTO, Repository
- Implemented billing calculation logic:
    - Extra Data = ₹10/GB
    - Extra SMS = ₹0.5/SMS
    - Extra Call Minutes = ₹1/min
- Added BillingService to calculate monthly bills

**04-Dec-2025 — Billing APIs Completed**
- Added billing APIs
- Tested billing logic with sample usage
- Updated Swagger documentation for Billing module
**05-Dec-2025** — All Subscriber, SIM, Plan, Usage, and Billing APIs were fully tested in Postman
- Verified request/response correctness for each module
- Ensured all endpoints are working as expected
- Checked validation, error handling, and success flows
- Postman collection updated with all Sprint-3 APIs

