package courier;

import base.BaseTest;
import io.qameta.allure.Step;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends BaseTest {

    @Test
    public void createCourierSuccess() {
        String login = "ninja" + System.currentTimeMillis();
        String password = "1234";
        String firstName = "saske";

        createCourier(login, password, firstName)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        deleteCourier(login, password);
    }

    @Step("Создать курьера с логином {login}")
    private io.restassured.response.Response createCourier(String login, String password, String firstName) {
        String body = "{"
                + "\"login\": \"" + login + "\","
                + "\"password\": \"" + password + "\","
                + "\"firstName\": \"" + firstName + "\""
                + "}";

        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Удалить курьера с логином {login}")
    private void deleteCourier(String login, String password) {
        String loginBody = "{"
                + "\"login\": \"" + login + "\","
                + "\"password\": \"" + password + "\""
                + "}";

        int courierId = given()
                .header("Content-type", "application/json")
                .body(loginBody)
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