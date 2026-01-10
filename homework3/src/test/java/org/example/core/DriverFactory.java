package org.example.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        // Allows running in CI / headless environments:
        // mvn test -Dheadless=true
        String headlessProp = System.getProperty("headless", "false");
        if ("true".equalsIgnoreCase(headlessProp)) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1920,1080");
        return new ChromeDriver(options);
    }
}


