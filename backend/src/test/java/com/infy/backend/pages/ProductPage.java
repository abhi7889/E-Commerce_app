package com.infy.backend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productsGrid = By.cssSelector(".products-grid");
    private final By productCards = By.cssSelector(".productCard");
    private final By productTitles = By.cssSelector(".productTitle");
    private final By productPrices = By.cssSelector(".productPrice");

    private final By detailsContainer = By.cssSelector(".product-details-container");
    private final By detailsTitle = By.cssSelector(".details-info h1");
    private final By detailsPrice = By.cssSelector(".details-price");
    private final By detailsDescription = By.cssSelector(".details-description");
    private final By backButton = By.cssSelector(".back-btn");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void openHome(String baseUrl) {
        driver.get(baseUrl + "/home");

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        wait.until(ExpectedConditions.urlContains("/home"));
    }

    public void waitForProductsToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsGrid));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productCards));
    }

    public boolean isProductGridDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productsGrid)).isDisplayed();
    }

    public int getProductCount() {
        List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        return cards.size();
    }

    public boolean isAtLeastOneProductVisible() {
        List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        return !cards.isEmpty() && cards.get(0).isDisplayed();
    }

    public String getFirstProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productTitles))
                .get(0).getText().trim();
    }

    public String getFirstProductPrice() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productPrices))
                .get(0).getText().trim();
    }

    public void clickFirstProduct() {
        List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cards.get(0).click();
    }

    public void waitForProductDetailsPage() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsContainer));
    }

    public boolean isProductDetailsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(detailsContainer)).isDisplayed();
    }

    public String getDetailsTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(detailsTitle)).getText().trim();
    }

    public String getDetailsPrice() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(detailsPrice)).getText().trim();
    }

    public String getDetailsDescription() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(detailsDescription)).getText().trim();
    }

    public void clickBackButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(backButton));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
    }
}