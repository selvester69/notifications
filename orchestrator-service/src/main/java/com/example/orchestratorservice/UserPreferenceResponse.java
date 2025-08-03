package com.example.orchestratorservice;

import lombok.Data;

@Data
public class UserPreferenceResponse {
    private ChannelType channel;
    private boolean isEnabled;
}
