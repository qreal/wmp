package com.qreal.wmp.uitesting.auth;

import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthDashboardTest {

    private static String dashboardUrl;

    private static WebDriver driver;

    /**
     * Setup ChromeDriverManager and load correct urls from .properties file.
     */
    @BeforeClass
    public static void setUpClass() {
        ChromeDriverManager.getInstance().setup();
        final String resourceName = "services.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dashboardUrl = props.getProperty("accessDashboardUri");
    }

    /**
     * Try to open dashboard page.
     * Should be redirected to auth page.
     */
    @Before
    public void openAuthPage() {
        driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        open(dashboardUrl);
        $(byText("Sign in to continue to Auth")).shouldBe(exist);
        $(byText("Dashboard")).shouldNotBe(exist);
    }

    /**
     * Try to login with correct username and password.
     * Should access and redirect to dashboard
     */
    @Test
    public void userCanLoginByUsername() {
        $(By.name("username")).setValue("123");
        $(By.name("password")).setValue("123");
        $("[type=\"submit\"]").click();
        $(byText("Dashboard")).waitUntil(appear, 50000);
    }

    /**
     * Try to login with random username and password.
     * An error must be shown
     */
    @Test
    public void  userWrongAuth() {
        $(byText("Password or login wrong")).shouldNotBe(exist);
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        String wrongLogin = RandomStringUtils.random(20, alphabet);
        String wrongPassword = RandomStringUtils.random(20, alphabet);
        $(By.name("username")).setValue(wrongLogin);
        $(By.name("password")).setValue(wrongPassword);
        $("[type=\"submit\"]").click();
        $(byText("Password or login wrong")).shouldBe(exist);
    }

    @After
    public void logout() {
        driver.close();
    }

}
