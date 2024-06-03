create table if not exists notifications
(
    notification_id     uuid                     not null
        constraint notifications_pk
            primary key,
    template_key        varchar(255)             not null,
    template_paramaters jsonb                    not null,
    target_user_id      uuid                     not null,
    created_at          timestamp with time zone not null,
    sent_at             timestamp with time zone not null,
    status              varchar(255)             not null
);

create table if not exists channel_providers
(
    provider_id     uuid         not null
        constraint channel_providers_pk
            primary key,
    channel_type    varchar(255) not null,
    provider_weight integer      not null,
    provider_status varchar(255) not null
);

