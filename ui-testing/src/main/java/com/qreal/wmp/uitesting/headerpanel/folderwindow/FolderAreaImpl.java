package com.qreal.wmp.uitesting.headerpanel.folderwindow;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.function.Function;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

/** {@inheritDoc} */
public class FolderAreaImpl implements FolderArea {
    
    public static final By selector = By.cssSelector("#diagrams .modal-content");
    
    private final WebDriver driver;
    
    public FolderAreaImpl(WebDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public FolderArea createFolder(String folderName) {
        $(selector).find(By.id("creating-menu")).click();
        $(selector).find(By.className("folder-menu")).find(By.cssSelector("[type=\"text\"]")).setValue(folderName);
        $(selector).find(By.className("folder-menu")).find(By.id("creating")).click();
        if ($(selector).find(By.className("warning-message")).isDisplayed()) {
            throw new IllegalArgumentException("The folder with this name already exists");
        }
        return this;
    }
    
    @Override
    public boolean isFolderExist(String name) {
        return $(selector).findAll(By.className("folders")).stream().anyMatch(elem -> elem.has(text(name)));
    }
    
    @Override
    public FolderArea moveForward(String name) {
        if (!isFolderExist(name)) {
            throw new IllegalArgumentException("Folder with name " + name + " does not exist");
        }
        String oldPath = getCurrentPath();
        oldPath = "".equals(oldPath) ? name : oldPath + "/" + name;
        $(selector).find(By.className("folders")).find(Selectors.byText(name)).click();
        waitUntilEquals(oldPath, FolderArea::getCurrentPath);
        return this;
    }
    
    @Override
    public FolderArea moveBack() {
        String oldPath = getCurrentPath();
        $(selector).find(By.id("level-up")).click();
        String[] steps = oldPath.split("/");
        String diff = String.join("/", Arrays.copyOf(steps, steps.length - 1));
        waitUntilEquals(diff, FolderArea::getCurrentPath);
        return this;
    }
    
    @Override
    public String getCurrentPath() {
        String result = $(selector).find(By.className("folder-path")).find(By.tagName("p")).getText();
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
            throw new NullPointerException("Folder is not exist");
        }
        $(selector).find(By.className("folders")).find(Selectors.byText(name)).contextClick();
        $(By.id("open-diagram-context-menu")).should(Condition.visible);
        $(By.id("open-diagram-context-menu")).click();
        return this;
    }
    
    @Override
    public void close() {
        SelenideElement closeButton = $(selector).find(By.className("close"));
        if ($(selector).isDisplayed() && closeButton.isDisplayed()) {
            $(selector).find(By.className("close")).click();
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
