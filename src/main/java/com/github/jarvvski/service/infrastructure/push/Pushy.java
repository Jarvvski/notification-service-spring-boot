package com.github.jarvvski.service.infrastructure.push;

import com.github.jarvvski.service.domain.ChannelProvider;
import com.github.jarvvski.service.domain.Notification;
import com.github.jarvvski.service.domain.NotificationChannel;

public class Pushy implements ChannelProvider.NotificationSubmitter {
    @Override
    public NotificationSubmissionState submit(Notification notification, Notification.NotificationContents contents) {
        return null;
    }

    @Override
    public boolean supports(NotificationChannel.NotificationChannelType notificationChannelType) {
        return false;
    }

    @Override
    public boolean forProvider(ChannelProvider.ChannelProviderId channelProviderId) {
        return false;
    }
}
