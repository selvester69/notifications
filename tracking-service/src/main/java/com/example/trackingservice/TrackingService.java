package com.example.trackingservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final NotificationEventRepository eventRepository;

    public void trackEvent(NotificationEvent event) {
        eventRepository.save(event);
    }
}
