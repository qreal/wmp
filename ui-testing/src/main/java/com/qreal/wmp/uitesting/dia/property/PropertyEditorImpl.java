package com.qreal.wmp.uitesting.dia.property;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/** {@inheritDoc} */
public class PropertyEditorImpl implements PropertyEditor {

    private static final Logger logger = LoggerFactory.getLogger(PropertyEditorImpl.class);
    
    private final SelectorService selectorService;
    
    public PropertyEditorImpl(SelectorService selectorService) {
        this.selectorService = selectorService;
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
        $(By.id(selectorService.getId())).click();
        element.click();
        SelenideElement property = getInputOfElement(propertyName);
        logger.info("Get value of preperty {}", propertyName);
        if (property.attr("class").equals("input-group")) {
            return property.find(By.xpath(".//*")).getValue();
        } else {
            return property.getSelectedOption().getValue();
        }
    }

    @Contract("_ -> !null")
    public static PropertyEditor getPropertyEditor(SelectorService selectorService) {
        return new PropertyEditorImpl(selectorService);
    }
    
    /** To set/get property we need to take web element which describes needed field. */
    private SelenideElement getInputOfElement(final String propertyName) {
          return $$(By.cssSelector(selectorService.getSelector("property")))
                .stream()
                .filter(property ->
                        property.find(selectorService.getSelector("property.propertyName")).exists()
                ).map(property -> property.find(selectorService.getSelector("property.propertyValue")))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no property with name " + propertyName));
    }
}
