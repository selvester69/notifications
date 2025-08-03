Great — for a **Notification Service at scale**, it's essential to define strong **Non-Functional Requirements (NFRs)**. Here's a comprehensive list of NFRs you can consider and categorize in your architecture/design document:

---

## 🚀 Non-Functional Requirements for Notification Service

### 1. **Scalability**

* The service should horizontally scale to handle millions of events per day.
* Kafka consumers and dispatchers should be independently scalable per channel (e.g., email, SMS, push).
* Use of stateless services to allow load balancing and scaling via Kubernetes or auto-scaling groups.

### 2. **Availability**

* Target availability: 99.9% (or higher).
* Deploy services in multiple availability zones.
* Retry mechanisms for downstream failures (e.g., mail gateway timeout).
* Use circuit breakers to avoid cascading failures.

### 3. **Latency**

* End-to-end latency from event ingestion to delivery should be within:

  * ⏱️ < 500ms for SMS/Push notifications.
  * ⏱️ < 2 seconds for emails.

### 4. **Throughput**

* Capable of processing **at least 10K+ events per second**.
* Kafka partitioning and batching should be optimized for high throughput.

### 5. **Reliability**

* Use dead-letter queues (DLQs) for failed notifications.
* Persist delivery status in tracking DB for auditing.
* Ensure at-least-once event delivery using Kafka offset management.

### 6. **Observability**

* Centralized logging using tools like ELK or Grafana Loki.
* Metrics on:

  * Events received/processed per second
  * Delivery success/failure per channel
  * Latency and retry counts
* Tracing via OpenTelemetry for E2E visibility across microservices.

### 7. **Security**

* API access secured via OAuth2 or JWT-based authentication.
* Role-based access control (RBAC) for admin functions (template editing, config changes).
* PII (email, phone) encryption at rest and in transit.
* Use HTTPS/TLS for all service-to-service communication.

### 8. **Data Retention & Archiving**

* Retain delivery logs and tracking data for 30–90 days (configurable).
* Archive older records to object storage like S3 or Glacier.

### 9. **Compliance**

* GDPR/CCPA compliance:

  * Consent management (via user preferences)
  * Data erasure support
* Audit logging for template and preference changes

### 10. **Extensibility**

* Support for adding new channels (e.g., WhatsApp, Slack) without major changes.
* Template placeholders and rendering engine should support complex conditional logic.

### 11. **Disaster Recovery**

* Backup template and preference DBs daily.
* Cross-region replication for critical services like Kafka and PostgreSQL.
* Failover strategies documented and tested.

### 12. **Maintainability**

* Microservices should be modular and loosely coupled.
* Clear API documentation via Swagger/OpenAPI.
* Well-commented codebase with automated testing and CI/CD pipelines.

Here is the **NFR-to-service mapping section** you've asked for, already appended at the end of your Low-Level Design document:

---

## 📊 Non-Functional Requirements (NFRs) – Service Mapping

| **NFR**               | **Applies To**                                   | **Notes**                                                                |
| --------------------- | ------------------------------------------------ | ------------------------------------------------------------------------ |
| **Scalability**       | Event Processor, Dispatcher, Orchestrator        | Kafka partitions for scaling; use container auto-scaling for Dispatchers |
| **Availability**      | All services                                     | Deploy on multi-AZ setups; retry logic and circuit breakers              |
| **Latency**           | Dispatcher, Orchestrator                         | Asynchronous I/O; parallel processing of template + preferences          |
| **Throughput**        | Event Processor, Dispatcher                      | Optimize Kafka consumer groups and batch sends                           |
| **Reliability**       | Dispatcher, Event Processor, Tracking            | Retry policies, DLQ, persistent logging                                  |
| **Observability**     | All services                                     | Centralized logging (ELK), tracing (Jaeger), and metrics (Prometheus)    |
| **Security**          | API services (Template, Preferences, Dispatcher) | JWT auth, HTTPS, encryption of sensitive fields                          |
| **Data Retention**    | Tracking Service, Delivery Logs                  | Archive or clean logs older than X days                                  |
| **Compliance**        | Preference, Template, Tracking                   | GDPR/CCPA readiness: consent, traceability, and deletion support         |
| **Extensibility**     | Orchestrator, Template Service                   | Plug-in architecture to support new channels or templates dynamically    |
| **Disaster Recovery** | Kafka, all PostgreSQL services                   | Snapshots, WAL archiving, regional backups                               |
| **Maintainability**   | All services                                     | Modular structure, API contracts, CI/CD integration                      |


