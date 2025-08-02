# 📌 Notification Service – Summary of Requirements

## 🗂️ Services Overview

This system follows a microservices architecture and includes the following core services:

1. **Template Service** – Manages channel-specific, localized templates with placeholders.
2. **User Preference Service** – Stores user preferences for communication channels and categories.
3. **Event Processor Service** – Ingests and normalizes events from Kafka topics.
4. **Orchestrator Service** – Coordinates preferences, templates, and generates messages.
5. **Dispatcher Service** – Sends messages via email, SMS, or push channels.
6. **Tracking Service** – Tracks and logs delivery and status events.

---

## 📁 Folder Structure (Suggested)
```bash
notification-service/
├── template-service/
├── user-preference-service/
├── event-processor-service/
├── orchestrator-service/
├── dispatcher-service/
├── tracking-service/
└── shared-libraries/
```

---

## ⚙️ Technologies Used

| Layer               | Technology Stack                            |
|---------------------|---------------------------------------------|
| Language            | Java 17                                     |
| Framework           | Spring Boot, Spring Web, Spring Data JPA    |
| Messaging Queue     | Apache Kafka                                |
| Database            | PostgreSQL                                  |
| Template Engine     | Freemarker / Handlebars                     |
| Caching             | Redis                                       |
| API Documentation   | Swagger / OpenAPI                           |
| Configuration       | Spring Cloud Config                         |
| Containerization    | Docker, Kubernetes                          |
| Monitoring & Logs   | Prometheus, Grafana, ELK stack              |
| CI/CD               | GitHub Actions / Jenkins                    |

---

## 🔁 Event Generators

The system consumes events from two main Kafka topics:

- **user-events**
  - `USER_REGISTERED`
  - `PASSWORD_RESET`

- **order-events**
  - `ORDER_PLACED`
  - `ORDER_SHIPPED`

---

## 🧩 Integration

Each service exposes its own APIs (documented in OpenAPI) and interacts through REST, Kafka events, and internal HTTP calls.

> Generated on 2025-08-02 17:03:30
