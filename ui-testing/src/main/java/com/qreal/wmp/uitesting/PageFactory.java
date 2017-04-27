package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.dia.palette.PaletteImpl;
import com.qreal.wmp.uitesting.dia.property.PropertyEditorImpl;
import com.qreal.wmp.uitesting.dia.scene.SceneProxy;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanelImpl;
import com.qreal.wmp.uitesting.mousegestures.GestureManipulatorImpl;
import com.qreal.wmp.uitesting.mousegestures.RobotCalibration;
import com.qreal.wmp.uitesting.pages.AuthPage;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import com.qreal.wmp.uitesting.pages.editor.DefaultEditorPage;
import com.qreal.wmp.uitesting.pages.editor.EditorPage;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.pages.editor.EditorPageWithGestures;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.WebDriverRunner.url;

/**
 * Returns page instance for requested uri.
 * Important: Factory must be used only if url of wanted page is opened in browser.
 */
public class PageFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(PageFactory.class);
    
    private final WebDriver webDriver;
    
    public PageFactory(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    
    /** Returns Editor Page instance. */
    public EditorPage getEditorPage(SelectorService selectorService) {
        logger.info("Editor page was created");
        EditorPageFacade editorPageFacade = new EditorPageFacade(url(), selectorService);
        EditorPage page = getDefaultEditorPage(editorPageFacade, selectorService);
        editorPageFacade.setScene((SceneProxy) page.getScene());
        return page;
    }
    
    /** Returns Editor page with gesture decorator. */
    public EditorPage getEditorPageWithGestures(SelectorService selectorService) {
        EditorPageFacade editorPageFacade = new EditorPageFacade(url(), selectorService);
        RobotCalibration.calibrate(webDriver);
        editorPageFacade.reload();
        EditorPage page = new EditorPageWithGestures(
                getDefaultEditorPage(editorPageFacade, selectorService),
                GestureManipulatorImpl.getGestureManipulator(editorPageFacade)
        );
        editorPageFacade.setScene((SceneProxy) page.getScene());
        return page;
    }
    
    /** Returns Dashboard Page instance. */
    public DashboardPage getDashboardPage() {
        logger.info("Dashboard page was created");
        return new DashboardPage();
    }
    
    /** Returns Auth Page instance. */
    public AuthPage getAuthPage() {
        logger.info("Auth page was created");
        return new AuthPage();
    }
    
    @Contract("_, _ -> !null")
    private EditorPage getDefaultEditorPage(EditorPageFacade editorPageFacade, SelectorService selectorService) {
        return new DefaultEditorPage(
                SceneProxy.getSceneProxy(webDriver, editorPageFacade, selectorService),
                PaletteImpl.getPalette(),
                PropertyEditorImpl.getPropertyEditor(),
                EditorHeaderPanelImpl.getEditorHeaderPanel(this, webDriver, editorPageFacade)
        );
    }
}
