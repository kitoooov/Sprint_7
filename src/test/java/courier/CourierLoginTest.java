package courier;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {

    private CourierModel courier;

    @Before
    public void initCourier() {
        // создаём курьера перед тестами
        courier = new CourierModel(
                "ninja" + System.currentTimeMillis(),
                "1234",
                "saske"
        );

        createCourier(courier).then().statusCode(201);
    }

    @Test
    public void loginCourierSuccess() {
        loginCourier(courier)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void loginWithWrongPassword() {
        CourierModel wrongPass = new CourierModel(courier.getLogin(), "wrong", courier.getFirstName());
        loginCourier(wrongPass)
                .then()
                .statusCode(404);
    }

    @Test
    public void loginWithoutLogin() {
        CourierModel noLogin = new CourierModel(null, courier.getPassword(), courier.getFirstName());
        loginCourier(noLogin)
                .then()
                .statusCode(400);
    }

    @Test
    public void loginWithoutPassword() {
        CourierModel noPassword = new CourierModel(courier.getLogin(), null, courier.getFirstName());
        loginCourier(noPassword)
                .then()
                .statusCode(400);
    }

    @Test
    public void loginWithWrongLogin() {
        CourierModel wrongLogin = new CourierModel("wrongLogin", courier.getPassword(), courier.getFirstName());
        loginCourier(wrongLogin)
                .then()
                .statusCode(404);
    }

    @Step("Создать курьера с логином {courier.login}")
    private Response createCourier(CourierModel courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Удалить курьера с логином {courier.login}")
    private void deleteCourier(CourierModel courier) {
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        // если пользователь существует, удаляем
        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.then().extract().path("id");
            given()
                    .when()
                    .delete("/api/v1/courier/" + courierId);
        }
    }

    @Step("Логин курьера с логином {courier.login}")
    private Response loginCourier(CourierModel courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }
    @After
    public void tearDown() {
        // удаляем курьера
        if (courier != null) {
            deleteCourier(courier);
        }
    }
}