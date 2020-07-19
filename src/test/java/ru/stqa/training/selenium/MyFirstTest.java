package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        driver.findElement(By.xpath("//input[@type=\"text\"]")).sendKeys("admin");
        driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@value=\"Login\"]")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        List<String> countriesNames = new ArrayList<>();
        //Получаем строки со странами
        List<WebElement> countries = driver.findElements(By.xpath("//table[@class=\"dataTable\"]//tr[@class=\"row\"]"));
        for (int i = 0; i < countries.size(); i++){
            //Обновляем список строк
            List<WebElement> updatedCountries = driver.findElements(By.xpath("//table[@class=\"dataTable\"]//tr[@class=\"row\"]"));
            countriesNames.add(updatedCountries.get(i).findElement(By.xpath("./td[5]")).getText());
            if (Integer.parseInt(updatedCountries.get(i).findElement(By.xpath("./td[6]")).getText()) != 0) {
                updatedCountries.get(i).findElement(By.xpath(".//td[5]/a")).click(); //переходим на страницу страны
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                List<String> zonesNames = new ArrayList<>();
                List<WebElement> zones = driver.findElements(By.xpath("//table[@id=\"table-zones\"]//tr[@class=\"row\"]"));
                for (int j = 0; j < zones.size(); j++) {
                    List<WebElement> updatedZones = driver.findElements(By.xpath("//table[@id=\"table-zones\"]//tr[@class=\"row\"]"));
                    zonesNames.add(updatedZones.get(j).findElement(By.xpath(".//td[3]")).getText());
                }
                //Проверяем сортировку зон
                List<String> sortedZones = new ArrayList<>(zonesNames);
                Collections.sort(sortedZones);
                Assert.assertEquals(sortedZones, zonesNames);
                //Возвращаемся к стписку стран
                driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            }
        }
        // Проверяем сортировку стран
        List<String> sortedNames = new ArrayList<>(countriesNames);
        Collections.sort(sortedNames);
        Assert.assertEquals(sortedNames, countriesNames);
    }

    @Test
    public void geoZonesSortCheck() {
        //Проверяем сортировку на странице зон
        driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        driver.findElement(By.xpath("//input[@type=\"text\"]")).sendKeys("admin");
        driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys("admin");
        driver.findElement(By.xpath("//button[@value=\"Login\"]")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        List<WebElement> countriesWithGeoZones= driver.findElements(By.xpath("//table[@class=\"dataTable\"]//tr[@class=\"row\"]"));
        for (int i = 0; i < countriesWithGeoZones.size(); i++) {
            List<WebElement> updatedCountriesWithGeoZones= driver.findElements(By.xpath("//table[@class=\"dataTable\"]//tr[@class=\"row\"]"));
            updatedCountriesWithGeoZones.get(i).findElement(By.tagName("a")).click();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            List<String> geoZonesNames = new ArrayList<>();
            List<WebElement> geoZones = driver.findElements(By.xpath("//select[not(contains(@aria-hidden,'true'))]"));
            for (int j = 0; j < geoZones.size(); j++) {
                List<WebElement> updatedGeoZones = driver.findElements(By.xpath("//select[not(contains(@aria-hidden,'true'))]"));
                geoZonesNames.add(updatedGeoZones.get(j).getAttribute("textContent"));
            }
            // Проверяем сортировку геозон и возвращаемся к списку стран
            List<String> sortedZonesNames = new ArrayList<>(geoZonesNames);
            Collections.sort(sortedZonesNames);
            Assert.assertEquals(geoZonesNames, sortedZonesNames);
            driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        }
    }

    @After
    public void stop(){
        driver.quit();
        driver= null;
    }
}