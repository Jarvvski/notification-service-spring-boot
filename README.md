# Notification Service Example

A Spring-Boot powered example of a relatively simple microservice, that can be used to send notifications using different channels, with multiple providers per channel. Triggered via kafka messages, that are turned into tasks for async execution.

## Domains

### Notifications

### Channels

### Providers

## Running the service

### Docker

You can build & run a docker image for the service via:

```shell
./gradlew bootjar
docker build -t notification-service ./
docker run notification-service
```

Likewise, there's a working docker-compose file to start up the services you need alongside it

```shell
docker compose up -d
```

However, the config isn't fully setup for a production deployment right now. So you'd need to update the spring boot config yml for a production DB URL

## Infra Features

## Adding a new Channel or Provider