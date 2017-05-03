package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.codeborne.selenide.Condition;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selectors.withText;
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
        $(By.id(selectorService.get("cancel", Attribute.ID))).click();
        $(By.id(selectorService.get(Attribute.ID))).shouldBe(Condition.disappear);
    }
    
    /** 'Yes' button. */
    public FolderArea save() {
        $(By.id(selectorService.get("confirm", Attribute.ID))).click();
        $(By.id(selectorService.get(Attribute.ID))).shouldBe(Condition.disappear);
        return new FolderAreaImpl(driver);
    }
    
    public static SaveDiagramConfirm getSaveDiagramConfirm(WebDriver driver, SelectorService selectorService) {
        return new SaveDiagramConfirm(driver, selectorService);
    }
}
