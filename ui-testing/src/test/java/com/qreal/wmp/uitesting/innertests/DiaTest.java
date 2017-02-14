package com.qreal.wmp.uitesting.innertests;

import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.Opener;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.model.Block;
import com.qreal.wmp.uitesting.dia.model.Link;
import com.qreal.wmp.uitesting.dia.services.Pallete;
import com.qreal.wmp.uitesting.dia.services.PropertyEditor;
import com.qreal.wmp.uitesting.dia.services.Scene;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
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
        final Block initialNode = scene.dragAndDrop(pallete.getElement("Initial Node"));
        assert scene.exist(initialNode);
    }

    /** Remove element from scene. */
    @Test
    public void remove() {
        final Block sceneElement = scene.dragAndDrop(pallete.getElement("Initial Node"));
        assert scene.exist(sceneElement);
        scene.remove(sceneElement);
        assert !scene.exist(sceneElement);
    }

    /** Add two elements and link them. */
    @Test
    public void addLink() {
        final Block initNode = scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4);
        final Block finalNode = scene.dragAndDrop(pallete.getElement("Final Node"), 4, 70);
        final Block motor = scene.dragAndDrop(pallete.getElement("Motors Forward"), 4, 7);
        Link link = scene.addLink(initNode, motor);
        Link link2 = scene.addLink(motor, finalNode);
        scene.moveToCell(motor, 72, 64);
        assert scene.exist(link);
        assert scene.exist(link2);
    }

    /** Set property 'Ports' of motor forward item to '123' and checks that all is correct. */
    @Test
    public void propertyEditor() {
        final Block motor = scene.dragAndDrop(pallete.getElement("Motors Forward"));
        propertyEditor.setProperty(motor.getInnerSeleniumElement(), "Ports", "123");
        assert propertyEditor.getProperty(motor.getInnerSeleniumElement(), "Ports").equals("123");
    }

    /** Move element to cell. */
    @Test
    public void moveElement() {
        final Block motor = scene.dragAndDrop(pallete.getElement("Motors Forward"));
        try {
            scene.moveToCell(motor, 40, 40);
            assert motor.getCoordinateOnScene().getXCell() == 40 && motor.getCoordinateOnScene().getYCell() == 40;
            scene.moveToCell(motor, 72, 64);
            assert motor.getCoordinateOnScene().getXCell() == 72 && motor.getCoordinateOnScene().getYCell() == 64;
            scene.moveToCell(motor, 0, 0);
            assert motor.getCoordinateOnScene().getXCell() == 0 && motor.getCoordinateOnScene().getYCell() == 0;
        } catch (ElementNotOnTheSceneException e) {
            e.printStackTrace();
        }
    }

    /** Close the browser. */
    @After
    public void stopDriver() {
        scene.clean();
        driver.quit();
    }
}
