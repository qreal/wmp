package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.springframework.stereotype.Service;

@Service
public class Scene {

    private final String selector = ".scene-wrapper";

    public void dragAndDrop(SelenideElement element) {
        element.dragAndDropTo(selector);
    };
}
