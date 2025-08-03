package com.example.dispatcherservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dispatch")
@RequiredArgsConstructor
public class DispatcherController {

    private final DispatcherService dispatcherService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void dispatch(@RequestBody NotificationRequest request) {
        dispatcherService.dispatch(request);
    }
}
