package order;

import base.BaseTest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {

    private int track; // для удаления заказа после теста
    private String[] colors;
    private final OrderClient orderClient = new OrderClient();

    // Конструктор для параметров
    public OrderCreateTest(String[] colors) {
        this.colors = colors;
    }

    // Параметры теста
    @Parameterized.Parameters(name = "Цвета заказа: {0}")
    public static Object[][] getColors() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    @After
    public void cleanupOrder() {
        if (track != 0) {
            orderClient.deleteOrder(track);
        }
    }

    @Test
    public void createOrderTest() {
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

        track = orderClient.createOrder(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }
}