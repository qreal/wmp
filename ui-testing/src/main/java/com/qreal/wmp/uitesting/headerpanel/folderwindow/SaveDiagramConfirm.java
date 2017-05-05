package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.google.common.base.Predicate;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Selenide.$;

/** Corresponds window, which appears in case we create new diagram. */
public class SaveDiagramConfirm {

    private final WebDriver driver;
    
    private final SelectorService selectorService;
    
    public SaveDiagramConfirm(WebDriver driver, SelectorService selectorService) {
        this.driver = driver;
        this.selectorService = selectorService;
    }
    
    /** 'No' button. */
    public void notSave() {
        (new WebDriverWait(driver, 10))
                .until((Predicate<WebDriver>) webDriver ->
                        $(By.id(selectorService.getId())).attr("class").contains("in"));
    
        new Actions(driver).click($(By.id(selectorService.getId("cancel")))).perform();
    }
    
    /** 'Yes' button. */
    public FolderArea save() {
        (new WebDriverWait(driver, 10))
                .until((Predicate<WebDriver>) webDriver ->
                        $(By.id(selectorService.getId())).attr("class").contains("in"));
    
        new Actions(driver).click($(By.id(selectorService.getId("confirm")))).perform();
    
        return new FolderAreaImpl(driver, selectorService);
    }
    
    public static SaveDiagramConfirm getSaveDiagramConfirm(WebDriver driver, SelectorService selectorService) {
        return new SaveDiagramConfirm(driver, selectorService);
    }
}
