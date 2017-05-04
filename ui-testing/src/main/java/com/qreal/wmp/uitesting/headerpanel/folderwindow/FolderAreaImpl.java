package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Predicate;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.function.Function;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/** {@inheritDoc} */
public class FolderAreaImpl implements FolderArea {
    
    private final WebDriver driver;
    
    private final SelectorService selectorService;
    
    public FolderAreaImpl(WebDriver driver, SelectorService selectorService) {
        this.driver = driver;
        this.selectorService = selectorService;
    }
    
    @Override
    public FolderArea createFolder(String folderName) {
        $(By.id(selectorService.getId("createItem"))).click();
        $(By.id(selectorService.getId("folderMenu.folderNameInput"))).setValue(folderName);
        $(By.id(selectorService.getId("folderMenu.confirmItem"))).click();
        if ($(By.id(selectorService.getId("warningMessage"))).isDisplayed()) {
            throw new IllegalArgumentException("The folder with this name already exists");
        }
        return this;
    }
    
    @Override
    public boolean isFolderExist(String name) {
        return $$(By.cssSelector(selectorService.getSelector("folders")))
                .stream().anyMatch(elem -> elem.has(text(name)));
    }
    
    @Override
    public FolderArea moveForward(String name) {
        if (!isFolderExist(name)) {
            throw new IllegalArgumentException("Folder with name " + name + " does not exist");
        }
        String oldPath = getCurrentPath();
        oldPath = "".equals(oldPath) ? name : oldPath + "/" + name;
        $(By.cssSelector(selectorService.getSelector("folders"))).find(byText(name)).click();
        waitUntilEquals(oldPath, FolderArea::getCurrentPath);
        return this;
    }
    
    @Override
    public FolderArea moveBack() {
        String oldPath = getCurrentPath();
        $(By.id(selectorService.getId("levelUpItem"))).click();
        String[] steps = oldPath.split("/");
        String diff = String.join("/", Arrays.copyOf(steps, steps.length - 1));
        waitUntilEquals(diff, FolderArea::getCurrentPath);
        return this;
    }
    
    @Override
    public String getCurrentPath() {
        String result = $(By.id(selectorService.getId("folderPath")))
                .find(By.tagName("p"))
                .getText();
        
        return result.contains("/") ? result.substring(0, result.length() - 1) : result;
    }
    
    @Override
    public FolderArea move(String path) {
        
        while (!("").equals(getCurrentPath())) {
            moveBack();
        }
        Arrays.stream(path.split("/")).filter(subfolder -> !subfolder.isEmpty()).forEach(subfolder -> {
            if (!isFolderExist(subfolder)) {
                createFolder(subfolder);
            }
            moveForward(subfolder);
        });
        return this;
    }
    
    @Override
    public FolderArea deleteFolder(String name) {
        if (!isFolderExist(name)) {
            throw new IllegalArgumentException("Folder is not exist");
        }
        $(By.cssSelector(selectorService.getSelector("folders")))
                .find(byText(name)).contextClick();
        $(By.id(selectorService.getId("contextMenu"))).shouldBe(Condition.visible);
        $(By.id(selectorService.getId("contextMenu.deleteItem"))).click();
        return this;
    }
    
    @Override
    public void close() {
        SelenideElement closeButton = $(By.id(selectorService.getId("closeItem")));
        if ($(By.id(selectorService.getId())).isDisplayed() && closeButton.isDisplayed()) {
            closeButton.click();
            $(By.id(selectorService.getId())).shouldBe(Condition.disappear);
        }
    }
    
    private void waitUntilEquals(String path, Function<FolderArea, String> function) {
        (new WebDriverWait(driver, 10))
                .until((Predicate<WebDriver>) webDriver -> {
                    assert webDriver != null;
                    return path.equals(function.apply(this));
                });
    }
}
