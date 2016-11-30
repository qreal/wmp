package com.qreal.wmp.uitesting.headerpanel;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class SaveDiagramConfirm {

    private final static By selector = By.id("confirm-save-diagram");

    public void notSave() {
        $(selector).find(withText("No")).click();
    }
}
