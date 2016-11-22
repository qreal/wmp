package com.qreal.wmp.uitesting.auth.testspace;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.auth.Opener;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.Pallete;
import com.qreal.wmp.uitesting.dia.PropertyEditor;
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
public class DiagramConstructingTest {

    @Autowired
    private Opener opener;

    @Autowired
    private Pallete pallete;

    @Autowired
    private Scene scene;

    @Autowired
    private PropertyEditor propertyEditor;

    private WebDriver driver;

    /** Setup ChromeDriverManager. */
    @BeforeClass
    public static void init() {
        ChromeDriverManager.getInstance().setup();
    }

    @Before
    public void runDriver() {
        driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        opener.open("editor");
        scene.updateWebdriver(driver);
    }

    @Test
    public void diagramThreeNodesTwoLinks() {
        final SelenideElement node1 = scene.dragAndDrop(pallete.getElement("InitialNode"), 4, 4);
        final SelenideElement node2 = scene.dragAndDrop(pallete.getElement("InitialNode"), 10, 4);
        final SelenideElement node3 = scene.dragAndDrop(pallete.getElement("InitialNode"), 16, 4);
        scene.addLink(node1, node2);
        scene.addLink(node2, node3);
    }

    @After
    public void stopDriver() {
        scene.clean();
        driver.close();
    }

}
