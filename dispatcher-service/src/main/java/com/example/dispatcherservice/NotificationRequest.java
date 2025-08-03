package com.example.dispatcherservice;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationRequest {
    private String userId;
    private ChannelType channel;
    private MessageContent message;
    private Map<String, Object> metadata;
}
