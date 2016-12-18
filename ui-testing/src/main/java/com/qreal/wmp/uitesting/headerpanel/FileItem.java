package com.qreal.wmp.uitesting.headerpanel;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class FileItem {

    public static final By selector = By.id("file-menu");

    private SaveDiagramConfirm saveDiagramConfirm;

    private SaveItem saveItem;

    public FileItem() {
        saveDiagramConfirm = new SaveDiagramConfirm();
        saveItem = new SaveItem();
    }

    public void newDiagram() {
        $(selector).find(withText("New")).click();
        saveDiagramConfirm.notSave();
    }

    public SaveItem getSaveItem() {
        $(selector).find(withText("Save")).click();
        return saveItem;
    }
}
