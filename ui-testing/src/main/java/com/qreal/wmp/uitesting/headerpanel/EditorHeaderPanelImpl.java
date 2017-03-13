package com.qreal.wmp.uitesting.headerpanel;

import com.qreal.wmp.uitesting.PageFactory;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class EditorHeaderPanelImpl implements EditorHeaderPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(EditorHeaderPanel.class);
    
    public static final By selector = By.id("main-toolbar-area");

    private final FileItem fileItem;
    
    private final PageFactory pageFactory;
    
    private final DiagramStoreService service;
    
    private EditorHeaderPanelImpl(PageFactory pageFactory, WebDriver webDriver) {
        service = new DiagramStoreService();
        fileItem = new FileItem(webDriver);
        this.pageFactory = pageFactory;
    }

    @Override
    public DashboardPage toDashboard() {
        $(selector).find(withText("Dashboard")).click();
        return pageFactory.getDashboardPage();
    }
    
    @Override
    public void newDiagram() {
        clickFile().newDiagram();
    }
    
    @Override
    public FolderArea getFolderArea() {
        return clickFile().getSaveItem();
    }
    
    @Override
    public void saveDiagram(String path) {
        try (FolderArea folderArea = getFolderArea()) {
            String[] steps = path.split("/");
            folderArea.move(String.join("/", Arrays.copyOf(steps, steps.length - 1)));
            service.addDiagram(steps[steps.length - 1]);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Override
    public void openDiagram() {
        
    }
    
    @Override
    public boolean isDiagramExist(String path) {
        try (FolderArea folderArea = getFolderArea()) {
            String[] steps = path.split("/");
            folderArea.move(String.join("/", Arrays.copyOf(steps, steps.length - 1)));
            return service.isDiagramExist(steps[steps.length - 1]);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
    
    private FileItem clickFile() {
        $(FileItem.selector).click();
        return fileItem;
    }
    
    public static EditorHeaderPanel getEditorHeaderPanel(PageFactory pageFactory, WebDriver driver) {
        return new EditorHeaderPanelImpl(pageFactory, driver);
    }
}
