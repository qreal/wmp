package com.qreal.wmp.uitesting.pages.editor;

import com.qreal.wmp.uitesting.dia.palette.Palette;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;

/**
 * Describes Editor page of the WMP project.
 * Includes such components as Scene, Pallete and PropertyEditor.
 */
public interface EditorPage {
    
    Scene getScene();
    
    Palette getPalette();
    
    PropertyEditor getPropertyEditor();
    
    EditorHeaderPanel getHeaderPanel();
    
}
