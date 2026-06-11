package ru.yandex.practicum.stellaburgers.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {
    private String email;
    private String password;
    private String name;
}
