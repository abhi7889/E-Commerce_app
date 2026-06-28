package com.infy.backend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartTitle = By.cssSelector(".cart-title");
    private final By cartCards = By.cssSelector(".cart-card");
    private final By cartItemName = By.cssSelector(".cart-info h3");
    private final By cartItemPrice = By.cssSelector(".cart-price");
    private final By quantityValue = By.cssSelector(".qty-box span");
    private final By minusButtons = By.xpath("//div[contains(@class,'qty-box')]//button[text()='-']");
    private final By plusButtons = By.xpath("//div[contains(@class,'qty-box')]//button[text()='+']");
    private final By lineTotals = By.cssSelector(".line-total");
    private final By summaryTotalItems = By.xpath("//div[contains(@class,'summary-row')][1]/span[2]");
    private final By summarySubtotal = By.xpath("//div[contains(@class,'summary-row')][2]//strong");
    private final By removeButtons = By
            .xpath("//div[contains(@class,'cart-actions')]//button[contains(text(),'Remove')]");
    private final By toastMessage = By.cssSelector(".cart-toast");
    private final By emptyCart = By.cssSelector(".empty-cart");
    private final By checkoutButton = By.cssSelector(".checkout-btn");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void openCart(String baseUrl) {
        driver.get(baseUrl + "/cart");
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    public void waitForCartPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitle));
    }

    public int getCartItemCount() {
        List<WebElement> items = driver.findElements(cartCards);
        return items.size();
    }

    public String getFirstCartItemName() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItemName))
                .get(0).getText().trim();
    }

    public String getFirstCartItemPrice() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItemPrice))
                .get(0).getText().trim();
    }

    public int getFirstCartItemQuantity() {
        String quantity = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(quantityValue))
                .get(0).getText().trim();
        return Integer.parseInt(quantity);
    }

    public String getFirstLineTotal() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(lineTotals))
                .get(0).getText().trim();
    }

    public String getSummaryTotalItems() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(summaryTotalItems)).getText().trim();
    }

    public String getSummarySubtotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(summarySubtotal)).getText().trim();
    }

    public void clickFirstPlusButton() {
        wait.until(ExpectedConditions.elementToBeClickable(plusButtons)).click();
    }

    public void clickFirstMinusButton() {
        wait.until(ExpectedConditions.elementToBeClickable(minusButtons)).click();
    }

    public void clickFirstRemoveButton() {
        wait.until(ExpectedConditions.elementToBeClickable(removeButtons)).click();
    }

    public String getToastMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessage)).getText().trim();
    }

    public boolean isCheckoutButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton)).isDisplayed();
    }

    public boolean isEmptyCartVisible() {
        List<WebElement> items = driver.findElements(emptyCart);
        return !items.isEmpty() && items.get(0).isDisplayed();
    }

    public String normalizePrice(String text) {
        return text.replace("₹", "").trim();
    }
}