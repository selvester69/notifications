# 📉 Notification Service – Low-Level Design (LLD)

## 🧱 Microservices Covered
1. Template Service
2. User Preference Service
3. Event Processor Service
4. Orchestrator Service
5. Dispatcher Service
6. Tracking Service

---

## 1️⃣ Template Service

### 📦 Class Diagram (Key Entities)
- `TemplateController`
- `TemplateService`
- `TemplateRepository`
- `TemplateEntity`
- `TemplateMapper`
- `TemplateDTO`

### 📓 Data Model
```java
@Entity
@Table(name = "templates")
public class TemplateEntity {
    UUID id;
    String name;
    String language;
    Channel channel; // EMAIL, SMS, PUSH
    String content;
    List<String> placeholders;
    Instant createdAt;
}
```

### 📈 Sequence Diagram (Create Template)
1. `Client` → `POST /templates`
2. Controller parses request → DTO
3. DTO → Entity → Persist via Repository
4. Return 201 Created with Template ID

### 🔗 APIs
```http
POST /templates
GET /templates/{id}
GET /templates?channel=email&language=en
PUT /templates/{id}
DELETE /templates/{id}
```

### 📄 Sample API Request
```json
{
  "name": "Password Reset",
  "channel": "EMAIL",
  "language": "en",
  "content": "Hi {{name}}, reset your password using {{resetLink}}",
  "placeholders": ["name", "resetLink"]
}
```

---

## 2️⃣ User Preference Service

### 📦 Class Diagram (Key Entities)
- `UserPreferenceController`
- `UserPreferenceService`
- `UserPreferenceRepository`
- `UserPreferenceEntity`
- `UserPreferenceDTO`

### 📓 Data Model
```java
@Entity
@Table(name = "user_preferences")
public class UserPreferenceEntity {
    UUID id;
    UUID userId;
    Channel channel; // EMAIL, SMS, PUSH
    String category; // ORDER, PROMOTION, etc.
    boolean optedIn;
    Instant updatedAt;
}
```

### 📈 Sequence Diagram (Update Preferences)
1. `Client` → `PUT /preferences/{userId}`
2. Controller parses preferences
3. Service validates + persists
4. Return 200 OK

### 🔗 APIs
```http
GET /preferences/{userId}
PUT /preferences/{userId}
```

### 📄 Sample API Request
```json
{
  "preferences": [
    {"channel": "EMAIL", "category": "ORDER", "optedIn": true},
    {"channel": "SMS", "category": "ORDER", "optedIn": false}
  ]
}
```

---

## 3️⃣ Event Processor Service

### 📦 Class Diagram (Key Entities)
- `EventProcessor`
- `EventValidator`
- `EventNormalizer`
- `OrchestratorClient`
- `IncomingEventMessage`

### 🧩 Event Enums
```java
enum EventType {
  USER_REGISTERED, PASSWORD_RESET, ORDER_PLACED, ORDER_SHIPPED;
}

enum EventSource {
  USER_SERVICE, ORDER_SERVICE;
}
```

### 📓 Data Model – Optional Audit Table
```sql
Table: event_log
-----------------------------------
event_id     UUID PRIMARY KEY
event_type   VARCHAR(50)
source       VARCHAR(50)
payload      JSONB
received_at  TIMESTAMP
```

### 📈 Sequence Diagram (Kafka → Orchestrator)
```plaintext
[Kafka] → [Event Processor]: receives USER_REGISTERED event
[Event Processor] → [Validator]: validate schema & payload
[Validator] → [Event Processor]: OK
[Event Processor] → [Orchestrator Service]: POST /notifications/trigger with normalized payload
```

### 🔗 Internal API (For Testing/Replay)
```http
POST /events/replay
{
  "eventType": "ORDER_PLACED",
  "payload": {
    "userId": "123",
    "orderId": "ABC123"
  }
}
```

### 🔄 Kafka Integration
- Topics:
  - `user-events`: USER_REGISTERED, PASSWORD_RESET
  - `order-events`: ORDER_PLACED, ORDER_SHIPPED
- Workflow:
  1. Validate event
  2. Normalize structure
  3. Call Orchestrator with structured notification payload

---

## 4️⃣ Orchestrator Service

### 📦 Class Diagram
- `NotificationOrchestrator`
- `PreferenceServiceClient`
- `TemplateServiceClient`
- `DispatcherClient`
- `NotificationRequest`
- `ResolvedNotification`

### 📓 Data Model (In-Memory Only)
```java
class NotificationRequest {
  EventType eventType;
  Map<String, Object> payload;
}

class ResolvedNotification {
  UUID userId;
  Channel channel;
  String renderedMessage;
  String destination;
}
```

### 📈 Sequence Diagram
```plaintext
[Event Processor] → [Orchestrator]
[Orchestrator] → [Preference Service] → fetch user preference
[Orchestrator] → [Template Service] → fetch and render template
[Orchestrator] → [Dispatcher] → deliver final message
```

### 🔗 APIs
```http
POST /notifications/trigger
```

### 📄 Sample Trigger Payload
```json
{
  "eventType": "USER_REGISTERED",
  "payload": {
    "userId": "123",
    "name": "Aman"
  }
}
```

---

## 5️⃣ Dispatcher Service

### 📦 Class Diagram
- `DispatcherController`
- `EmailSender`
- `SmsSender`
- `PushSender`
- `ChannelType`

### 📓 Data Model (Log Table)
```sql
Table: delivery_log
---------------------------
id           UUID PRIMARY KEY
user_id      UUID
channel      VARCHAR(20)
status       VARCHAR(20)
destination  TEXT
message      TEXT
timestamp    TIMESTAMP
```

### 📈 Sequence Diagram
```plaintext
[Orchestrator] → [Dispatcher] POST /dispatch
[Dispatcher] → [ChannelSender] (e.g., EmailSender)
Sender → External System
Log status to DB
```

### 🔗 APIs
```http
POST /dispatch
```

### 📄 Dispatch Payload
```json
{
  "userId": "123",
  "channel": "EMAIL",
  "destination": "aman@email.com",
  "message": "Hi Aman, welcome!"
}
```

---

## 6️⃣ Tracking Service

### 📦 Class Diagram
- `TrackingController`
- `TrackingService`
- `TrackingRepository`

### 📓 Data Model
```sql
Table: tracking_events
-----------------------------
id         UUID PRIMARY KEY
event      VARCHAR(50)
status     VARCHAR(20)
details    JSONB
created_at TIMESTAMP
```

### 📈 Sequence Diagram
```plaintext
[Dispatcher] → [Tracking Service]: send delivery + status info
[Tracking Service] → DB: log tracking event
```

### 🔗 APIs
```http
POST /track
GET /track/{userId}
```

### 📄 Sample Tracking Event
```json
{
  "event": "EMAIL_DELIVERED",
  "status": "SUCCESS",
  "details": {
    "userId": "123",
    "timestamp": "2025-08-02T12:34:56Z"
  }
}
```
