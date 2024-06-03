package com.github.jarvvski.service.infrastructure.sms;

import com.github.jarvvski.service.domain.ChannelProvider;
import com.github.jarvvski.service.domain.Notification;
import com.github.jarvvski.service.domain.NotificationChannel;

public class TwilioSms implements ChannelProvider.NotificationSubmitter {

    private static final String INTERNAL_PROVIDER_ID = "twilio_sms";
    public static final ChannelProvider.ChannelProviderId TWILIO_SMS_PROVIDER_ID = new ChannelProvider.ChannelProviderId(INTERNAL_PROVIDER_ID);

    @Override
    public NotificationSubmissionState submit(Notification notification, Notification.NotificationContents contents) {
        return null;
    }

    @Override
    public boolean supports(NotificationChannel.NotificationChannelType notificationChannelType) {
        return notificationChannelType == NotificationChannel.NotificationChannelType.SMS;
    }

    @Override
    public boolean forProvider(ChannelProvider.ChannelProviderId channelProviderId) {
        return channelProviderId == TWILIO_SMS_PROVIDER_ID;
    }
}
