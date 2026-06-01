package ru.yandex.practicum.stellaburgers.tests;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import ru.yandex.practicum.stellaburgers.api.CreateUserSteps;
import ru.yandex.practicum.stellaburgers.api.Endpoints;
import ru.yandex.practicum.stellaburgers.api.UserClient;

public class BaseTest {

    protected String accessToken;
    protected CreateUserSteps userClient;

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
        userClient = new CreateUserSteps();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}
