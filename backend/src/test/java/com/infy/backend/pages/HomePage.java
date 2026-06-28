package com.infy.backend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By homePageUniqueElement = By.cssSelector("body");
    private final By userMenuButton = By.cssSelector(".user-button");
    private final By dropdownMenu = By.cssSelector(".dropdown-menu");
    private final By profileButton = By
            .xpath("//div[contains(@class,'dropdown-menu')]//button[contains(text(),'Profile')]");
    private final By myOrdersButton = By
            .xpath("//div[contains(@class,'dropdown-menu')]//button[contains(text(),'My Orders')]");
    private final By adminDashboardButton = By
            .xpath("//div[contains(@class,'dropdown-menu')]//button[contains(text(),'Admin Dashboard')]");
    private final By logoutButton = By
            .xpath("//div[contains(@class,'dropdown-menu')]//button[contains(text(),'Logout')]");

    public HomePage(WebDriver driver) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void waitForHomePage() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.urlContains("/home"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(homePageUniqueElement));
    }

    public boolean isAtHomePage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return driver.getCurrentUrl().contains("/home");
    }

    public String getCurrentUrl() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void openUserMenu() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(userMenuButton));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu));
    }

    public boolean isDropdownVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu)).isDisplayed();
    }

    public boolean isLogoutButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton)).isDisplayed();
    }

    public void clickLogout() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
    }

    public boolean isProfileButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(profileButton)).isDisplayed();
    }

    public boolean isMyOrdersButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(myOrdersButton)).isDisplayed();
    }

    public boolean isAdminDashboardButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(adminDashboardButton)).isDisplayed();
    }
}