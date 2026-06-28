package com.infy.backend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartSearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchInput = By.cssSelector(".header-search input[placeholder='Search products...']");
    private final By categorySelect = By.cssSelector(".category-select");
    private final By productsGrid = By.cssSelector(".products-grid");
    private final By productCards = By.cssSelector(".productCard");
    private final By productTitles = By.cssSelector(".productTitle");
    private final By productPrices = By.cssSelector(".productPrice");
    private final By cartIcons = By.cssSelector(".cartIcon");
    private final By toastMessage = By.cssSelector(".cart-toast");

    private final By detailsContainer = By.cssSelector(".product-details-container");
    private final By detailsTitle = By.cssSelector(".details-info h1");
    private final By detailsPrice = By.cssSelector(".details-price");
    private final By detailsDescription = By.cssSelector(".details-description");
    private final By addToCartButton = By.cssSelector(".add-cart-btn");
    private final By backButton = By.cssSelector(".back-btn");

    public CartSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void openHome(String baseUrl) {
        driver.get(baseUrl + "/home");
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.urlContains("/home"));
    }

    public void waitForProductsToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsGrid));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
    }

    public int getProductCount() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards)).size();
    }

    public List<WebElement> getVisibleProductCards() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
    }

    public String getFirstProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productTitles))
                .get(0).getText().trim();
    }

    public String getFirstProductPrice() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productPrices))
                .get(0).getText().trim();
    }

    public void searchProduct(String text) {
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        search.clear();
        search.sendKeys(text);
        search.sendKeys(Keys.TAB);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getSearchValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput))
                .getAttribute("value");
    }

    public void selectCategory(String category) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(categorySelect));
        Select select = new Select(dropdown);
        select.selectByVisibleText(category);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getSelectedCategory() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(categorySelect));
        Select select = new Select(dropdown);
        return select.getFirstSelectedOption().getText().trim();
    }

    public boolean areAllVisibleProductsRelatedToSearch(String searchText) {
        List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        String expected = searchText.toLowerCase();

        for (WebElement card : cards) {
            String cardText = card.getText().toLowerCase();
            if (!cardText.contains(expected)) {
                return false;
            }
        }
        return true;
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

    public void clickFirstProductCartIcon() {
        List<WebElement> icons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartIcons));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        icons.get(0).click();
    }

    public void waitForProductDetailsPage() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsContainer));
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

    public void clickAddToCartOnDetails() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();
    }

    public String getToastMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessage)).getText().trim();
    }

    public void clickBackButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(backButton));
        button.click();
    }

    public String normalizePrice(String text) {
        return text.replace("Price", "")
                .replace(":", "")
                .replace("₹", "")
                .trim();
    }
}