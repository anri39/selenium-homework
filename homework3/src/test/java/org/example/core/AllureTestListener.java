package org.example.core;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AllureTestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachArtifacts("failure");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        attachArtifacts("skipped");
    }

    @Override
    public void onStart(ITestContext context) {
        // no-op
    }

    @Override
    public void onFinish(ITestContext context) {
        // no-op
    }

    private void attachArtifacts(String reason) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            return;
        }

        try {
            if (driver instanceof TakesScreenshot ts) {
                byte[] png = ts.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot (" + reason + ")", "image/png", new ByteArrayInputStream(png), ".png");
            }
        } catch (Exception ignored) {
            // best-effort attachments only
        }

        try {
            byte[] html = driver.getPageSource().getBytes(StandardCharsets.UTF_8);
            Allure.addAttachment("Page Source (" + reason + ")", "text/html", new ByteArrayInputStream(html), ".html");
        } catch (Exception ignored) {
            // best-effort attachments only
        }
    }
}


