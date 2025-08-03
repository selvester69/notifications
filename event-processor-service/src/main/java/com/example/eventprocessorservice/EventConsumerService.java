package com.example.eventprocessorservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumerService {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"user-events", "order-events"}, groupId = "notification-event-processor")
    public void consumeEvent(String message) {
        try {
            IncomingEventMessage event = objectMapper.readValue(message, IncomingEventMessage.class);
            log.info("Received event: {}", event);
            // In a real implementation, this would call the Orchestrator Service
        } catch (Exception e) {
            log.error("Error processing event: {}", message, e);
        }
    }
}
