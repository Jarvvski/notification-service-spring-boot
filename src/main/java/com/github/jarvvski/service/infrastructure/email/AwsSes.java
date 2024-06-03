package com.github.jarvvski.service.infrastructure.email;

import com.github.jarvvski.service.domain.ChannelProvider;
import com.github.jarvvski.service.domain.Notification;
import com.github.jarvvski.service.domain.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AwsSes implements ChannelProvider.NotificationSubmitter {
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
