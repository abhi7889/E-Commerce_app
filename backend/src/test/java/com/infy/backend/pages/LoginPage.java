package com.infy.backend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailInput = By.cssSelector("input[placeholder='Email']");
    private final By passwordInput = By.cssSelector("input[placeholder='Password']");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.cssSelector(".error-message");
    private final By loginHeading = By.cssSelector(".right--text");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/login");

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
    }

    public void enterEmail(String email) {
        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(loginButton));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clickLogin();
    }

    public boolean isLoginPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginHeading)).isDisplayed();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebElement errorElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return errorElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageIfPresent() {
        try {
            WebElement errorElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return errorElement.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getEmailValidationMessage() {
        return driver.findElement(emailInput).getAttribute("validationMessage");
    }

    public String getPasswordValidationMessage() {
        return driver.findElement(passwordInput).getAttribute("validationMessage");
    }
}