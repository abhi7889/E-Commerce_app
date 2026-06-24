package com.infy.backend.tests;

import com.infy.backend.base.BaseTest;
import com.infy.backend.pages.RegisterPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterTest extends BaseTest {

    @Test
    public void validRegistrationTest() {
        RegisterPage registerPage = new RegisterPage(driver);

        String uniqueEmail = "user" + System.currentTimeMillis() + "@gmail.com";

        registerPage.open(baseUrl);
        registerPage.register("Test User", uniqueEmail, "password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/login"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "After successful registration, user should be redirected to login page");
    }

    @Test
    public void duplicateEmailRegistrationTest() {
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.open(baseUrl);
        registerPage.register("Existing User", "existinguser@gmail.com", "password123");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(d -> d.getCurrentUrl().contains("/login") ||
                d.getCurrentUrl().contains("/register") ||
                registerPage.isErrorMessageDisplayed());

        if (driver.getCurrentUrl().contains("/login")) {
            Assertions.fail("Registration succeeded, so the email is probably not duplicate in the database.");
        }

        Assertions.assertTrue(driver.getCurrentUrl().contains("/register"),
                "User should remain on register page for duplicate email");

        Assertions.assertTrue(registerPage.isErrorMessageDisplayed(),
                "Error message should be displayed for duplicate registration");

        Assertions.assertFalse(registerPage.getErrorMessageIfPresent().isEmpty(),
                "Error message text should not be empty");
    }

    @Test
    public void emptyNameShouldShowValidationMessage() {
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.open(baseUrl);
        registerPage.enterEmail("testuser@gmail.com");
        registerPage.enterPassword("password123");
        registerPage.acceptTerms();
        registerPage.clickCreateAccount();

        String validationMessage = registerPage.getNameValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Name validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/register"),
                "User should remain on register page");
    }

    @Test
    public void emptyEmailShouldShowValidationMessage() {
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.open(baseUrl);
        registerPage.enterName("Test User");
        registerPage.enterPassword("password123");
        registerPage.acceptTerms();
        registerPage.clickCreateAccount();

        String validationMessage = registerPage.getEmailValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Email validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/register"),
                "User should remain on register page");
    }

    @Test
    public void emptyPasswordShouldShowValidationMessage() {
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.open(baseUrl);
        registerPage.enterName("Test User");
        registerPage.enterEmail("testuser@gmail.com");
        registerPage.acceptTerms();
        registerPage.clickCreateAccount();

        String validationMessage = registerPage.getPasswordValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Password validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/register"),
                "User should remain on register page");
    }

    @Test
    public void uncheckedTermsShouldShowValidationMessage() {
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.open(baseUrl);
        registerPage.enterName("Test User");
        registerPage.enterEmail("testuser@gmail.com");
        registerPage.enterPassword("password123");
        registerPage.uncheckTermsIfSelected();
        registerPage.clickCreateAccount();

        String validationMessage = registerPage.getTermsValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Checkbox validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/register"),
                "User should remain on register page");
    }

    @Test
    public void invalidEmailFormatShouldShowValidationMessage() {
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.open(baseUrl);
        registerPage.enterName("Test User");
        registerPage.enterEmail("invalid-email-format");
        registerPage.enterPassword("password123");
        registerPage.acceptTerms();
        registerPage.clickCreateAccount();

        String validationMessage = registerPage.getEmailValidationMessage();

        Assertions.assertFalse(validationMessage.isEmpty(),
                "Invalid email validation message should be shown");
        Assertions.assertTrue(driver.getCurrentUrl().contains("/register"),
                "User should remain on register page");
    }
}