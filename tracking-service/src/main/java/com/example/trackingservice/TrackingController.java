package com.example.trackingservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void trackEvent(@RequestBody NotificationEvent event) {
        trackingService.trackEvent(event);
    }
}
