package com.qreal.wmp.uitesting.pages.editor;

import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;
import com.qreal.wmp.uitesting.pages.AbstractPage;

/** {@inheritDoc} */
public class DefaultEditorPage extends AbstractPage implements EditorPage {
    
    private final Scene scene;
    
    private final Pallete pallete;
    
    private final PropertyEditor propertyEditor;
    
    private final EditorHeaderPanel headerPanel;
    
    /**
     * Describes page of the Editor and provides components.
     */
    public DefaultEditorPage(Scene scene, Pallete pallete, PropertyEditor propertyEditor,
                      EditorHeaderPanel headerPanel) {
        super();
        this.scene = scene;
        this.pallete = pallete;
        this.propertyEditor = propertyEditor;
        this.headerPanel = headerPanel;
    }
    
    @Override
    public Scene getScene() {
        return scene;
    }
    
    @Override
    public Pallete getPallete() {
        return pallete;
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        return propertyEditor;
    }
    
    @Override
    public EditorHeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}
