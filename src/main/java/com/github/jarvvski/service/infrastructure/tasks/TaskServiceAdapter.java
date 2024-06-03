package com.github.jarvvski.service.infrastructure.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jarvvski.service.configuration.TaskConfiguration;
import com.transferwise.tasks.domain.IBaseTask;
import com.transferwise.tasks.domain.ITask;
import com.transferwise.tasks.handler.ExponentialTaskRetryPolicy;
import com.transferwise.tasks.handler.SimpleTaskConcurrencyPolicy;
import com.transferwise.tasks.handler.SimpleTaskProcessingPolicy;
import com.transferwise.tasks.handler.interfaces.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Consumer;

public interface TaskServiceAdapter {

    void submit(TaskConfiguration.TaskType taskType, Object object);

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    final class DefaultTaskHandler<T> implements ITaskHandler, ISyncTaskProcessor {
        private final TaskConfiguration.TaskType taskType;
        private final Consumer<T> consumer;
        private final ObjectMapper objectMapper;
        private final Class<T> tClass;

        @Override
        public ITaskProcessor getProcessor(IBaseTask ignored) {
            return this;
        }

        @Override
        public ITaskRetryPolicy getRetryPolicy(IBaseTask ignored) {
            return new ExponentialTaskRetryPolicy()
                    .setDelay(Duration.ofSeconds(5))
                    .setMultiplier(2)
                    .setMaxCount(20)
                    .setMaxDelay(Duration.ofMinutes(120));
        }

        @Override
        public ITaskConcurrencyPolicy getConcurrencyPolicy(IBaseTask ignored) {
            return new SimpleTaskConcurrencyPolicy(5);
        }

        @Override
        public ITaskProcessingPolicy getProcessingPolicy(IBaseTask ignored) {
            return new SimpleTaskProcessingPolicy()
                    .setMaxProcessingDuration(Duration.ofMinutes(30));
        }

        @Override
        public boolean handles(IBaseTask task) {
            return task.getType().equals(this.taskType.getKey());
        }

        @Override
        public ProcessResult process(ITask task) {
            var result = new ISyncTaskProcessor.ProcessResult();
            try {
                final var parsed = objectMapper.convertValue(task, tClass);
                this.consumer.accept(parsed);
            } catch (Exception e) {
                return result.setResultCode(ISyncTaskProcessor.ProcessResult.ResultCode.COMMIT_AND_RETRY);
            }
            return result.setResultCode(ProcessResult.ResultCode.DONE);
        }
    }

    @Service
    @RequiredArgsConstructor
    class TaskHandlerFactory {
        private final ObjectMapper objectMapper;

        public <T> DefaultTaskHandler<T> create(
                Class<T> payload,
                TaskConfiguration.TaskType taskType,
                Consumer<T> consumer
        ) {
            return new DefaultTaskHandler<>(taskType, consumer, this.objectMapper, payload);
        }
    }
}
