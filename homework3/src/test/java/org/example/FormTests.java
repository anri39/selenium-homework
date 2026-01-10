package org.example;

import io.qameta.allure.*;
import org.example.core.AllureTestListener;
import org.example.core.BaseTest;
import org.example.core.DriverManager;
import org.example.pages.DemoQaPracticeFormPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(AllureTestListener.class)
@Epic("UI Tests")
@Feature("Forms")
@Owner("student")
public class FormTests extends BaseTest {

    @Test(description = "Fill practice form and verify submission modal contains entered values")
    @Story("Practice Form submission")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Fills out the DemoQA practice form, submits it, and asserts that the result modal contains expected values.")
    public void fillFormAndVerifyPopup() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String mobile = "1234567890";
        String subject = "Maths";
        String address = "123 Test Street";

        DemoQaPracticeFormPage page = new DemoQaPracticeFormPage(DriverManager.getDriver());
        String popupText = page.open()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .selectMaleGender()
                .setMobileNumber(mobile)
                .setDateOfBirth(10, "October", "1990")
                .addSubject(subject)
                .selectSportsHobby()
                .setCurrentAddress(address)
                .submit()
                .waitForResultModal()
                .getResultModalText();

        Assert.assertTrue(popupText.contains(firstName + " " + lastName));
        Assert.assertTrue(popupText.contains(email));
        Assert.assertTrue(popupText.contains("Male"));
        Assert.assertTrue(popupText.contains(mobile));
        Assert.assertTrue(popupText.contains("10 October,1990") || popupText.contains("10 Oct 1990"));
        Assert.assertTrue(popupText.contains(subject));
        Assert.assertTrue(popupText.contains("Sports"));
        Assert.assertTrue(popupText.contains(address));

        page.closeResultModal();
    }
}
