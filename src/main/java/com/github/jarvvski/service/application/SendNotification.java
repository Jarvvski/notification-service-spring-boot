package com.github.jarvvski.service.application;

import com.github.jarvvski.service.configuration.MetricRecorder;
import com.github.jarvvski.service.configuration.TaskConfiguration;
import com.github.jarvvski.service.domain.ChannelProvider;
import com.github.jarvvski.service.domain.Notification;
import com.github.jarvvski.service.domain.NotificationChannel;
import com.github.jarvvski.service.domain.NotificationTemplate;
import com.github.jarvvski.service.infrastructure.client.User;
import com.github.jarvvski.service.infrastructure.tasks.TaskServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.github.jarvvski.service.domain.ChannelProvider.ChannelProviderWeight.FIRST_CHOICE;

@Service
@RequiredArgsConstructor
public class SendNotification {

    private final ChannelProvider.ChannelProviderRepository channelProviderRepository;
    private final MetricRecorder metricRecorder;
    private final ChannelProvider.ProviderSubmitterRegistry providerSubmitterRegistry;
    private final Notification.NotificationRepository notificationRepository;
    private final NotificationTemplate.NotificationTemplateRegistry notificationTemplateRegistry;
    private final TaskServiceAdapter taskServiceAdapter;

    public record SendNotificationCommand(
            User.UserId userId,
            NotificationTemplate.NotificationTemplateKey templateKey,
            NotificationTemplate.NotificationTemplateParameters parameters,
            NotificationChannel.NotificationChannelType channelType,
            Optional<ChannelProvider.ChannelProviderId> channelProviderId
    ) {
        public static final String SEND_NOTIFICATION_TOPIC = "notification-service.send";
    }

    public record SendNotificationTask(
        Notification.NotificationId notificationId,
        ChannelProvider.ChannelProviderId channelProviderId
    ) {
        private static SendNotificationTask from(
                Notification.NotificationId notificationId,
                ChannelProvider.ChannelProviderId channelProviderId
        ) {
            return new SendNotificationTask(
                notificationId, channelProviderId
            );
        }
    }

    public void queueNotification(SendNotificationCommand sendNotificationCommand) {
        // 1. Pick channel provider
        final var providerId = sendNotificationCommand.channelProviderId()
                .orElseGet(() -> channelProviderRepository.findByTypeAndWeight(
                                sendNotificationCommand.channelType(),
                                FIRST_CHOICE
                        ).getProviderId()
                );

        // 2. Create the notification
        final var notification = Notification.create(
                sendNotificationCommand.templateKey(),
                sendNotificationCommand.channelType(),
                sendNotificationCommand.parameters(),
                sendNotificationCommand.userId()
        );
        notification.markAsInProgress();
        notificationRepository.save(notification);
        metricRecorder.incrementNotificationsPending();

        // 3. Queue up notification request
        final var task = SendNotificationTask.from(notification.getId(), providerId);
        taskServiceAdapter.submit(TaskConfiguration.TaskType.SEND_NOTIFICATION, task);
    }

    public void sendNotification(SendNotificationTask sendNotificationTask) {
        // 0. Unpack task into required args
        final var notification = notificationRepository.findById(sendNotificationTask.notificationId());
        final var channelProvider = channelProviderRepository.findById(sendNotificationTask.channelProviderId());

        // 1. Find matching notification template
        final var template = notificationTemplateRegistry.findByKey(notification.getNotificationTemplateKey());

        // 2. Interpolate notification Contents
        final var notificationContents = template.interpolate(notification.getNotificationTemplateParameters());

        // 3. Find notificationSubmitter for given provider
        final var submitter = providerSubmitterRegistry.findSubmitter(
                notification.getNotificationChannelType(),
                channelProvider.getProviderId()
        );

        // 4. Attempt submission
        final var status = submitter.submit(notification, notificationContents);

        // 5. Record status & update data
        switch (status) {
            case SUCCESS -> {
                notification.markAsSent();
                metricRecorder.incrementNotificationsSent();
            }
            case FAILED -> {
                notification.markAsFailed();
                metricRecorder.incrementNotificationsFailed();
            }
        }

        notificationRepository.save(notification);
    }
}
