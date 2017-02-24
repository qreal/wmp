package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.pages.AuthPage;
import com.qreal.wmp.uitesting.pages.DashboardPage;
import com.qreal.wmp.uitesting.pages.EditorPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(PageFactory.class);
	
	private final Scene scene;
	
	private final PropertyEditor propertyEditor;
	
	private final Pallete pallete;
	
	public PageFactory(Scene scene, PropertyEditor propertyEditor, Pallete pallete) {
		this.scene = scene;
		this.propertyEditor = propertyEditor;
		this.pallete = pallete;
	}
	
	public EditorPage getEditorPage() {
		logger.info("Editor page was created");
		return new EditorPage(scene, pallete, propertyEditor, (InitializedComponent) scene);
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
