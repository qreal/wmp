package com.qreal.wmp.uitesting.headerpanel;

import com.qreal.wmp.uitesting.PageFactory;
import com.qreal.wmp.uitesting.headerpanel.folderwindow.FileItem;
import com.qreal.wmp.uitesting.headerpanel.folderwindow.FolderArea;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;

/** {@inheritDoc} */
public class EditorHeaderPanelImpl implements EditorHeaderPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(EditorHeaderPanel.class);
    
    private final FileItem fileItem;
    
    private final PageFactory pageFactory;
    
    private final DiagramStoreService service;
    
    private final EditorPageFacade editorPageFacade;
    
    private final SelectorService selectorService;
    
    private EditorHeaderPanelImpl(
            PageFactory pageFactory,
            WebDriver webDriver,
            EditorPageFacade editorPageFacade,
            SelectorService selectorService) {
        
        service = new DiagramStoreService(editorPageFacade, selectorService);
        fileItem = new FileItem(webDriver, selectorService);
        this.pageFactory = pageFactory;
        this.editorPageFacade = editorPageFacade;
        this.selectorService = selectorService;
    }

    @Override
    public DashboardPage toDashboard() {
        $(By.id(selectorService.get("dashboardButton", SelectorService.Attribute.ID))).click();
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
                                                         EditorPageFacade editorPageFacade,
                                                         SelectorService selectorService) {
        
        return new EditorHeaderPanelImpl(pageFactory, driver, editorPageFacade, selectorService);
    }
    
    private FileItem clickFile() {
        $(By.id(selectorService.get("fileItem", SelectorService.Attribute.ID))).click();
        return fileItem;
    }
    
    private void moveByFolderArea(FolderArea folderArea, String path) {
        String[] steps = path.split("/");
        folderArea.move(String.join("/", Arrays.copyOf(steps, steps.length - 1)));
    }
}
