package ru.yandex.practicum.stellaburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellaburgers.api.CreateUserSteps;
import ru.yandex.practicum.stellaburgers.api.OrderSteps;
import ru.yandex.practicum.stellaburgers.api.RandomDataUser;
import ru.yandex.practicum.stellaburgers.models.CreateUser;
import ru.yandex.practicum.stellaburgers.models.LoginUser;
import ru.yandex.practicum.stellaburgers.models.Order;
import java.util.ArrayList;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Создание заказа в Stellar Burgers")
public class OrderTest extends BaseTest {

    private CreateUserSteps userSteps;
    private OrderSteps orderSteps;
    private List<String> validIngredients;

    @Before
    public void localSetUp() {
        userSteps = new CreateUserSteps();
        orderSteps = new OrderSteps();

        CreateUser userForOrder = RandomDataUser.generate();
        Response response = userSteps.register(userForOrder);
        accessToken = response.path("accessToken");

        LoginUser loginUser = new LoginUser(userForOrder.getEmail(), userForOrder.getPassword());
        userSteps.login(loginUser);

        Response ingredientsResponse = orderSteps.getIngredients();
        List<String> allIds = ingredientsResponse.path("data._id");

        validIngredients = new ArrayList<>();
        if (allIds != null && allIds.size() >= 2) {
            validIngredients.add(allIds.get(0));
            validIngredients.add(allIds.get(1));
        }
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Создание заказа с авторизацией: success true")
    public void createOrderByUSerWithAutorisation() {
        Order order = new Order(validIngredients);
        Response response = orderSteps.createOrder(order, accessToken);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());

    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Создание заказа без авторизации: success true ")
    public void createOrderByUSerWithoutAutorisation() {
        Order order = new Order(validIngredients);
        Response response = orderSteps.createOrder(order, null);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при создании заказа")
    @Description("Код 400 при создании заказа с авторизацией без передачи ингредиентов")
    public void createOrderByUSerWithAutorisationWithoutIngredients() {
        Order order = new Order(null);
        Response response = orderSteps.createOrder(order, accessToken);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Ошибка при создании заказа с неверным хешем ингредиентов")
    @Description("Код 500 при передаче невалидного хеша ингредиента")
    public void cannotCreateOrderWithInvalidHashIngredients() {
        List<String> invalidHashIngredients = new ArrayList<>();
        invalidHashIngredients.add("invalid_hash_value_999");

        Order orderWithWrongHash = new Order(invalidHashIngredients);
        Response response = orderSteps.createOrder(orderWithWrongHash, accessToken);
        response.then()
                .statusCode(500);
    }
}
