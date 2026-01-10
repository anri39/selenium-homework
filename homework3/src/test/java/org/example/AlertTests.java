package org.example;

import io.qameta.allure.*;
import org.example.core.AllureTestListener;
import org.example.core.BaseTest;
import org.example.core.DriverManager;
import org.example.pages.AlertsPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(AllureTestListener.class)
@Epic("UI Tests")
@Feature("JavaScript Alerts")
@Owner("student")
public class AlertTests extends BaseTest {

    @Test(description = "Verify prompt alert accepts text input and displays it on the page")
    @Story("Prompt alert")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Opens the alerts demo, triggers the prompt alert, inputs a full name, accepts, and verifies the result text.")
    public void alertWithTextboxTest() {
        String fullName = "John Doe";

        AlertsPage page = new AlertsPage(DriverManager.getDriver());
        String resultText = page.open()
                .openAlertWithTextboxTab()
                .triggerPromptAlert()
                .fillPromptAndAccept(fullName)
                .readPromptResult();

        Assert.assertTrue(resultText.contains(fullName), "Result text does not contain entered name!");
    }
}
