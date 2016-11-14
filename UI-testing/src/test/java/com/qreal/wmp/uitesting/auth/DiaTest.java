package com.qreal.wmp.uitesting.auth;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.Pallete;
import com.qreal.wmp.uitesting.dia.PropertyEditor;
import com.qreal.wmp.uitesting.dia.Scene;
import com.qreal.wmp.uitesting.dia.SceneWindow;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
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

    @Autowired
    private SceneWindow scw;

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
        final SelenideElement palleteElement = pallete.getElement("InitialNode");
        final SelenideElement sceneElement = scene.dragAndDrop(palleteElement);
        assert scene.exist(sceneElement);
    }

    /** Remove element from scene. */
    @Test
    public void remove() {
        final SelenideElement sceneElement = scene.dragAndDrop(pallete.getElement("InitialNode"));
        assert scene.exist(sceneElement);
        scene.remove(sceneElement);
        assert !scene.exist(sceneElement);
    }

    /** Move element on the scene by offsets. */
    @Test
    public void move() {
        final SelenideElement sceneElement = scene.dragAndDrop(pallete.getElement("InitialNode"));
        final Dimension oldPosition = scene.getPosition(sceneElement);
        scene.moveElement(sceneElement, 500, 100);
        final Dimension newPosition = scene.getPosition(sceneElement);
        assert  (oldPosition.getWidth() + 500 == newPosition.getWidth())
                && (oldPosition.getHeight() + 100 == newPosition.getHeight());
    }

    /** Add two elements and link them. */
    @Test
    public void addLink() {
        final SelenideElement initNode = scene.dragAndDrop(pallete.getElement("InitialNode"));
        scene.moveElement(initNode, 100, 100);
        final SelenideElement finalNode = scene.dragAndDrop(pallete.getElement("FinalNode"));
        SelenideElement link = scene.addLink(initNode, finalNode);
        assert scene.exist(link);
    }

    /** Set property 'Ports' of motor forward item to '123' and checks that all is correct. */
    @Test
    public void propertyEditor() {
        final SelenideElement motor = scene.dragAndDrop(pallete.getElement("TrikV6EnginesForward"));
        motor.click();
        propertyEditor.setProperty("Ports", "123");
        assert propertyEditor.getProperty("Ports").equals("123");
    }

    @Test
    public void sceneWindow() {
        final SelenideElement motor = scene.dragAndDrop(pallete.getElement("TrikV6EnginesForward"));
        scw.move(motor, new Dimension(1000, 1000), driver);
        assert scene.getPosition(motor).getWidth() == 1000 && scene.getPosition(motor).getHeight() == 1000;
        scw.move(motor, new Dimension(1800, 1600), driver);
        assert scene.getPosition(motor).getWidth() == 1800 && scene.getPosition(motor).getHeight() == 1600;
        scw.move(motor, new Dimension(1000, 1000), driver);
        assert scene.getPosition(motor).getWidth() == 1000 && scene.getPosition(motor).getHeight() == 1000;
    }

    /** Close the browser. */
    @After
    public void stopDriver() {
        scene.clean();
        driver.close();
    }
}
