package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.dia.pallete.PalleteImpl;
import com.qreal.wmp.uitesting.dia.property.PropertyEditorImpl;
import com.qreal.wmp.uitesting.dia.scene.SceneImpl;
import com.qreal.wmp.uitesting.pages.AuthPage;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import com.qreal.wmp.uitesting.pages.EditorPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(PageFactory.class);
	
	private final WebDriver webDriver;
	
	public PageFactory(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	public EditorPage getEditorPage() {
		logger.info("Editor page was created");
		return new EditorPage(
				SceneImpl.getScene(webDriver),
				PalleteImpl.getPallete(),
				PropertyEditorImpl.getPropertyEditor()
		);
	}
	
	public DashboardPage getDashboardPage() {
		logger.info("Dashboard page was created");
		return new DashboardPage();
	}
	
	public AuthPage getAuthPage() {
		logger.info("Auth page was created");
		return new AuthPage();
	}
}
