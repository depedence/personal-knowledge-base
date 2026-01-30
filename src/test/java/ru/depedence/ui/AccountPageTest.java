package ru.depedence.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.depedence.base.BaseUiTest;
import ru.depedence.helpers.TestDataHelper;
import ru.depedence.pages.AccountPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Epic("UI Testing")
@Feature("Account Page")
@DisplayName("Account Page UI Tests")
public class AccountPageTest extends BaseUiTest {

    @Autowired
    private TestDataHelper helper;

    private AccountPage accountPage;

    @BeforeEach
    void setUp() {
        helper.cleanDatabase();
        helper.createTestUser("testUser", "testPassword");
        loginWithCookies("testUser", "testPassword");
        accountPage = new AccountPage(driver);
        accountPage.open(baseUrl);
    }

    @Test
    @Story("Title Check")
    @Severity(SeverityLevel.TRIVIAL)
    @Description("Тест проверяет тайтл страницы")
    @DisplayName("Check title name on Account Page")
    @Step("Переходим на основную страницу и проверяем тайтл")
    void checkAccountPageTitle() {
        takeScreenshot("CheckTitle");
        assertTrue(accountPage.getPageTitle().contains("PKB Home Page"));
    }

    @Test
    @Story("Create Note")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Тест проверяет сохранение заметки")
    @DisplayName("Try to create test Note")
    void createTestNote__Success() {
        String title = "this is test title";
        String content = "this is test content";

        takeScreenshot("Before fill");

        accountPage.openModalAndFillInputs(title, content);

        takeScreenshot("After fill");

        assertTrue(accountPage.getNoteTitle().contains(title));
        assertTrue(accountPage.getNoteContent().contains(content));
    }

    @Attachment(value = "Screenshot: {name}", type = "image/png")
    private byte[] takeScreenshot(String name) {
        return ((org.openqa.selenium.TakesScreenshot) driver)
                .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
    }

}