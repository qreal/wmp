package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.pallete.PalleteImpl;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.property.PropertyEditorImpl;
import com.qreal.wmp.uitesting.dia.scene.SceneProxy;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanelImpl;
import com.qreal.wmp.uitesting.mousegestures.GestureManipulatorImpl;
import com.qreal.wmp.uitesting.pages.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.title;

/** Returns page instance for requested uri. */
public class PageFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(PageFactory.class);
    
    private final WebDriver webDriver;
    
    public PageFactory(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    
    /** Returns Editor Page instance. */
    public EditorPage getEditorPage() {
        logger.info("Editor page was created");
        EditorPageFacade editorPageFacade = new EditorPageFacade();
        EditorPage page = new EditorPage(
                title(),
                SceneProxy.getSceneProxy(webDriver, editorPageFacade),
                PalleteImpl.getPallete(),
                PropertyEditorImpl.getPropertyEditor(),
                EditorHeaderPanelImpl.getEditorHeaderPanel(this, webDriver, editorPageFacade),
                GestureManipulatorImpl.getGestureManipulator(editorPageFacade)
        );
        editorPageFacade.setScene((SceneProxy) page.getScene());
        return page;
    }
    
    /** Returns Dashboard Page instance. */
    public DashboardPage getDashboardPage() {
        logger.info("Dashboard page was created");
        return new DashboardPage(title());
    }
    
    /** Returns Auth Page instance. */
    public AuthPage getAuthPage() {
        logger.info("Auth page was created");
        return new AuthPage(title());
    }
}
