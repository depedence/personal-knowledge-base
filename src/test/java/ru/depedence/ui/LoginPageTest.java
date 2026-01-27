package ru.depedence.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.depedence.base.BaseUiTest;
import ru.depedence.entity.User;
import ru.depedence.helper.TestDataHelper;
import ru.depedence.page.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Login Page UI Tests")
public class LoginPageTest extends BaseUiTest {

    @Autowired
    private TestDataHelper helper;

    private LoginPage loginPage;
    private User testUser;

    @BeforeEach
    void setUp() {
        helper.cleanDatabase();
        testUser = helper.createTestUser("testUser", "testPassword");
        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("Login successfully with valid credentials")
    void loginWithValidCredentials__Success() {
        loginPage.open(baseUrl);

        loginPage.login("testUser", "testPassword");
        loginPage.waitForRedirect();

        assertTrue(loginPage.getCurrentUrl().contains("/account"));
    }
}