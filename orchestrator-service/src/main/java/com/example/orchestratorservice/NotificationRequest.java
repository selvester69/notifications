package com.example.orchestratorservice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class NotificationRequest {
    private String userId;
    private ChannelType channel;
    private MessageContent message;
    private Map<String, Object> metadata;
}
