package com.github.jarvvski.service.domain;

import com.fasterxml.uuid.Generators;
import com.google.common.primitives.UnsignedInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ChannelProvider {
    public record ChannelProviderId(String string) {
        public static ChannelProviderId create() {
            final var uuid = Generators.timeBasedEpochGenerator().generate();
            return new ChannelProviderId(uuid.toString());
        }
        public static ChannelProviderId of(String data) {
            return new ChannelProviderId(data);
        }
    }

    public record ChannelProviderWeight(UnsignedInteger weight) {
        public static final ChannelProviderWeight FIRST_CHOICE = ChannelProviderWeight.create(UnsignedInteger.ZERO);
        private static final UnsignedInteger DEFAULT_WEIGHT = UnsignedInteger.ONE;
        public static ChannelProviderWeight create() {
            return new ChannelProviderWeight(DEFAULT_WEIGHT);
        }
        public static ChannelProviderWeight create(UnsignedInteger unsignedInteger) {
            return new ChannelProviderWeight(unsignedInteger);
        }
    }

    public enum ProviderStatus {
        AVAILABLE,
        DISABLED
    }

    public interface ChannelProviderRepository {
        ChannelProvider findById(ChannelProviderId id);
        List<ChannelProvider> findByType(NotificationChannel.NotificationChannelType notificationChannelType);
        void save(ChannelProvider channelProvider);
        ChannelProvider findByTypeAndWeight(NotificationChannel.NotificationChannelType notificationChannelType, ChannelProviderWeight weight);
        ChannelProvider findByTypeAndWeight(NotificationChannel.NotificationChannelType notificationChannelType);
    }

    public interface NotificationSubmitter {
        NotificationSubmissionState submit(Notification notification, Notification.NotificationContents contents);
        boolean supports(NotificationChannel.NotificationChannelType notificationChannelType);
        boolean forProvider(ChannelProviderId channelProviderId);

        enum NotificationSubmissionState {
            SUCCESS, FAILED;
        }
    }

    private final ChannelProviderId providerId;
    private final NotificationChannel.NotificationChannelType notificationChannelType;
    private final ChannelProviderWeight channelProviderWeight;
    private final ProviderStatus providerStatus;

    @Service
    @RequiredArgsConstructor
    public static class ProviderSubmitterRegistry {
        private final List<NotificationSubmitter> notificationSubmitters;


        public static class NotificationSubmitterNotFound extends RuntimeException {
            private NotificationSubmitterNotFound(String message) {
                super(message);
            }

            public static NotificationSubmitterNotFound of(
                    ChannelProviderId channelProviderId,
                    NotificationChannel.NotificationChannelType notificationChannelType
            ) {
                final var message = "Notification submitter not found for channel provider id "
                        + channelProviderId
                        + " and type "
                        + notificationChannelType;
                return new NotificationSubmitterNotFound(message);
            }
        }

        public NotificationSubmitter findSubmitter(
                NotificationChannel.NotificationChannelType notificationChannelType,
                ChannelProviderId channelProviderId
        ) {
            NotificationSubmitter submitter = null;
            for (final var notificationSubmitter : notificationSubmitters) {
                if (!notificationSubmitter.forProvider(channelProviderId)) {
                    continue;
                }
                if (!notificationSubmitter.supports(notificationChannelType)) {
                    continue;
                }
                submitter = notificationSubmitter;
            }

            if (submitter == null) {
                throw NotificationSubmitterNotFound.of(channelProviderId, notificationChannelType);
            }

            return submitter;
        }
    }
}
