# 2️⃣ User Preference Service

Great! Let's **deep dive into the User Preference Service**.

---

## 🧠 Functional Requirements: User Preference Service

### 🎯 Purpose

The **User Preference Service** manages each user's notification preferences—**which channels (Email, SMS, Push, WhatsApp)** they wish to receive notifications on, and **for which event types**.

This service ensures the system **respects user opt-ins/opt-outs**, avoids spamming, and enables **fine-grained control** over notification delivery.

---

## ✅ Key Functional Requirements

### 1. **CRUD Operations for Preferences**

* Allow users (or systems on their behalf) to:

  * **Create** new preferences
  * **Read** existing preferences
  * **Update** preferences (enable/disable channels per event type)
  * **Delete** preferences (reset to defaults or remove overrides)

### 2. **Event-Channel Mapping**

* Each user should be able to specify:

  * Which **events** they want to be notified about
  * Through **which channels**

✅ Example:

```json
{
  "userId": "12345",
  "preferences": {
    "ORDER_SHIPPED": ["EMAIL", "PUSH"],
    "PASSWORD_RESET": ["SMS"]
  }
}
```

### 3. **Default vs Custom Preferences**

* If a user has **not** set a preference:

  * Use **default system-level preferences**
* Allow overrides for specific event-channel combinations

### 4. **User Profile Awareness**

* Store preferences **per user**
* Optionally group users by **locale**, **region**, or **segment** for bulk updates

### 5. **Validation Rules**

* Validate supported **channels** per event type
* Respect constraints:

  * E.g., SMS may not be allowed for marketing events

### 6. **Batch Preference Updates**

* APIs or internal jobs to update preferences **in bulk**

  * e.g., "opt out all users from WhatsApp marketing"

### 7. **Auditing & History**

* Keep a **versioned audit log** of user preference changes
* Who changed what, when, and from where (IP, UI vs API)

### 8. **Cache Layer Support**

* Read performance must be fast → consider **Redis caching**
* Cache invalidation logic when preferences are updated

---

## 🛠 Example Use Cases

| Use Case                                               | Description                                     |
| ------------------------------------------------------ | ----------------------------------------------- |
| A user disables SMS for `ORDER_SHIPPED`                | Update preferences to remove SMS for that event |
| A new user registers                                   | Create default preferences                      |
| Admin bulk disables WhatsApp for all users in EU       | Batch update preferences via admin panel or job |
| Dispatcher Service wants to know which channels to use | Reads the preference from cache or DB           |

---

## 📌 Notes

* This service is **read-heavy**, so optimize for **low latency reads**
* Changes should **immediately reflect** in downstream systems
* Event-driven support: **publish an event on preference change**

---

Let's proceed with the **Class Diagram and Data Model** for the **User Preference Service**.

---

## 🧩 Class Diagram – User Preference Service

```text
+-------------------+
|   UserPreference  |
+-------------------+
| - id              |
| - userId          |
| - eventType       |
| - channel         |
| - enabled         |
| - createdAt       |
| - updatedAt       |
+-------------------+

+---------------+       +-------------------+
|   User        |       |   PreferenceAudit |
+---------------+       +-------------------+
| - userId      |<----->| - id              |
| - name        |       | - userId          |
| - email       |       | - eventType       |
| - phone       |       | - channel         |
|               |       | - actionType      |
|               |       | - changedBy       |
|               |       | - changeSource    |
|               |       | - timestamp       |
+---------------+       +-------------------+
```

### Relationships

* `UserPreference` is uniquely identified by (`userId`, `eventType`, `channel`)
* `PreferenceAudit` tracks each preference change for compliance and rollback

---

## 🗃️ Data Model (Tables / Entities)

### 1. **user\_preferences**

| Field       | Type      | Description                          |
| ----------- | --------- | ------------------------------------ |
| id          | UUID      | Unique row ID                        |
| user\_id    | UUID      | Foreign key to `users`               |
| event\_type | VARCHAR   | e.g., ORDER\_PLACED, PASSWORD\_RESET |
| channel     | VARCHAR   | EMAIL, SMS, PUSH, WHATSAPP           |
| enabled     | BOOLEAN   | Whether this preference is enabled   |
| created\_at | TIMESTAMP | Created timestamp                    |
| updated\_at | TIMESTAMP | Last updated                         |

🟨 Composite unique index on `(user_id, event_type, channel)`

---

### 2. **preference\_audit\_log**

| Field          | Type      | Description                   |
| -------------- | --------- | ----------------------------- |
| id             | UUID      | Unique ID                     |
| user\_id       | UUID      | User whose preference changed |
| event\_type    | VARCHAR   | Event type                    |
| channel        | VARCHAR   | Channel updated               |
| action\_type   | VARCHAR   | UPDATE, CREATE, DELETE        |
| changed\_by    | VARCHAR   | SYSTEM, USER, ADMIN           |
| change\_source | VARCHAR   | API, UI, IMPORT\_JOB, etc.    |
| timestamp      | TIMESTAMP | When the change happened      |

---

Great! Let's continue with the **Sequence Diagrams** for the **User Preference Service**.

---

## 🔄 Sequence Diagrams – User Preference Service

---

### 1. **Retrieve User Preferences (GET /preferences/{userId})**

#### Scenario

When a service (or frontend app) wants to check a user's notification preferences.

```text
Client ──▶ User Preference API
          ──▶ PreferenceService
                ──▶ DB (user_preferences)
                ◀─ results
          ◀─ preferences JSON
◀─ JSON response
```

**Flow:**

1. Client sends `GET /preferences/{userId}`
2. Service fetches preferences from DB
3. Returns list of preferences by event type and channel

---

### 2. **Update User Preference (PUT /preferences/{userId})**

#### Scenario - 2

User disables push notifications for `ORDER_PLACED` event.

```text
Client ──▶ User Preference API
          ──▶ PreferenceService
                ──▶ DB: update `user_preferences`
                ──▶ DB: insert into `preference_audit_log`
                ◀─ success
          ◀─ HTTP 200 OK
◀─ confirmation
```

**Flow:**

1. Client sends a preference update
2. Service validates & updates main table
3. Change is recorded in audit log
4. Sends back success response

---

### 3. **Sync Preferences with Orchestrator/Dispatcher**

#### Scenario -3

The Orchestrator Service wants to check if a user has opted-in before sending a notification.

```text
Orchestrator ──▶ User Preference API
               ──▶ DB (user_preferences)
               ◀─ result (enabled/disabled)
◀─ proceed/skip
```

**Flow:**

1. Orchestrator queries `GET /preferences/check?userId=&eventType=&channel=`
2. Service replies with true/false
3. Orchestrator decides whether to trigger downstream notification

---

Here is the **REST API Contract (OpenAPI-style)** for the **User Preference Service**.

---

## 📘 User Preference Service – API Contract (OpenAPI-style)

```yaml
openapi: 3.0.1
info:
  title: User Preference Service
  version: 1.0.0
  description: Manage user notification preferences for different event types and channels.

paths:
  /preferences/{userId}:
    get:
      summary: Get preferences for a user
      parameters:
        - in: path
          name: userId
          required: true
          schema:
            type: string
      responses:
        '200':
          description: List of user preferences
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/UserPreference'
        '404':
          description: User not found

    put:
      summary: Update preferences for a user
      parameters:
        - in: path
          name: userId
          required: true
          schema:
            type: string
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: array
              items:
                $ref: '#/components/schemas/UserPreference'
      responses:
        '200':
          description: Preferences updated successfully
        '400':
          description: Invalid input

  /preferences/check:
    get:
      summary: Check if a user has opted-in to a specific notification
      parameters:
        - in: query
          name: userId
          required: true
          schema:
            type: string
        - in: query
          name: eventType
          required: true
          schema:
            type: string
        - in: query
          name: channel
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Opt-in status
          content:
            application/json:
              schema:
                type: object
                properties:
                  optedIn:
                    type: boolean
        '404':
          description: No preferences found

components:
  schemas:
    UserPreference:
      type: object
      properties:
        eventType:
          type: string
          example: ORDER_PLACED
        channel:
          type: string
          enum: [EMAIL, SMS, PUSH]
        enabled:
          type: boolean
```

---

## Error Handling & Validation Logic – User Preference Service

### 1. Validation Rules

#### PUT /preferences/{userId}

* `userId`: Must not be null or blank. Should follow UUID or internal ID format.
* Request body must be:
  * An array with at least one element.
  * Each item must contain:
    * `eventType`: Must be a known event, e.g. `USER_REGISTERED`, `ORDER_PLACED`.
    * `channel`: Must be one of `EMAIL`, `SMS`, `PUSH`.
    * `enabled`: Must be a boolean value.

**Examples of Invalid Requests:**

```json
// Missing eventType
[
  {
    "channel": "EMAIL",
    "enabled": true
  }
]
// Invalid channel
[
  {
    "eventType": "ORDER_PLACED",
    "channel": "FAX",
    "enabled": true
  }
]
```

#### GET /preferences/{userId}

* `userId`: Must be a valid and existing identifier.

**GET /preferences/check?userId=...**

* `userId`: Required and must be valid.
* `eventType`: Required and must be known.
* `channel`: Must be a supported value.

### 2. Common Error Response Schema

```json
{
  "timestamp": "2025-08-05T10:20:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid channel value: FAX",
  "path": "/preferences/123"
}
```

### 3. Possible HTTP Status Codes

| Status Code | Meaning                         | Scenarios                                              |
|-------------|----------------------------------|--------------------------------------------------------|
| 200         | OK                               | Valid request with successful result                   |
| 201         | Created                          | Not applicable here (no create endpoint)               |
| 400         | Bad Request                      | Validation errors, missing fields, wrong types         |
| 404         | Not Found                        | No preferences found for user                          |
| 409         | Conflict                         | Conflicting updates (rare for this service)            |
| 500         | Internal Server Error            | Unexpected failures in downstream (DB/network issues)  |

### 4. Error Handling Strategy

* **Validation errors**: Caught at controller level using `@Valid`, `@ControllerAdvice`, and `@ExceptionHandler`.
* **Business errors**: (e.g., unknown userId) mapped to 404 with descriptive message.
* **Unexpected errors**: Logged in detail, user-facing message kept generic for security.

### 5. Security Considerations

* Ensure only authenticated users can read/write preferences.
* Prevent access to another user’s preferences by enforcing identity match or role-based control.
