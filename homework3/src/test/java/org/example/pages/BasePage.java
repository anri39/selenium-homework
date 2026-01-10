package org.example.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Supplier;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Open URL: {url}")
    public void open(String url) {
        Allure.step("Open URL: " + url, () -> driver.get(url));
    }

    protected void dismissAlertIfPresent() {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignored) {
            // no-op
        }
    }

    private <T> T retryOnUnexpectedAlert(Supplier<T> action) {
        UnhandledAlertException last = null;
        for (int i = 0; i < 3; i++) {
            try {
                return action.get();
            } catch (UnhandledAlertException e) {
                last = e;
                dismissAlertIfPresent();
            }
        }
        if (last != null) {
            throw last;
        }
        return action.get();
    }

    protected WebElement el(By locator) {
        return retryOnUnexpectedAlert(() -> {
            dismissAlertIfPresent();
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        });
    }

    protected void click(By locator) {
        Allure.step("Click: " + locator, () -> retryOnUnexpectedAlert(() -> {
            dismissAlertIfPresent();
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollIntoView(locator);
            element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            try {
                element.click();
            } catch (WebDriverException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
            return null;
        }));
    }

    protected void type(By locator, String value) {
        Allure.step("Type into: " + locator, () -> retryOnUnexpectedAlert(() -> {
            dismissAlertIfPresent();
            scrollIntoView(locator);
            WebElement element = el(locator);
            try {
                element.click();
            } catch (WebDriverException ignored) {
                // best-effort focus
            }
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            element.sendKeys(Keys.BACK_SPACE);
            element.sendKeys(value);
            return null;
        }));
    }

    protected void scrollIntoView(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}


