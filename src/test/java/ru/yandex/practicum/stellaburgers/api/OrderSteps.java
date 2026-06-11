package ru.yandex.practicum.stellaburgers.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellaburgers.models.Order;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Получить список доступных ингредиентов")
    public Response getIngredients() {
        return given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .baseUri(Endpoints.BASE_URL)
                .when()
                .get(Endpoints.INGREDIENTS);
    }

    @Step("Создать заказ (с авторизацией или без)")
    public Response createOrder(Order order, String token) {
        var requestSpec = given()
                .filter(new io.qameta.allure.restassured.AllureRestAssured())
                .header("Content-Type", "application/json")
                .baseUri(Endpoints.BASE_URL);

        if (token != null) {
            requestSpec.header("Authorization", token);
        }

        return requestSpec
                .body(order)
                .when()
                .post(Endpoints.ORDERS);
    }
}