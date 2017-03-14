package com.qreal.wmp.uitesting.headerpanel;

import com.qreal.wmp.uitesting.PageFactory;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import com.qreal.wmp.uitesting.pages.EventProvider;
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
    
    private final EventProvider eventProvider;
    
    private EditorHeaderPanelImpl(PageFactory pageFactory, WebDriver webDriver, EventProvider eventProvider) {
        service = new DiagramStoreService();
        fileItem = new FileItem(webDriver);
        this.pageFactory = pageFactory;
        this.eventProvider = eventProvider;
    }

    @Override
    public DashboardPage toDashboard() {
        $(selector).find(withText("Dashboard")).click();
        logger.info("Open dashboard");
        return pageFactory.getDashboardPage();
    }
    
    @Override
    public void newDiagram() {
        clickFile().newDiagram();
        eventProvider.resetEvent();
        logger.info("New diagram");
    }
    
    @Override
    public FolderArea getFolderArea() {
        return clickFile().getSaveItem();
    }
    
    @Override
    public void saveDiagram(String path) {
        service.saveDiagram(moveByFolderArea(path));
        logger.info("Save diagram {0}", path);
    }
    
    @Override
    public void openDiagram(String path) {
        service.openDiagram(moveByFolderArea(path));
        logger.info("Open diagram {0}", path);
    }
    
    @Override
    public boolean isDiagramExist(String path) {
        return service.isDiagramExist(moveByFolderArea(path));
    }
    
    @Override
    public boolean equalsDiagrams(String path) {
        return service.equalsDrigrams(moveByFolderArea(path));
    }
    
    public static EditorHeaderPanel getEditorHeaderPanel(PageFactory pageFactory,
                                                         WebDriver driver,
                                                         EventProvider eventProvider) {
        return new EditorHeaderPanelImpl(pageFactory, driver, eventProvider);
    }
    
    private FileItem clickFile() {
        $(FileItem.selector).click();
        return fileItem;
    }
    
    private String moveByFolderArea(String path) {
        FolderArea folderArea = getFolderArea();
        String[] steps = path.split("/");
        folderArea.move(String.join("/", Arrays.copyOf(steps, steps.length - 1)));
        return steps[steps.length - 1];
    }
}
