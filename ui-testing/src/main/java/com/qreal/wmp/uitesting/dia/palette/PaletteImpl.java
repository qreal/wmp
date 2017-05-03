package com.qreal.wmp.uitesting.dia.palette;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

/** {@inheritDoc} */
public class PaletteImpl implements Palette {

    private static final Logger logger = LoggerFactory.getLogger(PaletteImpl.class);

    private final SelectorService selectorService;
    
    public PaletteImpl(SelectorService selectorService) {
        this.selectorService = selectorService;
    }
    
    public PaletteElement getElement(final String elementName) throws NoSuchElementException {
        final SelenideElement element =
                $(By.id(selectorService.get(SelectorService.Attribute.ID))).find(withText(elementName));
        logger.info("Get element {} from Palette", element);
        return new PaletteElement(element);
    }

    public static Palette getPalette(SelectorService selectorService) {
        return new PaletteImpl(selectorService);
    }
}
