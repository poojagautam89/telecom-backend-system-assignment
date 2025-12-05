# Telecom Backend – Docker Run Guide

This document explains how to run the **Telecom Subscriber Billing API** using **Docker** and **Docker Compose**.  
It is designed to make local development and testing easy and consistent across environments.

---

# 1. Prerequisites

Before running the application in Docker, ensure the following are installed:

- Docker Desktop / Docker Engine
- Docker Compose
- Java 17 (only needed for building the JAR)
- Maven

---

# 2. Build the JAR (Required before Docker build)

Run the following command from the project root:
```on git bash
- mvn clean package -DskipTests
- docker-compose up --build

