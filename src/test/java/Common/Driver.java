package Common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Driver {
    public static final Logger log = LoggerFactory.getLogger(Driver.class);
    public static boolean incognito = true;

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static void initDriver(String browser) {
        // 1. Check if a driver already exists for this thread to avoid multiple browsers
        if (driverThreadLocal.get() == null) {
            WebDriver driver;
            String browserName = (browser == null || browser.isEmpty()) ? "chrome" : browser.toLowerCase();

            switch (browserName) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (incognito) chromeOptions.addArguments("--incognito");
                    chromeOptions.addArguments("--disable-notifications");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (incognito) edgeOptions.addArguments("--inprivate");
                    driver = new EdgeDriver(edgeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions=new FirefoxOptions();
                    if (incognito) firefoxOptions.addArguments("-private");
                    driver=new FirefoxDriver(firefoxOptions);
                    break;

                default:
                    driver = new ChromeDriver();
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Recommended: 10s (Use Explicit for the rest)
            driver.manage().window().maximize();
            driverThreadLocal.set(driver);
            log.info("Initialized {} driver for Thread ID: {}", browserName, Thread.currentThread().getId());
        }
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
            log.info("Driver quit and ThreadLocal removed for Thread ID: {}", Thread.currentThread().getId());
        }
    }
}