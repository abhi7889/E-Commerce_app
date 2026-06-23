package com.infy.backend.tests;

import com.infy.backend.base.BaseTest;
import com.infy.backend.pages.HomePage;
import com.infy.backend.pages.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest extends BaseTest {

    @Test
    public void validUserLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        homePage.waitForHomePage();

        Assertions.assertTrue(homePage.isAtHomePage(), "User should be redirected to home page");
        Assertions.assertTrue(homePage.getCurrentUrl().contains("/home"), "URL should contain /home");
        Assertions.assertNotNull(homePage.getPageTitle(), "Home page title should not be null");
    }

    @Test
    public void invalidUserLoginTest() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open(baseUrl);
        loginPage.login("wronguser@gmail.com", "wrongpassword");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                org.openqa.selenium.By.cssSelector(".error-message")));

        Assertions.assertTrue(loginPage.isLoginPageDisplayed(), "User should remain on login page");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"), "URL should still contain /login");
        Assertions.assertFalse(loginPage.getErrorMessage().isEmpty(), "Error message should be displayed");
        Assertions.assertEquals("Login failed", loginPage.getErrorMessage());
    }
}