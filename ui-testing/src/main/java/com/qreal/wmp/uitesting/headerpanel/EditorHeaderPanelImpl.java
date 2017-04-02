package com.qreal.wmp.uitesting.headerpanel;

import com.qreal.wmp.uitesting.PageFactory;
import com.qreal.wmp.uitesting.headerpanel.folderwindow.FileItem;
import com.qreal.wmp.uitesting.headerpanel.folderwindow.FolderArea;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

/** {@inheritDoc} */
public class EditorHeaderPanelImpl implements EditorHeaderPanel {
    
    public static final By selector = By.id("main-toolbar-area");
    
    private static final Logger logger = LoggerFactory.getLogger(EditorHeaderPanel.class);
    
    private final FileItem fileItem;
    
    private final PageFactory pageFactory;
    
    private final DiagramStoreService service;
    
    private final EditorPageFacade editorPageFacade;
    
    private EditorHeaderPanelImpl(PageFactory pageFactory, WebDriver webDriver, EditorPageFacade editorPageFacade) {
        service = new DiagramStoreService();
        fileItem = new FileItem(webDriver);
        this.pageFactory = pageFactory;
        this.editorPageFacade = editorPageFacade;
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
        editorPageFacade.update();
        logger.info("New diagram");
    }
    
    @Override
    public FolderArea getFolderArea() {
        return clickFile().getSaveItem();
    }
    
    @Override
    public void saveDiagram(String path) {
        moveByFolderArea(clickFile().getSaveItem(), path);
        service.saveDiagram(path);
        logger.info("Save diagram {}", path);
    }
    
    @Override
    public void saveDiagram() {
        clickFile().saveDiagram();
        service.saveDiagram();
    }
    
    @Override
    public void openDiagram(String path) {
        moveByFolderArea(clickFile().getOpenItem(), path);
        service.openDiagram(path);
        logger.info("Open diagram {}", path);
    }
    
    @Override
    public boolean isDiagramExist(String path) {
        moveByFolderArea(clickFile().getSaveItem(), path);
        return service.isDiagramExist(path);
    }
    
    @Override
    public boolean equalsDiagrams(String path) {
        return service.equalsDrigrams(path);
    }
    
    public static EditorHeaderPanel getEditorHeaderPanel(PageFactory pageFactory,
                                                         WebDriver driver,
                                                         EditorPageFacade editorPageFacade) {
        return new EditorHeaderPanelImpl(pageFactory, driver, editorPageFacade);
    }
    
    private FileItem clickFile() {
        $(FileItem.selector).click();
        return fileItem;
    }
    
    private void moveByFolderArea(FolderArea folderArea, String path) {
        String[] steps = path.split("/");
        folderArea.move(String.join("/", Arrays.copyOf(steps, steps.length - 1)));
    }
}
