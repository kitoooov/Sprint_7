package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_PATH = "/api/v1/orders";

    @Step("Создать заказ через API с цветами: {colors}")
    public Response createOrder(OrderModel order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(BASE_PATH);
    }

    @Step("Удалить заказ через API по треку {track}")
    public void deleteOrder(int track) {
        given()
                .when()
                .delete(BASE_PATH + "/cancel/" + track);
    }
}