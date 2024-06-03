package com.github.jarvvski.service.domain;

import lombok.RequiredArgsConstructor;

import java.util.PriorityQueue;

@RequiredArgsConstructor
public class NotificationChannel {

    // TODO: Could be abstracted into a persisted type, so you can configure Channel types
    //  via config change, instead of code change with this enum
    public enum NotificationChannelType {
        EMAIL,
        SMS,
        PUSH;
    }

    private final NotificationChannelType notificationChannelType;
    private final PriorityQueue<ChannelProvider> providerWeightedQueue;
}
