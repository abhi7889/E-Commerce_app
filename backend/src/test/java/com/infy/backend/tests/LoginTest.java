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
        Assertions.assertFalse(homePage.getPageTitle().trim().isEmpty(), "Home page title should not be empty");
    }

    @Test
    public void invalidUserLoginTest() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open(baseUrl);
        loginPage.login("wronguser@gmail.com", "wrongpassword");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(d -> d.getCurrentUrl().contains("/login") ||
                loginPage.isErrorMessageDisplayed());

        Assertions.assertTrue(loginPage.isLoginPageDisplayed(), "User should remain on login page");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"), "URL should still contain /login");
        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assertions.assertFalse(loginPage.getErrorMessageIfPresent().isEmpty(), "Error message should not be empty");
    }

    @Test
    public void emptyEmailShouldShowValidationMessage() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open(baseUrl);
        loginPage.enterPassword("somepassword");
        loginPage.clickLogin();

        String validationMessage = loginPage.getEmailValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Email validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User should remain on login page");
    }

    @Test
    public void emptyPasswordShouldShowValidationMessage() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open(baseUrl);
        loginPage.enterEmail("testuser@gmail.com");
        loginPage.clickLogin();

        String validationMessage = loginPage.getPasswordValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Password validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User should remain on login page");
    }

    @Test
    public void invalidEmailFormatShouldShowValidationMessage() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open(baseUrl);
        loginPage.enterEmail("invalid-email");
        loginPage.enterPassword("somepassword");
        loginPage.clickLogin();

        String validationMessage = loginPage.getEmailValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Invalid email format validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User should remain on login page");
    }
}