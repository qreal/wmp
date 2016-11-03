package com.qreal.wmp.uitesting.auth;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.Pallete;
import com.qreal.wmp.uitesting.dia.Scene;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
public class DiaTest {

    @Autowired
    private Opener opener;

    @Autowired
    private Pallete pallete;

    @Autowired
    private Scene scene;

    private WebDriver driver;

    /** Setup ChromeDriverManager. */
    @BeforeClass
    public static void init() {
        ChromeDriverManager.getInstance().setup();
    }

    /** Setup browser. */
    @Before
    public void runDriver() {
        driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        opener.open("editor");
    }

    @Test
    public void dragAndDrop() {
        SelenideElement palleteElement = pallete.getElement("InitialNode");
        SelenideElement sceneElement = scene.dragAndDrop(palleteElement);
        assert (scene.exist(sceneElement));
    }

    /** Close the browser. */
    @After
    public void stopDriver() {
        driver.close();
    }
}
