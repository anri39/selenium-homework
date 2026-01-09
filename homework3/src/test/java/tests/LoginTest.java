package tests;

import base.BaseTest;
import pages.*;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    @Description("Valid login test")
    @Epic("Authentication")
    @Feature("Login")
    @Story("User logs in successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void loginTest(){

        LoginPage lp = new LoginPage(driver);
        HomePage hp = new HomePage(driver);

        lp.enterUsername("admin");
        lp.enterPassword("1234");
        lp.clickLogin();

        Assert.assertTrue(hp.isDisplayed());
    }
}
