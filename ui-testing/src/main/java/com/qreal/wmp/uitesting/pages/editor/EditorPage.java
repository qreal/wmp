package com.qreal.wmp.uitesting.pages.editor;

import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;

/** Describes Editor page of the WMP project.
 * Includes such components as Scene, Pallete and PropertyEditor.
 */
public interface EditorPage {
    
    Scene getScene();
    
    Pallete getPallete();
    
    PropertyEditor getPropertyEditor();
    
    EditorHeaderPanel getHeaderPanel();
    
}
