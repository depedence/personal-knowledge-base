package ru.depedence.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.StaleElementReferenceException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.depedence.base.BaseUiTest;
import ru.depedence.helper.TestDataHelper;
import ru.depedence.page.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Login Page UI Tests")
public class LoginPageTest extends BaseUiTest {

    @Autowired
    private TestDataHelper helper;

    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        helper.cleanDatabase();
        helper.createTestUser("testUser", "testPassword");
        loginPage = new LoginPage(driver);
        loginPage.open(baseUrl);
    }

    @Test
    @DisplayName("Login successfully with valid credentials")
    void loginWithValidCredentials__Success() {
        loginPage.login("testUser", "testPassword");
        loginPage.waitForRedirect();

        assertTrue(loginPage.getCurrentUrl().contains("/account"));
    }

    @Test
    @DisplayName("Login failed with invalid credentials")
    void loginWithInvalidCredentials__Failure() {
        loginPage.login("testUser", "incorrectPassword");

        int MAX_RETRY = 3;

        for (int i = 0; i < MAX_RETRY; i++) {
            try {
                loginPage.isErrorDisplayed();
                break;
            } catch (StaleElementReferenceException e) {
                if (i == MAX_RETRY - 1) throw e;
                loginPage.isErrorDisplayed();
            }
        }

        assertTrue(loginPage.getErrorMessage().contains("Wrong username or password"));
    }
}