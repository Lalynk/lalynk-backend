# lalynk Backend

Backend service for Lalynk, a secure secret-sharing platform.

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- PostgreSQL
- Flyway
- Docker
- Auth0

## Overview
The backend provides the REST API for creating and retrieving one-time secrets.
It handles authentication, authorization, encryption and persistence.

## Architecture

The backend is structured as a modular monolith using Spring Modulith.

## Configuration

Configuration is provided through environment variables.
See `.env.example` for the required variables.

## Related Repository

See the [Lalynk Frontend](https://github.com/Lalynk/lalynk-frontend) repository for the frontend implementation.