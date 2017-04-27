package com.qreal.wmp.uitesting.pages;

import com.qreal.wmp.uitesting.dia.palette.Palette;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;

import static com.codeborne.selenide.Selenide.title;

/**
 * Describes Editor page of the WMP project.
 * Includes such components as Scene, Pallete and PropertyEditor.
 */
public class EditorPage implements EventProvider.EventListener {
    
    private final String title;
    
    private final Scene scene;
    
    private final Palette palette;
    
    private final PropertyEditor propertyEditor;
    
    private final EditorHeaderPanel headerPanel;
    
    /** Describes page of the Editor and provides components. */
    public EditorPage(String title, Scene scene, Palette palette, PropertyEditor propertyEditor,
                      EditorHeaderPanel headerPanel) {
        this.title = title;
        this.scene = scene;
        this.palette = palette;
        this.propertyEditor = propertyEditor;
        this.headerPanel = headerPanel;
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
    
    public EditorHeaderPanel getHeaderPanel() {
        return headerPanel;
    }
    
    public boolean onPage() {
        return title.equals(title());
    }
    
    @Override
    public void updateEvent() {
        ((Resettable) scene).reset();
    }
}
