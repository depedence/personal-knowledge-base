package ru.depedence.helper;

import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class AuthHelper {

    public Cookies loginAndGetCookies(String username, String password) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", username)
                .formParam("password", password)
                .redirects().follow(false)
                .when().post("/login")
                .then().statusCode(302)
                .extract().response();

        Cookies cookies = response.getDetailedCookies();

        if (cookies.get("JSESSIONID") == null) {
            throw new RuntimeException(
                    String.format("Failed to login user '%s': JSESSIONID cookie not found. \" +\n" +
                            "Response status: %d, Location: %s",
                            username,
                            response.getStatusCode(),
                            response.getHeader("Location"))
            );
        }

        return cookies;

    }

}