package com.example.dispatcherservice;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DispatcherService {

    private final Map<ChannelType, ChannelDispatcher> dispatchers;

    public DispatcherService(List<ChannelDispatcher> dispatcherList) {
        this.dispatchers = dispatcherList.stream()
                .collect(Collectors.toMap(ChannelDispatcher::getChannel, Function.identity()));
    }

    public void dispatch(NotificationRequest request) {
        ChannelDispatcher dispatcher = dispatchers.get(request.getChannel());
        if (dispatcher != null) {
            dispatcher.dispatch(request);
        } else {
            throw new IllegalArgumentException("No dispatcher found for channel: " + request.getChannel());
        }
    }
}
