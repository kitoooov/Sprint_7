package courier;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {

    @Test
    public void loginCourierSuccess() {
        String login = "ninja" + System.currentTimeMillis();
        String password = "1234";

        createCourier(login, password, "saske");

        loginCourier(login, password)
                .then()
                .statusCode(200)
                .body("id", notNullValue());

        deleteCourier(login, password);
    }

    @Step("Создать курьера с логином {login}")
    private void createCourier(String login, String password, String firstName) {
        String body = "{"
                + "\"login\": \"" + login + "\","
                + "\"password\": \"" + password + "\","
                + "\"firstName\": \"" + firstName + "\""
                + "}";

        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера {login}")
    private io.restassured.response.Response loginCourier(String login, String password) {
        String body = "{"
                + "\"login\": \"" + login + "\","
                + "\"password\": \"" + password + "\""
                + "}";

        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удалить курьера с логином {login}")
    private void deleteCourier(String login, String password) {
        String body = "{"
                + "\"login\": \"" + login + "\","
                + "\"password\": \"" + password + "\""
                + "}";

        int courierId = given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .delete("/api/v1/courier/" + courierId);
    }
}
