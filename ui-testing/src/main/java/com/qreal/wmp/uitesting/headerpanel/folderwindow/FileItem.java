package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.codeborne.selenide.Condition;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selenide.$;

/** Describes file item on the header menu. */
public class FileItem {

    private final FolderArea folderArea;
    
    private final WebDriver driver;
    
    private final SelectorService selectorService;

    public FileItem(WebDriver driver, SelectorService selectorService) {
        this.driver = driver;
        folderArea = new FolderAreaImpl(driver, selectorService.create("folderArea"));
        this.selectorService = selectorService;
    }

    /** Corresponds 'New' button. */
    public void newDiagram() {
        $(By.id(selectorService.get("fileItem.newDiagramItem", Attribute.ID))).click();
        $(By.id(selectorService.get("saveDiagramConfirmWindow", Attribute.ID)))
                .waitUntil(Condition.visible, 10000);
        SaveDiagramConfirm.getSaveDiagramConfirm(driver, selectorService.create("saveDiagramConfirmWindow")).notSave();
    }
    
    /** Returns folder window by clicking 'SaveAs'. */
    public FolderArea getSaveItem() {
        $(By.id(selectorService.get("fileItem.saveAsItem", Attribute.ID))).click();
        $(By.id(selectorService.get("folderArea", Attribute.ID))).waitUntil(Condition.visible, 10000);
        return folderArea;
    }
    
    /** Returns folder window by clicking 'Open'. */
    public FolderArea getOpenItem() {
        $(By.id(selectorService.get("fileItem.OpenItem", Attribute.ID))).click();
        $(By.id(selectorService.get("folderArea", Attribute.ID))).waitUntil(Condition.visible, 10000);
        return folderArea;
    }
    
    /** Corresponds 'Save' button. */
    public void saveDiagram() {
        $(By.id(selectorService.get("fileItem.SaveItem", Attribute.ID))).click();
    }
}
