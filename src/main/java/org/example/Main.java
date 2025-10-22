package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriver driver = new ChromeDriver();

        driver.get("http://the-internet.herokuapp.com/dynamic_controls");

        driver.findElement(By.xpath("//button[text()='Enable']")).click();
        Thread.sleep(2000);

        if (driver.findElement(By.xpath("//input[@type='text']")).isEnabled() &&
                driver.findElement(By.id("message")).getText().equals("It's enabled!")) {
            System.out.println("Input field enabled and text visible");
        }

        if (driver.findElement(By.xpath("//button[text()='Disable']")).getText().equals("Disable")) {
            System.out.println("Button text changed successfully");
        }

        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("Bootcamp");
        driver.findElement(By.xpath("//input[@type='text']")).clear();

        driver.get("http://the-internet.herokuapp.com/drag_and_drop");

        if (driver.findElement(By.id("column-a")).getLocation().getY() ==
                driver.findElement(By.id("column-b")).getLocation().getY()) {
            System.out.println("Columns A and B aligned successfully");
        }

        driver.quit();
    }
}
