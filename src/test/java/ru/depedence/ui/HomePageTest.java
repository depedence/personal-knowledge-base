package ru.depedence.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.depedence.base.BaseUiTest;
import ru.depedence.helpers.TestDataHelper;
import ru.depedence.pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Epic("UI Testing")
@Feature("Home Page")
@DisplayName("Home Page UI Tests")
public class HomePageTest extends BaseUiTest {

    @Autowired
    private TestDataHelper helper;

    private HomePage homePage;

    @BeforeEach
    void setUp() {
        helper.cleanDatabase();
        helper.createTestUser("testUser", "testPassword");
        loginWithCookies("testUser", "testPassword");
        homePage = new HomePage(driver);
        homePage.open(baseUrl);
    }

    @Test
    @Story("Title Check")
    @Severity(SeverityLevel.TRIVIAL)
    @Description("РўРµСЃС‚ РїСЂРѕРІРµСЂСЏРµС‚ С‚Р°Р№С‚Р» СЃС‚СЂР°РЅРёС†С‹")
    @DisplayName("Check title name on Home Page")
    @Step("РџРµСЂРµС…РѕРґРёРј РЅР° РѕСЃРЅРѕРІРЅСѓСЋ СЃС‚СЂР°РЅРёС†Сѓ Рё РїСЂРѕРІРµСЂСЏРµРј С‚Р°Р№С‚Р»")
    void checkHomePageTitle() {
        takeScreenshot("CheckTitle");
        assertTrue(homePage.getPageTitle().contains("PKB Home Page"));
    }

    @Test
    @Story("Create Note")
    @Severity(SeverityLevel.BLOCKER)
    @Description("РўРµСЃС‚ РїСЂРѕРІРµСЂСЏРµС‚ СЃРѕС…СЂР°РЅРµРЅРёРµ Р·Р°РјРµС‚РєРё")
    @DisplayName("Try to create test Note")
    void createTestNote__Success() {
        String title = "this is test title";
        String content = "this is test content";

        takeScreenshot("Before fill");

        homePage.openModalAndFillInputs(title, content);

        takeScreenshot("After fill");

        assertTrue(homePage.getNoteTitle().contains(title));
        assertTrue(homePage.getNoteContent().contains(content));
        assertTrue(homePage.getNoteCategory().contains("Personal"));
    }

    @Attachment(value = "Screenshot: {name}", type = "image/png")
    private byte[] takeScreenshot(String name) {
        return ((org.openqa.selenium.TakesScreenshot) driver)
                .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
    }

}
