package courier;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends BaseTest {

    private CourierModel courier;

    @Test
    public void createCourierSuccess() {
        courier = new CourierModel(
                "ninja" + System.currentTimeMillis(),
                "1234",
                "saske"
        );

        createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    // Негативный тест: без логина
    @Test
    public void createCourierWithoutLogin() {
        courier = new CourierModel(
                null,
                "1234",
                "saske"
        );

        createCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // Негативный тест: без пароля
    @Test
    public void createCourierWithoutPassword() {
        courier = new CourierModel(
                "ninja" + System.currentTimeMillis(),
                null,
                "saske"
        );

        createCourier(courier)
                .then()
                .statusCode(400) // Bad Request
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Создать курьера с логином {courier.login}")
    private io.restassured.response.Response createCourier(CourierModel courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)  // сериализация объекта в JSON
                .when()
                .post("/api/v1/courier");
    }

    @Step("Удалить курьера с логином {courier.login}")
    private void deleteCourier(CourierModel courier) {
        if (courier.getLogin() == null || courier.getPassword() == null) return; // если нет данных — не удаляем

        int courierId = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .delete("/api/v1/courier/" + courierId);
    }

    @After
    public void cleanup() {
        if (courier != null) {
            deleteCourier(courier);
        }
    }
}