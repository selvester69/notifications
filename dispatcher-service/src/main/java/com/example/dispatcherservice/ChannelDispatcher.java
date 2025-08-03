package com.example.dispatcherservice;

public interface ChannelDispatcher {
    void dispatch(NotificationRequest request);
    ChannelType getChannel();
}
