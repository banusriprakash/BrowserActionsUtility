package Common;

import org.testng.annotations.*;

public class BaseTest {


    @Parameters("browser")
    @BeforeClass
    public void setUp(@Optional("chrome") String browserFromXml) {
        Driver.initDriver(browserFromXml);
    }

    @AfterClass
    public void tearDown() {
        Driver.quitDriver();
    }
}