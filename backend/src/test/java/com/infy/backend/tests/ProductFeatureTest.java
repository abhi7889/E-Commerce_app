package com.infy.backend.tests;

import com.infy.backend.base.BaseTest;
import com.infy.backend.pages.CartPage;
import com.infy.backend.pages.CartSearchPage;
import com.infy.backend.pages.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductFeatureTest extends BaseTest {

    private final String email = "abhishek131214@gmail.com";
    private final String password = "Abhi@20113122";

    @Test
    public void testSearchFunctionality() {
        LoginPage loginPage = new LoginPage(driver);
        CartSearchPage productPage = new CartSearchPage(driver);

        loginPage.open(baseUrl);
        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();
        productPage.searchProduct("Mouse");

        Assertions.assertEquals("Mouse", productPage.getSearchValue(),
                "Search box should contain entered text");
        Assertions.assertTrue(productPage.getProductCount() > 0,
                "Search results should show at least one product");
        Assertions.assertTrue(productPage.areAllVisibleProductsRelatedToSearch("Mouse"),
                "Visible products should match search text");
    }

    @Test
    public void validateFilteringTest() {
        LoginPage loginPage = new LoginPage(driver);
        CartSearchPage productPage = new CartSearchPage(driver);

        loginPage.open(baseUrl);
        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();
        productPage.selectCategory("All");

        Assertions.assertEquals("All", productPage.getSelectedCategory(),
                "Selected category should be All");
        Assertions.assertTrue(productPage.getProductCount() > 0,
                "Products should be visible after category selection");
    }

    @Test
    public void validateProductDetailsPageTest() {
        LoginPage loginPage = new LoginPage(driver);
        CartSearchPage productPage = new CartSearchPage(driver);

        loginPage.open(baseUrl);
        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();

        String listingTitle = productPage.getFirstProductTitle();
        String listingPrice = productPage.getFirstProductPrice();

        productPage.clickFirstProduct();
        productPage.waitForProductDetailsPage();

        Assertions.assertFalse(productPage.getDetailsTitle().isEmpty(),
                "Details title should not be empty");
        Assertions.assertFalse(productPage.getDetailsPrice().isEmpty(),
                "Details price should not be empty");
        Assertions.assertFalse(productPage.getDetailsDescription().isEmpty(),
                "Details description should not be empty");

        Assertions.assertEquals(listingTitle, productPage.getDetailsTitle(),
                "Listing title and details title should match");

        Assertions.assertEquals(
                productPage.normalizePrice(listingPrice),
                productPage.normalizePrice(productPage.getDetailsPrice()),
                "Listing price and details price should match");
    }

    @Test
    public void automateAddToCartFunctionalityFromDetailsTest() {
        LoginPage loginPage = new LoginPage(driver);
        CartSearchPage productPage = new CartSearchPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.open(baseUrl);
        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();
        String productName = productPage.getFirstProductTitle();

        productPage.clickFirstProduct();
        productPage.waitForProductDetailsPage();
        productPage.clickAddToCartOnDetails();

        String toast = productPage.getToastMessage();
        Assertions.assertTrue(toast.contains("added to cart"),
                "Add to cart confirmation should be shown");

        cartPage.openCart(baseUrl);
        cartPage.waitForCartPage();

        Assertions.assertTrue(cartPage.getCartItemCount() > 0,
                "Cart should contain at least one item");
        Assertions.assertEquals(productName, cartPage.getFirstCartItemName(),
                "Added product should appear in cart");
    }

    @Test
    public void validateProductAdditionFromListingTest() {
        LoginPage loginPage = new LoginPage(driver);
        CartSearchPage productPage = new CartSearchPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.open(baseUrl);
        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();
        String productName = productPage.getFirstProductTitle();

        productPage.clickFirstProductCartIcon();

        String toast = productPage.getToastMessage();
        Assertions.assertTrue(toast.contains("added to cart"),
                "Toast should confirm product addition");

        cartPage.openCart(baseUrl);
        cartPage.waitForCartPage();

        Assertions.assertTrue(cartPage.getFirstCartItemName().contains(productName),
                "Cart should show the added product");
    }

    @Test
    public void validateCartItemsTest() {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.open(baseUrl);
        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        cartPage.openCart(baseUrl);
        cartPage.waitForCartPage();

        Assertions.assertTrue(cartPage.getCartItemCount() > 0,
                "Cart should contain items");
        Assertions.assertFalse(cartPage.getFirstCartItemName().isEmpty(),
                "Cart item name should not be empty");
        Assertions.assertFalse(cartPage.getFirstCartItemPrice().isEmpty(),
                "Cart item price should not be empty");
        Assertions.assertTrue(cartPage.isCheckoutButtonDisplayed(),
                "Checkout button should be visible");
    }

    @Test
    public void verifyQuantityAndPriceTest() {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.open(baseUrl);
        loginPage.login(email, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        cartPage.openCart(baseUrl);
        cartPage.waitForCartPage();

        int oldQuantity = cartPage.getFirstCartItemQuantity();
        cartPage.clickFirstPlusButton();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int newQuantity = cartPage.getFirstCartItemQuantity();

        Assertions.assertTrue(newQuantity == oldQuantity + 1,
                "Quantity should increase by 1");

        Assertions.assertFalse(cartPage.getFirstLineTotal().isEmpty(),
                "Line total should be visible");
        Assertions.assertFalse(cartPage.getSummarySubtotal().isEmpty(),
                "Subtotal should be visible");
        Assertions.assertFalse(cartPage.getSummaryTotalItems().isEmpty(),
                "Total items should be visible");
    }
}