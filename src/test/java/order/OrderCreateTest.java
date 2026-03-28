package order;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest extends BaseTest {

    @Test
    public void createOrderWithColorBlack() {
        createOrder("BLACK")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Step("Создать заказ с цветом {color}")
    private io.restassured.response.Response createOrder(String color) {
        String body = "{"
                + "\"firstName\": \"Alex\","
                + "\"lastName\": \"Kitov\","
                + "\"address\": \"Spain\","
                + "\"metroStation\": 1,"
                + "\"phone\": \"+1234567890\","
                + "\"rentTime\": 5,"
                + "\"deliveryDate\": \"2026-03-30\","
                + "\"comment\": \"test\","
                + "\"color\": [\"" + color + "\"]"
                + "}";

        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/orders");
    }
}
