package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.dia.pallete.PalleteImpl;
import com.qreal.wmp.uitesting.dia.property.PropertyEditorImpl;
import com.qreal.wmp.uitesting.dia.scene.SceneImpl;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanelImpl;
import com.qreal.wmp.uitesting.pages.AuthPage;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import com.qreal.wmp.uitesting.pages.EditorPage;
import com.qreal.wmp.uitesting.pages.EventProvider;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.title;

/** Returns page instance for requested uri. */
public class PageFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(PageFactory.class);
    
    private final WebDriver webDriver;
    
    private final EventProvider eventProvider;
    
    public PageFactory(WebDriver webDriver) {
        this.webDriver = webDriver;
        eventProvider = new EventProvider();
    }
    
    /** Returns Editor Page instance. */
    public EditorPage getEditorPage() {
        logger.info("Editor page was created");
        EditorPage page = new EditorPage(
                title(),
                SceneImpl.getScene(webDriver),
                PalleteImpl.getPallete(),
                PropertyEditorImpl.getPropertyEditor(),
                EditorHeaderPanelImpl.getEditorHeaderPanel(this, webDriver, eventProvider)
        );
        eventProvider.addListener(page);
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
