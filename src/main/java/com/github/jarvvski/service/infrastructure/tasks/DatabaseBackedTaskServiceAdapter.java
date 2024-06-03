package com.github.jarvvski.service.infrastructure.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jarvvski.service.configuration.TaskConfiguration;
import com.transferwise.tasks.ITasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
class DatabaseBackedTaskServiceAdapter implements TaskServiceAdapter {
    private final ObjectMapper objectMapper;
    private final ITasksService taskServiceInfra;

    @Override
    public void submit(TaskConfiguration.TaskType taskType, Object object) {
        try {
            final var json = objectMapper.writeValueAsString(object);
            taskServiceInfra.addTask(new ITasksService.AddTaskRequest()
                    .setType(taskType.getKey())
                    .setData(json.getBytes(StandardCharsets.UTF_8)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
