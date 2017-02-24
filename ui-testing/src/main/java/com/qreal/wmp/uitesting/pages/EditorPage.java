package com.qreal.wmp.uitesting.pages;

import com.qreal.wmp.uitesting.InitializedComponent;
import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;

import java.util.Arrays;

public class EditorPage {
	
	private final Scene scene;
	
	private final Pallete pallete;
	
	private final PropertyEditor propertyEditor;
	
	public EditorPage(Scene scene, Pallete pallete, PropertyEditor propertyEditor,
	                  InitializedComponent... components) {
		this.scene = scene;
		this.pallete = pallete;
		this.propertyEditor = propertyEditor;
		Arrays.stream(components).forEach(InitializedComponent::init);
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Pallete getPallete() {
		return pallete;
	}
	
	public PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}
}
