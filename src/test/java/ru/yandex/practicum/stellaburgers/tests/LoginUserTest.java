package ru.yandex.practicum.stellaburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.CreateUserSteps;
import ru.yandex.practicum.stellaburgers.api.RandomDataUser;
import ru.yandex.practicum.stellaburgers.models.CreateUser;
import ru.yandex.practicum.stellaburgers.models.LoginUser;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Авторизация пользователя в Stellar Burgers")
public class LoginUserTest extends BaseTest {

    private CreateUserSteps userSteps;
    private CreateUser registeredUser;

    @Before
    public void localSetUp() {
        userSteps = new CreateUserSteps();
        registeredUser = RandomDataUser.generate();
        Response response = userSteps.register(registeredUser);
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    @Description("Код 200, success true и токены в ответе на запрос авторизации")
    public void successUserAuthorisation() {
        LoginUser loginUser = new LoginUser(registeredUser.getEmail(), registeredUser.getPassword());
        Response response = userSteps.login(loginUser);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным паролем")
    @Description("Код 401 и success false при попытке входа с некорректным паролем")
    public void cannotAuthoriseWithWrongPassword() {
        LoginUser loginUserWithWrongPass = new LoginUser(registeredUser.getEmail(), "wrong_password_999");
        Response response = userSteps.login(loginUserWithWrongPass);
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
