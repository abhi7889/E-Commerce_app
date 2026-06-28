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
                loginPage.login("abhis31214@gmail.com", "wrongpassword");

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                wait.until(d -> loginPage.isErrorMessageDisplayed());

                String actualError = loginPage.getErrorMessageIfPresent();
                System.out.println("Visible login error on UI: " + actualError);
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                Assertions.assertTrue(loginPage.isLoginPageDisplayed(),
                                "User should remain on login page");
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                                "URL should still contain /login");

                Assertions.assertTrue(loginPage.isErrorMessageDisplayed(),
                                "Error message should be visible on the website");

                Assertions.assertEquals("Email or password is incorrect", actualError,
                                "Wrong login error message displayed on the UI");
        }

        @Test
        public void emptyEmailShouldShowValidationMessage() {
                LoginPage loginPage = new LoginPage(driver);

                loginPage.open(baseUrl);
                loginPage.enterPassword("somepassword");
                loginPage.clickLogin();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                String validationMessage = loginPage.getEmailValidationMessage();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
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
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                String validationMessage = loginPage.getPasswordValidationMessage();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
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
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                String validationMessage = loginPage.getEmailValidationMessage();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                Assertions.assertFalse(validationMessage.isEmpty(),
                                "Invalid email format validation message should be shown");
                Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                                "User should remain on login page");
        }
}