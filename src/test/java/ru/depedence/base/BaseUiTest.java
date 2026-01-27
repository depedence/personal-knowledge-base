package ru.depedence.base;

import io.restassured.http.Cookies;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import ru.depedence.helper.AuthHelper;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseUiTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected AuthHelper authHelper;

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeEach
    public void setUpSelenium() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void testDownSelenium() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void loginWithCookies(String username, String password) {
        driver.get(baseUrl);

        Cookies restAssuredCookies = authHelper.loginAndGetCookies(
                username,
                password,
                "http://localhost",
                port
        );

        restAssuredCookies.asList().forEach(restAssuredCookie -> {
            Cookie seleniumCookie = new Cookie(
                    restAssuredCookie.getName(),
                    restAssuredCookie.getValue(),
                    restAssuredCookie.getDomain(),
                    restAssuredCookie.getPath(),
                    restAssuredCookie.getExpiryDate(),
                    restAssuredCookie.isSecured(),
                    restAssuredCookie.isHttpOnly()
            );

            driver.manage().addCookie(seleniumCookie);
        });
    }

}