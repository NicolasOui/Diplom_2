package ru.yandex.practicum.stellaburgers.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellaburgers.models.CreateUser;
import ru.yandex.practicum.stellaburgers.models.LoginUser;
import static io.restassured.RestAssured.given;

public class CreateUserSteps {

    @Step("Отправка POST-запроса на регистрацию пользователя")
    public Response register(CreateUser user) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .header("Content-Type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .body(user)
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

    @Step("Отправка POST-запроса на авторизацию пользователя (логин)")
    public Response login(LoginUser loginUser) {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .header("Content-Type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .body(loginUser)
                .when()
                .post(Endpoints.USER_LOGIN);
    }
}
