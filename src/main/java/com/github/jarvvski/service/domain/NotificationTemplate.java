package com.github.jarvvski.service.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class NotificationTemplate {

    public static class NotificationTemplateParameters {
        private final Map<String, String> backingStore = new HashMap<>();

        public void put(String key, String value) {
            backingStore.put(key, value);
        }
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    public static class NotificationTemplateKey {
        private final String data;
    }

    @RequiredArgsConstructor(staticName = "of")
    public static class NotificationTemplateContents {
        private final String data;
    }

    private final NotificationTemplateKey notificationKey;
    private final NotificationTemplateContents templateContents;

    public Notification.NotificationContents interpolate(NotificationTemplateParameters parameters) {
        final var substitution = new StringSubstitutor(parameters.backingStore);
        final var interpolatedTemplate = substitution.replace(this.templateContents.data);
        return new Notification.NotificationContents(interpolatedTemplate);
    }

    public interface NotificationTemplateRegistry {
        NotificationTemplate findByKey(NotificationTemplateKey notificationTemplateKey);
    }
}
