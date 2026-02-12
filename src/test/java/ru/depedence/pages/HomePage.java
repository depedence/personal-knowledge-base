package ru.depedence.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "newNoteButton")
    private WebElement openModal;

    @FindBy(id = "createNoteTitleInput")
    private WebElement titleInput;

    @FindBy(id = "createNoteContentInput")
    private WebElement contentInput;

    @FindBy(id = "createNoteButton")
    private WebElement createBtn;

    @FindBy(id = "noteTitle")
    private WebElement noteTitle;

    @FindBy(id = "noteBody")
    private WebElement noteContent;

    @FindBy(id = "noteCategory")
    private WebElement noteCategory;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/home");
    }

    public void openModalAndFillInputs(String title, String content) {
        wait.until(ExpectedConditions.elementToBeClickable(openModal));
        openModal.click();

        wait.until(ExpectedConditions.visibilityOf(titleInput));
        titleInput.sendKeys(title);

        contentInput.sendKeys(content);

        createBtn.click();
    }

    public String getNoteTitle() {
        wait.until(ExpectedConditions.visibilityOf(noteTitle));
        return noteTitle.getText();
    }

    public String getNoteContent() {
        wait.until(ExpectedConditions.visibilityOf(noteContent));
        return noteContent.getText();
    }

    public String getNoteCategory() {
        wait.until(ExpectedConditions.visibilityOf(noteCategory));
        return noteCategory.getText();
    }

}
