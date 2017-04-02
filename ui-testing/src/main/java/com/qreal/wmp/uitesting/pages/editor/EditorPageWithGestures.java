package com.qreal.wmp.uitesting.pages.editor;

import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;
import com.qreal.wmp.uitesting.mousegestures.GestureManipulator;
import com.qreal.wmp.uitesting.pages.AbstractPage;

/** {@inheritDoc} */
public class EditorPageWithGestures extends AbstractPage implements EditorPage {
    
    private final EditorPage editorPage;
    
    private final GestureManipulator gestureManipulator;
    
    public EditorPageWithGestures(EditorPage editorPage, GestureManipulator gestureManipulator) {
        super();
        this.editorPage = editorPage;
        this.gestureManipulator = gestureManipulator;
    }
    
    @Override
    public Scene getScene() {
        return editorPage.getScene();
    }
    
    @Override
    public Pallete getPallete() {
        return editorPage.getPallete();
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        return editorPage.getPropertyEditor();
    }
    
    @Override
    public EditorHeaderPanel getHeaderPanel() {
        return editorPage.getHeaderPanel();
    }
    
    public GestureManipulator getGestureManipulator() {
        return gestureManipulator;
    }
}
