package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

/** Describes file item on the header menu. */
public class FileItem {

    public static final By selector = By.id("file-menu");
    
    private final FolderArea folderArea;
    
    private final WebDriver driver;

    public FileItem(WebDriver driver) {
        this.driver = driver;
        folderArea = new FolderAreaImpl(driver);
    }

    /** Corresponds 'New' button. */
    public void newDiagram() {
        $(selector).find(withText("New")).click();
        $(SaveDiagramConfirm.selector).waitUntil(Condition.visible, 10000);
        SaveDiagramConfirm.getSaveDiagramConfirm(driver).notSave();
    }
    
    /** Returns folder window by clicking 'SaveAs'. */
    public FolderArea getSaveItem() {
        $(selector).find(withText("SaveAs")).click();
        $(FolderAreaImpl.selector).waitUntil(Condition.visible, 10000);
        return folderArea;
    }
    
    /** Returns folder window by clicking 'Open'. */
    public FolderArea getOpenItem() {
        $(selector).find(withText("Open")).click();
        $(FolderAreaImpl.selector).waitUntil(Condition.visible, 10000);
        return folderArea;
    }
    
    /** Corresponds 'Save' button. */
    public void saveDiagram() {
        $(selector).find(withText("Save")).click();
    }
}
