package ru.depedence.api;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.depedence.base.BaseApiTest;
import ru.depedence.entity.User;
import ru.depedence.helpers.TestDataHelper;
import ru.depedence.repository.UserRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("api")
@Epic("API Testing")
@Feature("User API")
@DisplayName("User API Tests")
public class UserApiTest extends BaseApiTest {

    @Autowired
    private TestDataHelper dataHelper;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    String username = "testUser";
    String password = "testPassword";

    @BeforeEach
    void setUp() {
        dataHelper.cleanDatabase();
        testUser = dataHelper.createTestUser(username, password);
        authenticateAs("testUser", "testPassword");
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @Story("Get User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение пользователя через API")
    @DisplayName("GET /api/users/{userId} - get user with valid id")
    void getUserById_withValidId__Success() {
        Response response = given().spec(requestSpec)
                .when().get("/api/users/" + testUser.getId())
                .then().statusCode(200)
                .body("id", equalTo(testUser.getId()))
                .body("username", equalTo(username))
                .extract().response();

        String usernameUser = response.jsonPath().getString("username");
        assertTrue(userRepository.existsByUsername(usernameUser), "User should exist in database");

        assertEquals(username, usernameUser);
    }

    @Test
    @Story("Get User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение пользователя через API")
    @DisplayName("GET /api/users/{userId} - get user with invalid id")
    void getUserById_withInvalidId__Failure() {
        Response response = given().spec(requestSpec)
                .when().get("/api/users/" + 999)
                .then().statusCode(404)
                .body("status", equalTo(404))
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("User with id = 999 not found"))
                .extract().response();
    }

    @Test
    @Story("Get Current User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение авторизованного тестового пользователя через API")
    @DisplayName("GET /api/users/me - get current user")
    void getCurrentUser__Success() {
        Response response = given().spec(requestSpec)
                .when().get("/api/users/me")
                .then().statusCode(200)
                .body("id", equalTo(testUser.getId()))
                .body("username", equalTo(testUser.getUsername()))
                .extract().response();

        int userId = response.jsonPath().getInt("id");

        assertEquals(userId, testUser.getId());
    }
}