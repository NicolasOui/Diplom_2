package ru.yandex.practicum.stellaburgers.api;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellaburgers.models.CreateUser;
import ru.yandex.practicum.stellaburgers.models.LoginUser;

import static io.restassured.RestAssured.given;

public class UserClient {

    private final Gson gson = new Gson();

    @Step("Регистрация пользователя через API (Gson-сериализация)")
    public Response register(CreateUser user) {
        String jsonBody = gson.toJson(user);
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .header("Content-Type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .body(jsonBody) // Передаем готовую Gson-строку
                .when()
                .post(Endpoints.USER_REGISTER);
    }

    @Step("Удаление пользователя по токену")
    public Response delete(String token) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .baseUri(Endpoints.BASE_URL)
                .when()
                .delete(Endpoints.DELETE_USER);
    }

    @Step("Отправка POST-запроса на авторизацию пользователя (логин) через API (Gson-сериализация)")
    public Response login(LoginUser loginUser) {
        // Явно превращаем объект логина в JSON-строку с помощью Gson
        String jsonBody = gson.toJson(loginUser);

        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .header("Content-Type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .body(jsonBody) // Передаем готовую JSON-строку
                .when()
                .post(Endpoints.USER_LOGIN);
    }
}
