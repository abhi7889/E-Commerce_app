package com.infy.backend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nameInput = By.cssSelector("input[placeholder='Name']");
    private final By emailInput = By.cssSelector("input[placeholder='Email']");
    private final By passwordInput = By.cssSelector("input[placeholder='Password']");
    private final By termsCheckbox = By.cssSelector(".chk-box input[type='checkbox']");
    private final By createAccountButton = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.cssSelector(".error-message");
    private final By successMessage = By.cssSelector(".success-message");
    private final By registerHeading = By.cssSelector(".right--text");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/register");

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
    }

    public void enterName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        field.clear();
        field.sendKeys(name);
    }

    public void enterEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        field.clear();
        field.sendKeys(password);
    }

    public void acceptTerms() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void uncheckTermsIfSelected() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickCreateAccount() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(createAccountButton));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
    }

    public void register(String name, String email, String password) {
        enterName(name);
        enterEmail(email);
        enterPassword(password);
        acceptTerms();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clickCreateAccount();
    }

    public boolean isRegisterPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(registerHeading)).isDisplayed();
    }

    public boolean isErrorMessageDisplayed() {
        List<WebElement> elements = driver.findElements(errorMessage);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    public String getErrorMessageIfPresent() {
        List<WebElement> elements = driver.findElements(errorMessage);
        if (elements.isEmpty()) {
            return "";
        }
        return elements.get(0).getText().trim();
    }

    public String getNameValidationMessage() {
        return driver.findElement(nameInput).getAttribute("validationMessage");
    }

    public String getEmailValidationMessage() {
        return driver.findElement(emailInput).getAttribute("validationMessage");
    }

    public String getPasswordValidationMessage() {
        return driver.findElement(passwordInput).getAttribute("validationMessage");
    }

    public String getTermsValidationMessage() {
        return driver.findElement(termsCheckbox).getAttribute("validationMessage");
    }
}