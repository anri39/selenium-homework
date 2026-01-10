package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class DemoQaPracticeFormPage extends BasePage {
    private static final String URL = "https://demoqa.com/automation-practice-form";

    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By userEmail = By.id("userEmail");
    private final By genderMaleLabel = By.cssSelector("label[for='gender-radio-1']");
    private final By userNumber = By.id("userNumber");
    private final By dateOfBirthInput = By.id("dateOfBirthInput");
    private final By datePickerMonthSelect = By.cssSelector(".react-datepicker__month-select");
    private final By datePickerYearSelect = By.cssSelector(".react-datepicker__year-select");
    private final By subjectsInput = By.id("subjectsInput");
    private final By hobbiesSportsLabel = By.cssSelector("label[for='hobbies-checkbox-1']");
    private final By currentAddress = By.id("currentAddress");
    private final By submitButton = By.id("submit");

    private final By modalContent = By.className("modal-content");
    private final By closeLargeModal = By.id("closeLargeModal");

    public DemoQaPracticeFormPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open DemoQA Practice Form page")
    public DemoQaPracticeFormPage open() {
        open(URL);
        return this;
    }

    @Step("Set first name: {value}")
    public DemoQaPracticeFormPage setFirstName(String value) {
        type(firstName, value);
        return this;
    }

    @Step("Set last name: {value}")
    public DemoQaPracticeFormPage setLastName(String value) {
        type(lastName, value);
        return this;
    }

    @Step("Set email: {value}")
    public DemoQaPracticeFormPage setEmail(String value) {
        type(userEmail, value);
        return this;
    }

    @Step("Select gender: Male")
    public DemoQaPracticeFormPage selectMaleGender() {
        click(genderMaleLabel);
        return this;
    }

    @Step("Set mobile number: {value}")
    public DemoQaPracticeFormPage setMobileNumber(String value) {
        type(userNumber, value);
        return this;
    }

    @Step("Set date of birth: {day} {month} {year}")
    public DemoQaPracticeFormPage setDateOfBirth(int day, String month, String year) {
        click(dateOfBirthInput);

        new Select(el(datePickerMonthSelect)).selectByVisibleText(month);
        new Select(el(datePickerYearSelect)).selectByVisibleText(year);

        String dayStr = String.format("%02d", day);
        By dayLocator = By.xpath(
                "//div[contains(@class,'react-datepicker__day--0" + dayStr + "') and not(contains(@class,'react-datepicker__day--outside-month'))]"
        );
        click(dayLocator);
        return this;
    }

    @Step("Add subject: {value}")
    public DemoQaPracticeFormPage addSubject(String value) {
        scrollIntoView(subjectsInput);
        click(subjectsInput);
        el(subjectsInput).sendKeys(value);
        el(subjectsInput).sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Select hobby: Sports")
    public DemoQaPracticeFormPage selectSportsHobby() {
        click(hobbiesSportsLabel);
        return this;
    }

    @Step("Set current address: {value}")
    public DemoQaPracticeFormPage setCurrentAddress(String value) {
        type(currentAddress, value);
        return this;
    }

    @Step("Submit the form")
    public DemoQaPracticeFormPage submit() {
        scrollIntoView(submitButton);
        click(submitButton);
        return this;
    }

    @Step("Wait for result modal to appear")
    public DemoQaPracticeFormPage waitForResultModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));
        return this;
    }

    @Step("Get result modal text")
    public String getResultModalText() {
        return el(modalContent).getText();
    }

    @Step("Close result modal")
    public DemoQaPracticeFormPage closeResultModal() {
        click(closeLargeModal);
        return this;
    }
}


