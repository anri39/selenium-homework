package org.example;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class AlertTests {

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
    public void alertWithTextboxTest() {
        driver.get("https://demo.automationtesting.in/Alerts.html");


        driver.findElement(By.xpath("//a[text()='Alert with Textbox']")).click();


        driver.findElement(By.xpath("//button[contains(text(),'click the button to demonstrate the prompt box')]")).click();


        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String fullName = "John Doe";
        alert.sendKeys(fullName);
        alert.accept();


        String resultText = driver.findElement(By.id("demo1")).getText();
        Assert.assertTrue(resultText.contains(fullName), "Result text does not contain entered name!");
    }
}
