package com.github.jarvvski.service.infrastructure.database;

import com.github.jarvvski.service.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcNotificationRepository implements Notification.NotificationRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Notification findById(Notification.NotificationId notificationId) {
        return null;
    }

    @Override
    public void save(Notification notification) {

    }
}
