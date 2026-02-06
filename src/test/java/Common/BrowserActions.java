package Common;

import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class BrowserActions {
    public static final Logger log = LoggerFactory.getLogger(BrowserActions.class);

    private static WebDriver getDriver() {
        return Driver.getDriver();
    }

    public static WebDriverWait explicitWait(int seconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
    }

    public static WebDriverWait explicitWait() {
        return explicitWait(30);
    }

    public static JavascriptExecutor getExecutor() {
        return (JavascriptExecutor) getDriver();
    }


    public static void scrollAndClick(WebElement element) {
        waitUntilElementVisible(element);
        getExecutor().executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        getExecutor().executeScript("arguments[0].click();", element);
    }

    public static void scrollIntoView(WebElement element) {
        getExecutor().executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void waitUntilElementVisible(WebElement element) {
        explicitWait().until(ExpectedConditions.visibilityOf(element));
    }


    public static void waitUntilClickable(WebElement element) {
        explicitWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void get(String url) {
        getDriver().get(url);
    }

    public static String getTitle() {
        return getDriver().getTitle();
    }

    public static void navigateBack() {
        getDriver().navigate().back();
    }

    public static void navigateForward() {
        getDriver().navigate().forward();
    }

    public static void refreshPage() {
        getDriver().navigate().refresh();

    }

    public static void highlight(WebElement element) {
        getExecutor().executeScript("arguments[0].style.border='3px solid yellow'", element);
    }

    public static WebElement findElementByXpath(String xpath) {
        return getDriver().findElement(By.xpath(xpath));
    }

    public static List<WebElement> findElementsByXpath(String xpath) {
        return new ArrayList<>(getDriver().findElements(By.xpath(xpath)));
    }

    public static long findElementsSizeByXpath(String xpath) {
        return new ArrayList<>(getDriver().findElements(By.xpath(xpath))).size();
    }

    public static void acceptPopup() {
        try {
            Alert alert = explicitWait()
                    .until(ExpectedConditions.alertIsPresent());

            assert alert != null;
            alert.accept();
        } catch (TimeoutException e) {
            BrowserActions.captureScreenshot("Alert_Not_Present");
            throw new RuntimeException("Alert was not present", e);
        } catch (UnhandledAlertException e) {
            BrowserActions.captureScreenshot("Unhandled_Alert");
            throw e;
        }
    }

    public static void dismissPopup() {
        Alert alert = explicitWait().until(ExpectedConditions.alertIsPresent());
        if (alert != null) {
            alert.dismiss();
        }
    }

    public static String getPopupText() {
        Alert alert = explicitWait().until(ExpectedConditions.alertIsPresent());
        return alert != null ? alert.getText() : null;
    }

    public static void sendKeysToPopup(String text) {
        Alert alert = explicitWait().until(ExpectedConditions.alertIsPresent());
        if (alert != null) {
            alert.sendKeys(text);
            alert.accept();
        }
    }

    public static void switchToWindowByName(String name) {
        getDriver().switchTo().window(name);
    }

    public static void switchToWindowByIndex(int index) {
        List<String> windowHandles = new ArrayList<>(getDriver().getWindowHandles());

        if (index < windowHandles.size() && index >= 0) {
            getDriver().switchTo().window(windowHandles.get(index));
        } else {
            Driver.log.error("Index {} is out of bounds for open windows.", index);
        }
    }

    public static void openNewWindow() {
        getDriver().switchTo().newWindow(WindowType.WINDOW);
    }

    public static void openNewTab() {
        getDriver().switchTo().newWindow(WindowType.TAB);
    }

    public static void closeCurrentWindowAndReturn() {
        getDriver().close();
        switchToWindowByIndex(0);
    }


    public static void captureScreenshot(String fileName) {
        try {
            if (fileName.equalsIgnoreCase("")) fileName = "ScreenShot";

            // 1. Capture the screenshot as a temporary file
            File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

            // 2. Generate dynamic timestamp and project-relative path
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String projectPath = System.getProperty("user.dir");

            // 3. Define the directory and ensure it exists
            String folderPath = projectPath + "/src/test/java/ScreenShots/";
            Path directory = Paths.get(folderPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 4. Define the full destination path
            Path destination = directory.resolve(fileName + "_" + timestamp + ".png");

            // 5. Use Java NIO to move/copy the file
            // StandardCopyOption.REPLACE_EXISTING ensures it overwrites if a name conflict occurs
            Files.copy(srcFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot captured: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }

    public static void sendText(WebElement element, String text) {
        waitUntilElementVisible(element);
        highlight(element);
        element.clear();
        element.sendKeys(text);
    }

    public static void clickElement(WebElement element) {
        getExecutor().executeScript("arguments[0].click();", element);
    }

    public static void scrollByAmount(int x, int y) {
        getExecutor().executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public static void scrollAndClick(String xpath) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        waitUntilElementVisible(element);
        getExecutor().executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        getExecutor().executeScript("arguments[0].click();", element);
    }

    public static void clickElement(String xpath) {
        try{
            WebElement element = BrowserActions.findElementByXpath(xpath);
            getExecutor().executeScript("arguments[0].click();", element);
        }
        catch (Exception e){
            BrowserActions.captureScreenshot("Error_Occured_during_click");
        }

    }

    public static void sendText(String xpath, String text) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        waitUntilElementVisible(element);
        highlight(element);
        element.clear();
        element.sendKeys(text);
    }

    public static void highlight(String xpath) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        getExecutor().executeScript("arguments[0].style.border='3px solid yellow'", element);
    }

    public static void scrollIntoView(String xpath) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        getExecutor().executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void waitUntilClickable(String xpath) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        explicitWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitUntilNotVisible(String xpath) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        explicitWait().until(ExpectedConditions.invisibilityOf(element));
    }

    public static void waitUntilElementVisible(String xpath) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        explicitWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitUntilPageContain(String text) {
        explicitWait(40).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), '" + text + "')]")
        ));
    }

    public static void hardWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public static void selectByValue(String xpath, String value) {

            try {
                WebElement element = BrowserActions.findElementByXpath(xpath);
                explicitWait().until(ExpectedConditions.elementToBeClickable(element));
                Select select = new Select(element);
                select.selectByValue(value);
            } catch (Exception e) {
                BrowserActions.captureScreenshot("selection_error");
                Driver.log.error("Dropdown selection failed for xpath: {}", xpath, e);
                throw e;
            }
        }

        public static void selectByIndex(String xpath, int number) {
            try {
                WebElement element = BrowserActions.findElementByXpath(xpath);
                explicitWait().until(ExpectedConditions.elementToBeClickable(element));
                Select select = new Select(element);
                select.selectByIndex(number);
            } catch (Exception e) {
                BrowserActions.captureScreenshot("selection_error");
                Driver.log.error("Dropdown selection failed for xpath: {}", xpath, e);
                throw e;
            }

        }

        public static void selectValue(String value){
            String xpath = String.format("//li[@aria-label='%s']", value);
            WebElement element = BrowserActions.findElementByXpath(xpath);
            explicitWait().until(ExpectedConditions.elementToBeClickable(element));
            BrowserActions.clickElement(element);
        }

        public static void scrollandsendtext(String xpath,String text){
        WebElement element=BrowserActions.findElementByXpath(xpath);
        getExecutor().executeScript("arguments[0].scrollIntoView(true);",element);
        explicitWait().until(ExpectedConditions.visibilityOf(element));
        highlight(element);
        element.clear();
        element.sendKeys(text);
        }

        public static String getText(String xpath){
            WebElement element=BrowserActions.findElementByXpath(xpath);
            explicitWait().until(ExpectedConditions.visibilityOf(element));
            return element.getText();
        }

        public static void pageShouldContain(String text) {
            WebElement element = getDriver().findElement(
                    By.xpath("//*[contains(normalize-space(text()),'" + text + "')]")
            );
            explicitWait().until(ExpectedConditions.visibilityOf(element));
            getExecutor().executeScript("arguments[0].scrollIntoView(true);", element);

        }

        public static void pressPageDown(int number){
            Actions action=new Actions(getDriver());
            for (int i = 0; i < number; i++) {
                action.sendKeys(Keys.PAGE_DOWN).perform();
            }
        }

        public static void pressPageUp(int number){
            Actions actions=new Actions(getDriver());

            for (int i = 0; i < number; i++) {
                actions.sendKeys(Keys.PAGE_UP).perform();
            }
        }

        public static void pressLeftArrow(int number){
            Actions actions=new Actions(getDriver());
            for (int i = 0; i < number; i++) {
                actions.sendKeys(Keys.ARROW_LEFT).perform();
            }
        }

        public static void pressRightArrow(int number){
            Actions actions=new Actions(getDriver());
            for (int i = 0; i < number; i++) {
                actions.sendKeys(Keys.ARROW_RIGHT).perform();
            }
        }

        public static void pressEnterKey(){
            Actions actions=new Actions(getDriver());
            actions.sendKeys(Keys.ENTER).perform();
        }


    public static String replaceXpath(String xpath, String... values){
        return String.format(xpath, (Object[]) values);
    }

    public static WebElement findWebElementByTag(String tagName){
        WebElement element=null;
        try {
            element=getDriver().findElement(By.tagName(tagName));

        }
        catch (Exception e){
            BrowserActions.captureScreenshot("Error_finding_WebElement");
        }
        return element;
    }

    public static WebElement findWebElementByCss(String cssSelector){
        WebElement element=null;
        try {
            element=getDriver().findElement(By.cssSelector(cssSelector));
        }
        catch (Exception e){
            BrowserActions.captureScreenshot("Error_finding_WebElement");
        }
        return element;
    }

    public static WebElement findWebElementById(String id){
        WebElement element=null;
        try {
            element=getDriver().findElement(By.id(id));
        }
        catch (Exception e){
            BrowserActions.captureScreenshot("Error_finding_WebElement");
        }
        return element;
    }

    public static WebElement findShadowElement(String hostCss, String innerCss) {
        String script = String.format("return document.querySelector('%s').shadowRoot.querySelector('%s')", hostCss, innerCss);
        return (WebElement) getExecutor().executeScript(script);
    }

    public static String getElementAttributeValue(String xpath, String attributeName) {
        WebElement element = BrowserActions.findElementByXpath(xpath);
        String value = element.getAttribute(attributeName);
        return value;
    }

    public static boolean isElementDisabled(String xpath) {
        WebElement webElement = BrowserActions.findElementByXpath(xpath);
        boolean status = !webElement.isEnabled();

        log.info("Is element at {} disabled? : {}", xpath, status);

        return status;
    }

    public static boolean isElementEnabled(String xpath){
        WebElement element=BrowserActions.findElementByXpath(xpath);
        boolean status=element.isEnabled();

        log.info("Is element at {} enabled? : {}", xpath, status);

        return status;
    }

    public static void webBack(){
        getExecutor().executeScript("window.history.back();");
    }

    public static void webFront(){
        getExecutor().executeScript("window.history.forward()");
    }

    public static boolean isElementClickable(String xpath) {
        try {

            WebDriverWait wait = BrowserActions.explicitWait(10);

            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

            log.info("Element is clickable: " + xpath);
            return element != null;

        } catch (Exception e) {
            log.error("Element not clickable within timeout: " + xpath);
            return false;
        }
    }

    public static boolean isElementNotClickable(String xpath) {
        try {
            WebDriverWait wait = BrowserActions.explicitWait(10);


            boolean isUnclickable = wait.until(
                    ExpectedConditions.not(ExpectedConditions.elementToBeClickable(By.xpath(xpath)))
            );

            log.info("Confirmed: Element is NOT clickable: " + xpath);
            return isUnclickable;

        } catch (TimeoutException e) {

            log.error("Element remained clickable after timeout: " + xpath);
            return false;
        }
    }

}