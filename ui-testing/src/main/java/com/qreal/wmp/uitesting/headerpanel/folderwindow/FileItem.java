package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.qreal.wmp.uitesting.services.SelectorService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/** Describes file item on the header menu. */
public class FileItem {
    private static final Logger logger = LoggerFactory.getLogger(FileItem.class);
    
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
        new Actions(driver).click($(By.id(selectorService.getId("fileItem.newDiagramItem")))).perform();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        SaveDiagramConfirm.getSaveDiagramConfirm(driver, selectorService.create("saveDiagramConfirmWindow")).notSave();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    
    /** Returns folder window by clicking 'SaveAs'. */
    public FolderArea getSaveItem() {
        new Actions(driver).click($(By.id(selectorService.getId("fileItem.saveAsItem")))).perform();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        return folderArea;
    }
    
    /** Returns folder window by clicking 'Open'. */
    public FolderArea getOpenItem() {
        new Actions(driver).click($(By.id(selectorService.getId("fileItem.openItem")))).perform();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        return folderArea;
    }
    
    /** Corresponds 'Save' button. */
    public void saveDiagram() {
        new Actions(driver).click($(By.id(selectorService.getId("fileItem.saveItem")))).perform();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
