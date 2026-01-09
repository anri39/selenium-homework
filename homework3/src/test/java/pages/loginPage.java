package pages;

import org.openqa.selenium.*;

public class LoginPage {

    WebDriver driver;

    By user = By.id("username");
    By pass = By.id("password");
    By btn = By.id("login");

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    public void enterUsername(String u){
        driver.findElement(user).sendKeys(u);
    }

    public void enterPassword(String p){
        driver.findElement(pass).sendKeys(p);
    }

    public void clickLogin(){
        driver.findElement(btn).click();
    }
}
