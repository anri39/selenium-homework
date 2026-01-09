package pages;

import org.openqa.selenium.*;

public class HomePage {

    WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
    }

    public boolean isDisplayed(){
        return driver.getTitle().contains("Home");
    }
}
