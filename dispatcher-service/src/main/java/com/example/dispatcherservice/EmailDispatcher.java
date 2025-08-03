package com.example.dispatcherservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailDispatcher implements ChannelDispatcher {

    @Override
    public void dispatch(NotificationRequest request) {
        log.info("Sending email to {}: Subject: {}, Body: {}",
                request.getUserId(), request.getMessage().getSubject(), request.getMessage().getBody());
        // In a real implementation, this would use a mail sender to send the email
    }

    @Override
    public ChannelType getChannel() {
        return ChannelType.EMAIL;
    }
}
