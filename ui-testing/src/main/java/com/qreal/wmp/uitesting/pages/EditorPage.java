package com.qreal.wmp.uitesting.pages;

import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;
import com.qreal.wmp.uitesting.mousegestures.GestureManipulator;

import static com.codeborne.selenide.Selenide.title;

/** Describes Editor page of the WMP project.
 * Includes such components as Scene, Pallete and PropertyEditor.
 */
public class EditorPage {
    
    private final String title;
    
    private final Scene scene;
    
    private final Pallete pallete;
    
    private final PropertyEditor propertyEditor;
    
    private final EditorHeaderPanel headerPanel;
    
    private final GestureManipulator gestureManipulator;
    
    /**
     * Describes page of the Editor and provides components.
     */
    public EditorPage(String title, Scene scene, Pallete pallete, PropertyEditor propertyEditor,
                      EditorHeaderPanel headerPanel, GestureManipulator gestureManipulator) {
        this.title = title;
        this.scene = scene;
        this.pallete = pallete;
        this.propertyEditor = propertyEditor;
        this.headerPanel = headerPanel;
        this.gestureManipulator = gestureManipulator;
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
    
    public GestureManipulator getGestureManipulator() {
        return gestureManipulator;
    }
}
