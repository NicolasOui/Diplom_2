package ru.yandex.practicum.stellaburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.CreateUserSteps;
import ru.yandex.practicum.stellaburgers.api.RandomDataUser;
import ru.yandex.practicum.stellaburgers.models.CreateUser;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Регистрация пользователя в Stellar Burgers")
public class CreateUserTest extends BaseTest {

    private CreateUserSteps userSteps;

    @Before
    public void localSetUp() {
        userSteps = new CreateUserSteps();
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Код 200 и success true на запрос регистрации пользователя")
    public void shouldCreateUniqueUserSuccessfully() {
        CreateUser user = RandomDataUser.generate();
        Response response = userSteps.register(user);
        accessToken = response.path("accessToken");
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Регистрационные поля пользователя в теле ответе заполнены")
    public void shouldReceiveUserDataNotNull() {
        CreateUser user = RandomDataUser.generate();
        Response response = userSteps.register(user);
        accessToken = response.path("accessToken");
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Поле с токенами в теле ответа заполнено")
    public void shouldReceiveUniqueUserTokensSuccessfully () {
        CreateUser user = RandomDataUser.generate();
        Response response = userSteps.register(user);
        accessToken = response.path("accessToken");
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при создании уже существующего пользователя")
    @Description("Ошибка 403 при регистрации пользователя с email, который уже есть в системе")
    public void cannotCreateUserWithRegisteredBeforeEmail () {
        CreateUser user = RandomDataUser.generate();
        Response responseFirst = userSteps.register(user);
        accessToken = responseFirst.path("accessToken");

        Response responseDuplicate = userSteps.register(user);
        responseDuplicate.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без обязательного поля name")
    @Description("Ошибка 403 при попытке зарегистрировать пользователя с пустым полем name")
    public void cannotCreateUserWithoutName () {
        CreateUser userWithoutName = RandomDataUser.generate();
        userWithoutName.setName(null);
        Response response = userSteps.register(userWithoutName);
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя без пароля")
    @Description("Ошибка 403 при попытке зарегистрировать пользователя с пустым полем password")
    public void cannotCreateUserWithoutPassword () {
        CreateUser userWithoutPassword = RandomDataUser.generate();
        userWithoutPassword.setPassword(null);
        Response response = userSteps.register(userWithoutPassword);
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
