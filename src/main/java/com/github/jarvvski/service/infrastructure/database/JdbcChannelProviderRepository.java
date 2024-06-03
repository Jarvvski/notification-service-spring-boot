package com.github.jarvvski.service.infrastructure.database;

import com.github.jarvvski.service.domain.ChannelProvider;
import com.github.jarvvski.service.domain.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class JdbcChannelProviderRepository implements ChannelProvider.ChannelProviderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public ChannelProvider findById(ChannelProvider.ChannelProviderId id) {
        return null;
    }

    @Override
    public List<ChannelProvider> findByType(NotificationChannel.NotificationChannelType notificationChannelType) {
        return List.of();
    }

    @Override
    public void save(ChannelProvider channelProvider) {

    }

    @Override
    public ChannelProvider findByTypeAndWeight(NotificationChannel.NotificationChannelType notificationChannelType, ChannelProvider.ChannelProviderWeight weight) {
        return null;
    }

    @Override
    public ChannelProvider findByTypeAndWeight(NotificationChannel.NotificationChannelType notificationChannelType) {
        return null;
    }
}
