package order;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest extends BaseTest {

    private int track; // для удаления заказа после теста

    @After
    public void cleanupOrder() {
        if (track != 0) {
            deleteOrder(track);
        }
    }

    @Test
    public void createOrderBlack() {
        createOrder("BLACK");
    }

    @Test
    public void createOrderGrey() {
        createOrder("GREY");
    }

    @Test
    public void createOrderBlackAndGrey() {
        createOrder("BLACK", "GREY");
    }

    @Test
    public void createOrderNoColor() {
        createOrder();
    }

    @Step("Создать заказ с цветами: {colors}")
    private void createOrder(String... colors) {
        OrderModel order = new OrderModel(
                "Alex",
                "Kitov",
                "Spain",
                1,
                "+1234567890",
                5,
                "2026-03-30",
                "test",
                colors
        );

        track = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }

    @Step("Удалить заказ по треку {track}")
    private void deleteOrder(int track) {
        given()
                .when()
                .delete("/api/v1/orders/cancel/" + track);
    }
}