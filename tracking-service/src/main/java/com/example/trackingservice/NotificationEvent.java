package com.example.trackingservice;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "notification_events")
public class NotificationEvent {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private UUID notificationId;

    @Column(nullable = false)
    private String eventType;

    @Column(updatable = false)
    private LocalDateTime timestamp;

    @Lob
    private String metadata;

    @PrePersist
    protected void onCreate() {
        eventId = UUID.randomUUID();
        timestamp = LocalDateTime.now();
    }
}
