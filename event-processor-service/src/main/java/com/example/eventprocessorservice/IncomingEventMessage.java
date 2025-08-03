package com.example.eventprocessorservice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class IncomingEventMessage {

    private UUID eventId;
    private EventType eventType;
    private EventSource source;
    private Map<String, Object> payload;
    private LocalDateTime receivedAt;
}
