package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class FormTests {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void fillFormAndVerifyPopup() {
        driver.get("https://demoqa.com/automation-practice-form");

        driver.findElement(By.id("firstName")).sendKeys("John");
        driver.findElement(By.id("lastName")).sendKeys("Doe");
        driver.findElement(By.id("userEmail")).sendKeys("john.doe@example.com");

        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();

        driver.findElement(By.id("userNumber")).sendKeys("1234567890");

        driver.findElement(By.id("dateOfBirthInput")).sendKeys("10 Oct 1990\n");

        driver.findElement(By.id("subjectsInput")).sendKeys("Maths\n");

        driver.findElement(By.cssSelector("label[for='hobbies-checkbox-1']")).click();

        driver.findElement(By.id("currentAddress")).sendKeys("123 Test Street");


        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", driver.findElement(By.id("submit"))
        );

        driver.findElement(By.id("submit")).click();


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("modal-content")));


        String popupText = driver.findElement(By.className("modal-content")).getText();
        Assert.assertTrue(popupText.contains("John Doe"));
        Assert.assertTrue(popupText.contains("john.doe@example.com"));
        Assert.assertTrue(popupText.contains("Male"));
        Assert.assertTrue(popupText.contains("1234567890"));
        Assert.assertTrue(popupText.contains("10 October,1990") || popupText.contains("10 Oct 1990"));
        Assert.assertTrue(popupText.contains("Maths"));
        Assert.assertTrue(popupText.contains("Sports"));
        Assert.assertTrue(popupText.contains("123 Test Street"));


        driver.findElement(By.id("closeLargeModal")).click();
    }
}
