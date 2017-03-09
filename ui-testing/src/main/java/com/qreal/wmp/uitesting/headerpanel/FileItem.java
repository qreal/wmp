package com.qreal.wmp.uitesting.headerpanel;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class FileItem {

    public static final By selector = By.id("file-menu");

    private SaveDiagramConfirm saveDiagramConfirm;

    private FolderArea folderArea;

    public FileItem(WebDriver driver) {
        saveDiagramConfirm = new SaveDiagramConfirm();
        folderArea = new FolderAreaImpl(driver);
    }

    public void newDiagram() {
        $(selector).find(withText("New")).click();
        $(SaveDiagramConfirm.selector).shouldBe(Condition.visible);
        saveDiagramConfirm.notSave();
    }

    public FolderArea getSaveItem() {
        $(selector).find(withText("Save")).click();
        $(FolderAreaImpl.selector).shouldBe(Condition.visible);
        return folderArea;
    }
    
    public FolderArea getOpenItem() {
        $(selector).find(withText("Open")).click();
        $(FolderAreaImpl.selector).shouldBe(Condition.visible);
        return folderArea;
    }
}
