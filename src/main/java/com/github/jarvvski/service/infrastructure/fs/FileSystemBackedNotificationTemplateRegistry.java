package com.github.jarvvski.service.infrastructure.fs;

import com.github.jarvvski.service.domain.NotificationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FileSystemBackedNotificationTemplateRegistry implements NotificationTemplate.NotificationTemplateRegistry {

    private final ResourceLoader resourceLoader;
    private static final String DEFAULT_FILE_EXTENSION = "txt";
    private static final Charset DEFAULT_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    @Override
    public NotificationTemplate findByKey(NotificationTemplate.NotificationTemplateKey notificationTemplateKey) {
        try (
                final var resourceInputStream = loadResource(notificationTemplateKey);
                final var fileContents = new BufferedInputStream(resourceInputStream);
        ) {
            final var templateContents = new String(fileContents.readAllBytes(), DEFAULT_CHARACTER_ENCODING);
            return new NotificationTemplate(
                    notificationTemplateKey,
                    NotificationTemplate.NotificationTemplateContents.of(templateContents)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load notification template " + notificationTemplateKey, e);
        }
    }

    private InputStream loadResource(NotificationTemplate.NotificationTemplateKey notificationTemplateKey) throws IOException {
        final var resource = resourceLoader.getResource(
                "classpath:notification-templates/" + formatKey(notificationTemplateKey)
        );
        return resource.getInputStream();
    }

    private String formatKey(NotificationTemplate.NotificationTemplateKey notificationTemplateKey) {
        return String.format("%s.%s", notificationTemplateKey.getData(), DEFAULT_FILE_EXTENSION);
    }
}
