package com.github.jarvvski.service.domain;

import com.fasterxml.uuid.Generators;
import com.github.jarvvski.service.infrastructure.client.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Notification {

    public record NotificationId(String data) {
        public static NotificationId create() {
            final var uuid = Generators.timeBasedEpochGenerator().generate();
            return new NotificationId(uuid.toString());
        }
        public static NotificationId of(String data) {
            return new NotificationId(data);
        }
    }

    public record NotificationContents(String data) {
    }

    public interface NotificationRepository {
        Notification findById(NotificationId notificationId);
        void save(Notification notification);
    }

    public enum NotificationStatus {
        NEW,
        PENDING,
        SENT,
        FAILED
    }

    private final NotificationId id;
    private final NotificationTemplate.NotificationTemplateKey notificationTemplateKey;
    private final NotificationTemplate.NotificationTemplateParameters notificationTemplateParameters;
    private final NotificationChannel.NotificationChannelType notificationChannelType;
    private final User.UserId targetedUser;
    private final ZonedDateTime createdAt;
    private ZonedDateTime sentAt;
    private NotificationStatus status;

    public static Notification create(
            NotificationTemplate.NotificationTemplateKey notificationTemplateKey,
            NotificationChannel.NotificationChannelType notificationChannelType,
            NotificationTemplate.NotificationTemplateParameters parameters,
            User.UserId userId
    ) {
        return new Notification(
                NotificationId.create(),
                notificationTemplateKey,
                parameters,
                notificationChannelType,
                userId,
                ZonedDateTime.now(),
                null,
                NotificationStatus.NEW
        );
    }

    public void markAsInProgress() {
        this.status = NotificationStatus.PENDING;
    }

    public void markAsSent() {
        this.sentAt = ZonedDateTime.now();
        this.status = NotificationStatus.SENT;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }
}
