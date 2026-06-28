package com.infy.backend.tests;

import com.infy.backend.base.BaseTest;
import com.infy.backend.pages.HomePage;
import com.infy.backend.pages.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogoutSessionTest extends BaseTest {

    @Test
    public void validateLogoutFunctionality() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        homePage.waitForHomePage();

        Assertions.assertTrue(homePage.isAtHomePage(), "User should be redirected to home page");

        homePage.openUserMenu();

        Assertions.assertTrue(homePage.isDropdownVisible(), "User dropdown should be visible");
        Assertions.assertTrue(homePage.isLogoutButtonVisible(), "Logout button should be visible in dropdown");

        homePage.clickLogout();

        wait.until(ExpectedConditions.urlContains("/login"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "After logout, user should be redirected to login page");
    }

    @Test
    public void validateSessionHandlingAfterLogout() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        homePage.waitForHomePage();
        Assertions.assertTrue(homePage.isAtHomePage(), "User should be on home page after login");

        homePage.openUserMenu();
        homePage.clickLogout();

        wait.until(ExpectedConditions.urlContains("/login"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "After logout, user should be redirected to login page");

        driver.get(baseUrl + "/home");

        wait.until(d -> d.getCurrentUrl().contains("/login") || d.getCurrentUrl().contains("/home"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Logged out user should not be able to access home page directly");
    }

    @Test
    public void validateDropdownOptionsVisible() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        homePage.waitForHomePage();
        homePage.openUserMenu();

        Assertions.assertTrue(homePage.isDropdownVisible(), "Dropdown menu should be visible");
        Assertions.assertTrue(homePage.isProfileButtonVisible(), "Profile option should be visible");
        Assertions.assertTrue(homePage.isMyOrdersButtonVisible(), "My Orders option should be visible");
        Assertions.assertTrue(homePage.isAdminDashboardButtonVisible(), "Admin Dashboard option should be visible");
        Assertions.assertTrue(homePage.isLogoutButtonVisible(), "Logout option should be visible");
    }
}