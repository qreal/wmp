package com.qreal.wmp.uitesting.headerpanel;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class FileItem {

    public static final By selector = By.id("file-menu");
    
    private FolderArea folderArea;

    public FileItem(WebDriver driver) {
        folderArea = new FolderAreaImpl(driver);
    }

    public void newDiagram() {
        $(selector).find(withText("New")).click();
        $(SaveDiagramConfirm.selector).shouldBe(Condition.visible);
        SaveDiagramConfirm.getSaveDiagramConfirm().notSave();
    }

    public FolderArea getSaveItem() {
        $(selector).find(withText("SaveAs")).click();
        $(FolderAreaImpl.selector).shouldBe(Condition.visible);
        return folderArea;
    }
    
    public FolderArea getOpenItem() {
        $(selector).find(withText("Open")).click();
        $(FolderAreaImpl.selector).shouldBe(Condition.visible);
        return folderArea;
    }
}
