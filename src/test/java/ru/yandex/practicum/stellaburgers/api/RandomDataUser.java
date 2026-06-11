package ru.yandex.practicum.stellaburgers.api;

import ru.yandex.practicum.stellaburgers.models.CreateUser;

public class RandomDataUser {

    public static CreateUser generate() {
        long timestamp = System.currentTimeMillis();
        String uniqueEmail = "user_" + timestamp + "@yandex.ru";
        String password = "password_" + timestamp;
        String name = "Name_" + timestamp;
        return new CreateUser(uniqueEmail, password, name);
    }
}
