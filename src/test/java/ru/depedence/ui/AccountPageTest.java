package ru.depedence.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.depedence.base.BaseUiTest;
import ru.depedence.helpers.TestDataHelper;
import ru.depedence.pages.AccountPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("Check title name on Account Page")
    void checkAccountPageTitle() {
        assertTrue(accountPage.getPageTitle().contains("PKB Home Page"));
    }

    @Test
    @DisplayName("Try to create test Note")
    void createTestNote__Success() {
        String title = "this is test title";
        String content = "this is test content";

        accountPage.openModalAndFillInputs(title, content);

        System.out.println("ZXC: По факту что там в элементе title - " + accountPage.getNoteTitle());
        System.out.println("ZXC: Что должно быть - " + title);

        System.out.println("ZXC: По факту что там в элементе content - " + accountPage.getNoteContent());
        System.out.println("ZXC: Что должно быть - " + content);

        assertTrue(accountPage.getNoteTitle().contains(title));
        assertTrue(accountPage.getNoteContent().contains(content));
    }
}