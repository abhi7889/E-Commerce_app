package com.infy.backend.tests;

import com.infy.backend.base.BaseTest;
import com.infy.backend.pages.LoginPage;
import com.infy.backend.pages.ProductPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductTest extends BaseTest {

    @Test
    public void automateProductListingPageTest() {
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = new ProductPage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"),
                "User should be on home page after login");
        Assertions.assertTrue(productPage.isProductGridDisplayed(),
                "Product grid should be displayed");
    }

    @Test
    public void validateProductVisibilityTest() {
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = new ProductPage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productPage.waitForProductsToLoad();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(productPage.isAtLeastOneProductVisible(),
                "At least one product should be visible");
        Assertions.assertTrue(productPage.getProductCount() > 0,
                "Product count should be greater than zero");
    }

    @Test
    public void validateProductDetailsTest() {
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = new ProductPage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();

        String listingTitle = productPage.getFirstProductTitle();
        String listingPrice = productPage.getFirstProductPrice();

        productPage.clickFirstProduct();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productPage.waitForProductDetailsPage();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(productPage.isProductDetailsDisplayed(),
                "Product details page should be displayed");

        Assertions.assertFalse(productPage.getDetailsTitle().isEmpty(),
                "Product title should not be empty on details page");

        Assertions.assertFalse(productPage.getDetailsPrice().isEmpty(),
                "Product price should not be empty on details page");

        Assertions.assertEquals(listingTitle, productPage.getDetailsTitle(),
                "Product title should match between listing and details page");

        Assertions.assertEquals(listingPrice, productPage.getDetailsPrice(),
                "Product price should match between listing and details page");
    }

    @Test
    public void verifyProductNavigationTest() {
        LoginPage loginPage = new LoginPage(driver);
        ProductPage productPage = new ProductPage(driver);

        loginPage.open(baseUrl);
        loginPage.login("abhishek131214@gmail.com", "Abhi@20113122");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("/home"));

        productPage.waitForProductsToLoad();
        productPage.clickFirstProduct();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productPage.waitForProductDetailsPage();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(productPage.isProductDetailsDisplayed(),
                "User should navigate to product details page");

        Assertions.assertTrue(driver.getCurrentUrl().contains("/products/"),
                "URL should contain /products/ after clicking a product");
    }
}