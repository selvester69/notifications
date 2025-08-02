
# 📐 Notification Service – High-Level Design (HLD)

## 🧭 Overview
The Notification Service is responsible for sending user-facing communications (Email, SMS, Push) based on system events such as `USER_REGISTERED`, `ORDER_PLACED`, etc. It is built using a microservices architecture and follows an event-driven pattern.

## 🎯 Functional Requirements
- Receive domain events (`USER_REGISTERED`, `ORDER_PLACED`) via Kafka.
- Determine eligible users and their preferences.
- Fetch and render templates for each event.
- Dispatch messages via configured channels (Email, SMS, Push).
- Track delivery status of notifications.
- Allow user-specific preferences per channel and category.
- Allow dynamic template management per channel & language.

## 🏗️ Architecture Overview

### 🔌 Event Sources
- `user-events`: `USER_REGISTERED`, `PASSWORD_RESET`
- `order-events`: `ORDER_PLACED`, `ORDER_SHIPPED`

### 🧩 Microservices
1. **Template Service** – Manages templates per channel/language.
2. **User Preference Service** – Stores and fetches per-user notification preferences.
3. **Event Processor Service** – Kafka consumer, validates and normalizes events.
4. **Orchestrator Service** – Core service; orchestrates preference + template + dispatch flow.
5. **Dispatcher Service** – Sends actual notifications through Email, SMS, Push.
6. **Tracking Service** – Logs delivery status of messages.

### 🔁 Data Flow

```plaintext
Event Source (Kafka) → Event Processor → Orchestrator
   → Preference Service
   → Template Service
   → Dispatcher → External Channels
   → Tracking Service
```

### 📦 Tech Stack
- **Language:** Java + Spring Boot
- **Async Messaging:** Kafka
- **Database:** PostgreSQL
- **Template Engine:** FreeMarker or Thymeleaf
- **API Docs:** OpenAPI (Swagger)
- **Infra:** Kubernetes, Docker, AWS (S3 for attachments)

## 🧱 Non-Functional Requirements
- Scalable: Horizontal scaling for Event Processor and Dispatcher.
- Reliable: Kafka with retry and dead-letter queues.
- Secure: Authenticated APIs, secure credentials via Vault or Secrets Manager.
- Observability: Logs, metrics, distributed tracing via OpenTelemetry.

## 🧠 Design Principles
- Bounded context per service.
- Idempotent event processing.
- Asynchronous processing.
- Configurable templates and preferences.
- Circuit breaker on external channel APIs.

