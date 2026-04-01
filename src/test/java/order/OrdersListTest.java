package order;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest extends BaseTest {

    @Test
    public void getOrdersList() {
        getOrders()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @Step("Получить список заказов")
    private io.restassured.response.Response getOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
    }
}