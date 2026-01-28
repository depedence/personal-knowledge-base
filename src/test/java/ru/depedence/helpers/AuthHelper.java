package ru.depedence.helpers;

import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class AuthHelper {

    public Cookies loginAndGetCookies(String username, String password) {
        return performLogin(given(), username, password);
    }

    public Cookies loginAndGetCookies(String username, String password, String baseUrl, int port) {
        RequestSpecification spec = given()
                .baseUri(baseUrl)
                .port(port);
        return performLogin(spec, username, password);
    }

    private Cookies performLogin(RequestSpecification spec, String username, String password) {
        Response response = spec
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