package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertsPage extends BasePage {
    private static final String URL = "https://demo.automationtesting.in/Alerts.html";

    private final By tabAlertWithTextbox = By.xpath("//a[normalize-space()='Alert with Textbox']");
    private final By buttonPrompt = By.xpath("//button[contains(text(),'click the button to demonstrate the prompt box')]");
    private final By resultText = By.id("demo1");

    public AlertsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Alerts page")
    public AlertsPage open() {
        open(URL);
        // The site occasionally shows an unexpected alert on load; close it to keep the test stable.
        try {
            Alert unexpected = new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.alertIsPresent());
            unexpected.accept();
        } catch (TimeoutException ignored) {
            // no unexpected alert
        }
        return this;
    }

    @Step("Open 'Alert with Textbox' tab")
    public AlertsPage openAlertWithTextboxTab() {
        click(tabAlertWithTextbox);
        return this;
    }

    @Step("Trigger prompt alert")
    public AlertsPage triggerPromptAlert() {
        click(buttonPrompt);
        return this;
    }

    @Step("Fill alert prompt with full name: {fullName} and accept")
    public AlertsPage fillPromptAndAccept(String fullName) {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.sendKeys(fullName);
        alert.accept();
        return this;
    }

    @Step("Read result text under the prompt")
    public String readPromptResult() {
        return el(resultText).getText();
    }
}


