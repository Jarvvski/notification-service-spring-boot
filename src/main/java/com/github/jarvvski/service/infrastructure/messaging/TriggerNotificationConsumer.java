package com.github.jarvvski.service.infrastructure.messaging;

import com.github.jarvvski.service.application.SendNotification;
import com.github.jarvvski.service.configuration.TaskConfiguration;
import com.github.jarvvski.service.infrastructure.tasks.TaskServiceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.github.jarvvski.service.application.SendNotification.SendNotificationCommand.SEND_NOTIFICATION_TOPIC;
import static com.github.jarvvski.service.configuration.KafkaConfiguration.DEFAULT_GROUP_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TriggerNotificationConsumer {
    private final TaskServiceAdapter taskServiceAdapter;

    @KafkaListener(topics = SEND_NOTIFICATION_TOPIC, groupId = DEFAULT_GROUP_ID)
    void listen(SendNotification.SendNotificationCommand command) {
        log.info("Received Send notification command via kafka: TemplateKey {} UserId {}",
                command.templateKey(),
                command.userId()
        );
        taskServiceAdapter.submit(
                TaskConfiguration.TaskType.PROCESS_KAFKA_REQUEST_NOTIFICATION,
                command
        );
    }
}
