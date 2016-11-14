package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;

@Service
public class SceneWindow {

    @Autowired
    private Scene scene;

    private Dimension size;

    private static final String selector = ".scene-wrapper";

    private WebDriver driver;

    /** For actions such as mouse move we need driver of current page. */
    public void updateWebdriver(final WebDriver webDriver) {
        driver = webDriver;
    }

    public void move(SelenideElement element, Dimension dist, WebDriver driver) {
        Dimension src = scene.getPosition(element);
        SelenideElement sceneWrapper =  $(By.cssSelector(".scene-wrapper"));
        int step = sceneWrapper.getSize().getWidth() / 9;
        size = sceneWrapper.getSize();
        focus(src, driver);

        if (src.getWidth() < dist.getWidth())
            stepsWithActions(src.getWidth(), x -> x < dist.getWidth() + 0.75 * src.getWidth(), step,
                () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_RIGHT)
                        .clickAndHold(element).moveByOffset(step, 0));
        else
            stepsWithActions(src.getWidth(), x -> x > dist.getWidth() + 0.27 * src.getWidth(), -step,
                () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_LEFT)
                        .clickAndHold(element).moveByOffset(-step, 0));

        if (src.getHeight() < dist.getHeight())
            stepsWithActions(src.getHeight(), x -> x < dist.getHeight() + 0.75 * src.getHeight(), step,
                    () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_DOWN)
                            .clickAndHold(element).moveByOffset(step, 0));
        else
            stepsWithActions(src.getHeight(), x -> x > dist.getHeight() + 0.75 * src.getHeight(), -step,
                    () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_UP)
                            .clickAndHold(element).moveByOffset(-step, 0));


        Dimension currentPosition = scene.getPosition(element);
        new Actions(driver).release().clickAndHold(element).moveByOffset(dist.getWidth() - currentPosition.getWidth(),
                dist.getHeight() - currentPosition.getHeight()).release().build().perform();

        System.out.println("Position: " + scene.getPosition(element));
    }

    public void focus(Dimension position, WebDriver driver) {
        SelenideElement sceneWrapper =  $(By.cssSelector(selector));
        size = sceneWrapper.getSize();
        int step = size.getWidth() / 9;

        stepsWithActions(0, constructPredicate(0, 2000, 0), step,
                () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_LEFT));
        stepsWithActions(0, constructPredicate(0, 2000, 0), step,
                () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_UP));

        stepsWithActions(size.getWidth(), x -> x < position.getWidth() + 0.75 * size.getWidth(), step,
                () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_RIGHT));
        stepsWithActions(size.getHeight(), x -> x < position.getHeight() + 0.75 * size.getHeight(), step,
                () -> new Actions(driver).click(sceneWrapper).sendKeys(Keys.ARROW_DOWN));
        new Actions(driver).build().perform();
    }

    public IntPredicate constructPredicate(final int srcValue, final int distValue, final int half) {
        return srcValue < distValue ? x -> x < distValue + 1.5 * half: x -> x > srcValue + 1.5 * half;
    }

    public void stepsWithActions(int begin, IntPredicate border, int step, Supplier<Actions>... actionses) {
        IntStream.iterate(begin, i -> i + step).peek(newValue -> {
            Arrays.stream(actionses).forEach(actions -> actions.get().perform());
        }).allMatch(border);
    }
}
