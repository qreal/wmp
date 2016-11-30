package com.qreal.wmp.uitesting.innertests;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.Opener;
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
public class DiaTest {

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

    /** Setup browser. */
    @Before
    public void runDriver() {
        driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        opener.open("editor");
        scene.updateWebdriver(driver);
    }

    /** Drag element from pallete and drop on the scene. */
    @Test
    public void dragAndDrop() {
        final SelenideElement initialNode = scene.dragAndDrop(pallete.getElement("Initial Node"));
        assert scene.isExist(initialNode);
    }

    /** Remove element from scene. */
    @Test
    public void remove() {
        final SelenideElement sceneElement = scene.dragAndDrop(pallete.getElement("Initial Node"));
        assert scene.isExist(sceneElement);
        scene.remove(sceneElement);
        assert !scene.isExist(sceneElement);
    }

    /** Add two elements and link them. */
    @Test
    public void addLink() {
        final SelenideElement initNode = scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4);
        final SelenideElement finalNode = scene.dragAndDrop(pallete.getElement("Final Node"), 4, 70);
        final SelenideElement motor = scene.dragAndDrop(pallete.getElement("Motors Forward"), 4, 7);
        SelenideElement link = scene.addLink(initNode, motor);
        SelenideElement link2 = scene.addLink(motor, finalNode);
        scene.moveToCell(motor, 72, 64);
        assert scene.isExist(link);
        assert scene.isExist(link2);
    }

    /** Set property 'Ports' of motor forward item to '123' and checks that all is correct. */
    @Test
    public void propertyEditor() {
        final SelenideElement motor = scene.dragAndDrop(pallete.getElement("Motors Forward"));
        propertyEditor.setProperty(motor, "Ports", "123");
        assert propertyEditor.getProperty(motor, "Ports").equals("123");
    }

    /** Move element to cell. */
    @Test
    public void moveElement() {
        final SelenideElement motor = scene.dragAndDrop(pallete.getElement("Motors Forward"));
        scene.moveToCell(motor, 40, 40);
        assert scene.getCell(motor).getWidth() == 40 && scene.getCell(motor).getHeight() == 40;
        scene.moveToCell(motor, 72, 64);
        assert scene.getCell(motor).getWidth() == 72 && scene.getCell(motor).getHeight() == 64;
        scene.moveToCell(motor, 0, 0);
        assert scene.getCell(motor).getWidth() == 0 && scene.getCell(motor).getHeight() == 0;
    }

    /** Close the browser. */
    @After
    public void stopDriver() {
        scene.clean();
        driver.quit();
    }
}
