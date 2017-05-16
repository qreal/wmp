package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

/** Corresponds window, which appears in case we create new diagram. */
public class SaveDiagramConfirm {

    private final WebDriver driver;
    
    public static final By selector = By.id("confirm-save-diagram");
    
    public SaveDiagramConfirm(WebDriver driver) {
        this.driver = driver;
    }
    
    /** 'No' button. */
    public void notSave() {
        $(selector).find(withText("No")).click();
        $(selector).shouldBe(Condition.disappear);
    }
    
    /** 'Yes' button. */
    public FolderArea save() {
        $(selector).find(withText("Yes")).click();
        $(selector).shouldBe(Condition.disappear);
        return new FolderAreaImpl(driver);
    }
    
    public static SaveDiagramConfirm getSaveDiagramConfirm(WebDriver driver) {
        return new SaveDiagramConfirm(driver);
    }
}
