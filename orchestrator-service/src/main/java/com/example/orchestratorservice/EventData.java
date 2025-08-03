package com.example.orchestratorservice;

import lombok.Data;

import java.util.Map;

@Data
public class EventData {
    private String eventType;
    private String userId;
    private Map<String, Object> data;
}
