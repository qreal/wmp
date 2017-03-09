package com.qreal.wmp.uitesting.pages;

import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;

import static com.codeborne.selenide.Selenide.title;

public class EditorPage {
    
    private final String title;
    
    private final Scene scene;
    
    private final Pallete pallete;
    
    private final PropertyEditor propertyEditor;
    
    private final EditorHeaderPanel headerPanel;
    
    /** Describes page of the Editor and provides components. */
    public EditorPage(String title, Scene scene, Pallete pallete, PropertyEditor propertyEditor,
                      EditorHeaderPanel headerPanel) {
        this.title = title;
        this.scene = scene;
        this.pallete = pallete;
        this.propertyEditor = propertyEditor;
        this.headerPanel = headerPanel;
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
    
    public EditorHeaderPanel getHeaderPanel() {
        return headerPanel;
    }
    
    public boolean onPage() {
        return title.equals(title());
    }
}
