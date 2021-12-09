import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ReviewTest {

    public WebDriver driver;
    public static final String URL = "https://www.hepsiburada.com.tr";
    public static final String DRIVER_NAME = "webdriver.chrome.driver";
    public static final String THANKS_TEXT = "Teşekkür Ederiz.";
    public static final String PRODUCT_NAME = "iphone";
    public static final List<String> SORT_REVIEWS_TYPES = Arrays.asList("En yeni değerlendirme",
            "En faydalı değerlendirme",
            "Puana göre azalan",
            "Puana göre artan");

    @Before
    public void beforeEachTest() {
        init();
    }

    @After
    public void afterEachTest() throws Exception {
        Thread.sleep(3000);
        driver.quit();
    }

    @Test
    public void likeProductFirstReviewTest() {
        searchProduct();
        moveToFirstProductDetail();
        moveToReviewsTab();
        if (isReviewExist()) {
            likeFirstReview();
        }
    }

    @Test
    public void clickSortReviewsDropboxTest() {
        searchProduct();
        moveToFirstProductDetail();
        moveToReviewsTab();
        if (isReviewExist()) {
            clickDropbox();
        }
    }

    public void init() {

        System.setProperty(DRIVER_NAME, System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--ignore-certifcate-errors");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.setAcceptInsecureCerts(true);

        driver = new ChromeDriver(chromeOptions);
        driver.get(URL);
    }

    public void searchProduct() {

        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"SearchBoxOld\"]/div/div/div[1]/div[2]/input"));
        searchBox.sendKeys(PRODUCT_NAME);

        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"SearchBoxOld\"]/div/div/div[2]"));
        searchButton.click();
    }

    public void moveToFirstProductDetail() {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"productresults\"]")));

        WebElement selectedProduct = driver.findElement(By.cssSelector("div#productresults li:nth-child(2) > div > a > div > h3 > div > p > span"));
        selectedProduct.click();
    }

    public void moveToReviewsTab() {
        WebElement reviewsTab = driver.findElement(By.id("reviewsTabTrigger"));
        reviewsTab.click();

        scrollDown();
    }

    public void likeFirstReview() {
        WebElement likeButton = driver.findElement(By.className("hermes-ReviewCard-module-tAGUS"));
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(likeButton)).click();

        Assert.assertTrue(isThanksElementPresent());
    }

    public void clickDropbox() {
        WebElement sortReviewsDropbox = driver.findElement(By.className("hermes-Sort-module-pGjws"));
        sortReviewsDropbox.click();

        List<WebElement> reviewsSortDropboxElements = driver.findElements(By.xpath("//*[contains(@class,'hermes-Sort-module-vYQvT hermes-Sort-module-2npZQ')]"));
        Assert.assertTrue(isValidReviewSortElements(reviewsSortDropboxElements));
    }

    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000)");
    }

    public boolean isValidReviewSortElements(List<WebElement> reviewsSortDropboxElements) {

        if (reviewsSortDropboxElements.size() != 4)
            return false;

        for (int i = 0; i < reviewsSortDropboxElements.size(); ++i) {
            if (!SORT_REVIEWS_TYPES.get(i).equals(reviewsSortDropboxElements.get(i).getText()))
                return false;
        }
        return true;

    }

    public boolean isReviewExist() {
        List<WebElement> noReviewsElement = driver.findElements(By.className("hermes-ProductRate-module-QusM-"));
        return noReviewsElement.isEmpty();
    }

    public boolean isThanksElementPresent() {
        List<WebElement> thanksElement = driver.findElements(By.className("hermes-ReviewCard-module-1ZiTv"));
        return !thanksElement.isEmpty() && thanksElement.get(0).getText().equals(THANKS_TEXT);
    }
}
