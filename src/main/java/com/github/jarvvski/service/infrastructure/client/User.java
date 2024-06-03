package com.github.jarvvski.service.infrastructure.client;

import com.fasterxml.uuid.Generators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class User {

    public record UserId(String data) {
        public static UserId create() {
            final var uuid = Generators.timeBasedEpochGenerator().generate();
            return new UserId(uuid.toString());
        }
        public static UserId of(String data) {
            return new UserId(data);
        }
    }

    public record UserName(String data) {
        public static UserName of(String data) {
            return new UserName(data);
        }
    }

    public record PhoneNumber(String data) {
        public static PhoneNumber of(String data) {
            return new PhoneNumber(data);
        }
    }

    public record Email(String data) {
        public static Email of(String data) {
            return new Email(data);
        }
    }

    private final UserId userId;
    private final UserName userName;
    private final PhoneNumber phoneNumber;
    private final Email email;
}
