package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class MyFirstTest {

    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void start(){
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void myFirstTest() {
        driver.get("http://localhost/litecart/");
        wait.until(titleIs("My Store | Online Store"));
        int products = driver.findElements(By.xpath("//div[@class=\"image-wrapper\"]")).size();
        int stickers = driver.findElements(By.xpath("//div[@class=\"image-wrapper\"]/div[contains(@class, 'sticker')]")).size();
        Assert.assertTrue(products == stickers);
    }

    @After
    public void stop(){
        driver.quit();
        driver= null;
    }
}