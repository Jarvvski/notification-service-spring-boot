package com.github.jarvvski.service.configuration;

import com.github.jarvvski.service.application.SendNotification;
import com.github.jarvvski.service.infrastructure.tasks.TaskServiceAdapter;
import com.transferwise.tasks.handler.interfaces.ISyncTaskProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfiguration {

    @RequiredArgsConstructor
    @Getter
    public enum TaskType {
        PROCESS_KAFKA_REQUEST_NOTIFICATION("process-kafka-request-notification"),
        SEND_NOTIFICATION("send-notification");

        private final String key;
    }

    @Bean
    public TaskServiceAdapter.DefaultTaskHandler<SendNotification.SendNotificationTask> sendNotificationTaskDefaultTaskHandler(
            TaskServiceAdapter.TaskHandlerFactory taskHandlerFactory,
            SendNotification sendNotification) {
        return taskHandlerFactory.create(
                SendNotification.SendNotificationTask.class,
                TaskType.SEND_NOTIFICATION,
                sendNotification::sendNotification
        );
    }

    @Bean
    public TaskServiceAdapter.DefaultTaskHandler<SendNotification.SendNotificationCommand> queueNotificationTaskHandler(
            TaskServiceAdapter.TaskHandlerFactory taskHandlerFactory,
            SendNotification sendNotification) {
        return taskHandlerFactory.create(
                SendNotification.SendNotificationCommand.class,
                TaskType.PROCESS_KAFKA_REQUEST_NOTIFICATION,
                sendNotification::queueNotification
        );
    }
}
