package com.qreal.wmp.uitesting.innertests;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.Opener;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.Pallete;
import com.qreal.wmp.uitesting.dia.Scene;
import com.qreal.wmp.uitesting.headerpanel.HeaderPanel;
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

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
public class HeaderPanelTest {

    @Autowired
    private Opener opener;

    @Autowired
    private Pallete pallete;

    @Autowired
    private Scene scene;

    @Autowired
    private HeaderPanel headerPanel;

    private WebDriver driver;

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

    @Test
    public void newDiagramTest() {

        List<SelenideElement> elements = new ArrayList<>();
        List<SelenideElement> links = new ArrayList<>();

        elements.add(scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4));
        elements.add(scene.dragAndDrop(pallete.getElement("Motors Forward"), 10, 4));
        links.add(scene.addLink(elements.get(0), elements.get(1)));
        elements.add(scene.dragAndDrop(pallete.getElement("Painter Color"), 16, 4));
        links.add(scene.addLink(elements.get(1), elements.get(2)));

        headerPanel.getFileItem().newDiagram();
    }

    @Test
    public void clickDashboardTest() {
        headerPanel.clickDashboard();
        $(byText("Dashboard")).waitUntil(appear, 5000);
    }

    @Test
    public void createFolderTest() {
        headerPanel.getFileItem().getSaveItem().createFolder("myFolder");
    }

    /** Close the browser. */
    @After
    public void stopDriver() {
        scene.clean();
        driver.quit();
    }
}
