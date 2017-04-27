package com.qreal.wmp.uitesting.pages;

import com.qreal.wmp.uitesting.dia.palette.Palette;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;

/** Describes Editor page of the WMP project.
 * Includes such components as Scene, Palette and PropertyEditor.
 */
public class EditorPage {
    
    private final Scene scene;
    
    private final Palette palette;
    
    private final PropertyEditor propertyEditor;
    
    public EditorPage(Scene scene, Palette palette, PropertyEditor propertyEditor) {
        this.scene = scene;
        this.palette = palette;
        this.propertyEditor = propertyEditor;
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public Palette getPalette() {
        return palette;
    }
    
    public PropertyEditor getPropertyEditor() {
        return propertyEditor;
    }
}
