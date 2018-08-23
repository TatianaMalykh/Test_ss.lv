import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.*;
import java.io.IOException;
import org.openqa.selenium.NoSuchElementException;


import java.lang.reflect.Method;


public class TestAdverts {

    private static WebDriver driver;

    @BeforeClass
    public static void createDriver() throws IOException {
        System.setProperty("webdriver.chrome.driver", "C:/server/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterClass
    public static void quitDriver() {
        driver.quit();
    }

    @Test
    public void test() throws Exception {

        driver.get("https://ss.lv");

        fillClick("linkText","RU", "");
        fillClick("linkText","Электротехника", "");
        fillClick("linkText","Поиск", "");
        fillClick("id","ptxt", "Компьютеры");
        fillClick("name","search_region", "Рижский р-он");
        submit("sbtn");

        fillClick("linkText","Цена", "");
        fillClick("name","sid", "Продают");
        fillClick("linkText","Расширенный поиск", "");
        fillClick("name","sid", "Продают");
        fillClick("name","topt[8][min]", "0");
        fillClick("name","topt[8][max]", "300");
        fillClick("name","sort", "Цена");
        submit("sbtn");

        List advertsId =  getIdAdverts();

        if(advertsId.size()>=3){
            Collections.shuffle(advertsId);
            advertsId.subList(advertsId.size() - (advertsId.size() - 3), advertsId.size()).clear();

            JavascriptExecutor js = (JavascriptExecutor)driver;
            for (int i = 0; i < advertsId.size(); i++) {
                js.executeScript("document.getElementById('"+advertsId.get(i)+"').click();");
            }
            js.executeScript("document.getElementById('a_fav_sel').click();");
        }else{
            System.out.println("Count of adverts is less than 3.");
        }

        fillClick("linkText","Закладки", "");

        List advertsIdSelected =  getIdAdverts();

        if(advertsIdSelected.size()==3 && advertsIdSelected.containsAll(advertsId)){
            System.out.println("It's Ok.");
        }else{
            System.out.println("Error");
        }

        System.out.println("advertsId: "+advertsId);
        System.out.println("advertsIdSelected: "+advertsIdSelected);
    }

    private List getIdAdverts() throws Exception {

        List<String> adverts = new ArrayList<String>();
        List<WebElement> allElements = driver.findElements(By.xpath("//input[@name='mid[]']"));

        for (WebElement element : allElements) {
            String advert = element.getAttribute("id");
            adverts.add(advert);
        }
        return adverts;
    }

    private void fillClick(String mode, String parameter, String value) throws Exception {
        try {
            Method method = By.class.getMethod(mode, String.class);
            WebElement element = driver.findElement((By) method.invoke(null, parameter));

            if (!value.isEmpty()) {
                element.sendKeys(value);
            } else {
                element.click();
            }
        } catch (NoSuchElementException e) {
            System.out.print("WebElement " + mode + " was not found " + parameter);
            throw e;
        } catch (Exception e) {
            System.out.print("Cannot fill or click element" + parameter);
            throw e;
        }
    }

    private void submit(String parameter) throws Exception {
        try {
            driver.findElement(By.id(parameter)).submit();

        } catch (NoSuchElementException e) {
            System.out.print("WebElement " + parameter + " was not found");
            throw e;
        } catch (Exception e) {
            System.out.print("Cannot subbmit " + parameter);
            throw e;
        }
    }
}
