package com.example.orchestratorservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageContent {
    private String subject;
    private String body;
}
