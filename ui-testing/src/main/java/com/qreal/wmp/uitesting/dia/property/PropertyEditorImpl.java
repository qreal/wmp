package com.qreal.wmp.uitesting.dia.property;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PropertyEditorImpl implements PropertyEditor {

    private static final String SELECTOR = "#property_table";

    private static final Logger logger = LoggerFactory.getLogger(PropertyEditorImpl.class);

    private final Scene scene;
    
    public PropertyEditorImpl(Scene scene) {
        this.scene = scene;
    }
    
    /** {@inheritDoc} */
    public void setProperty(final SelenideElement element, final String propertyName, final String propertyValue)
            throws NoSuchElementException
    {
        element.click();
        SelenideElement property = getInputOfElement(propertyName);
        if (property.attr("class").equals("input-group")) {
            property.find(By.xpath(".//*")).setValue(propertyValue);
        } else {
            property.selectOptionByValue(propertyValue);
        }
        logger.info("Set property {} to {}", propertyName, propertyValue);
    }
    
    /** {@inheritDoc} */
    public String getProperty(final SelenideElement element, final String propertyName) throws NoSuchElementException {
        $(By.cssSelector(scene.getSelector())).click();
        element.click();
        SelenideElement property = getInputOfElement(propertyName);
        logger.info("Get value of preperty {}", propertyName);
        if (property.attr("class").equals("input-group")) {
            return property.find(By.xpath(".//*")).getValue();
        } else {
            return property.getSelectedOption().getValue();
        }
    }

    /** To set/get property we need to take web element which describes needed field. */
    private SelenideElement getInputOfElement(final String propertyName) {
        final List<SelenideElement> allChilds = $$(By.cssSelector(SELECTOR + " tbody > * > *"));
        final OptionalInt indexOfNeeded = IntStream.range(0, allChilds.size())
                .filter(index -> allChilds.get(index).getText().contains(propertyName))
                .findFirst();
        if (!indexOfNeeded.isPresent()) {
            throw new NoSuchElementException("There is no property with name " + propertyName);
        }
        return $(By.cssSelector(SELECTOR + " tbody > *:nth-of-type("
                + (indexOfNeeded.getAsInt() / 2 + 1)
                + ") > *:nth-of-type(2) > *" ));
    }
}
