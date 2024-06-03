package com.github.jarvvski.service.infrastructure.client;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Faker faker;

    // simulating fetching user data from remote service / database etc
    private final Map<User.UserId, User> userStorage = new HashMap<>();

    public User findById(User.UserId userId) {
        if (userStorage.containsKey(userId)) {
            return userStorage.get(userId);
        }

        final var user = new User(
                userId,
                User.UserName.of(faker.internet().username()),
                User.PhoneNumber.of(faker.phoneNumber().cellPhone()),
                User.Email.of(faker.internet().emailAddress())
        );

        userStorage.put(user.getUserId(), user);
        return user;
    }
}
