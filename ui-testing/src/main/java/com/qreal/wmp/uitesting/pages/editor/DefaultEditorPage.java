package com.qreal.wmp.uitesting.pages.editor;

import com.qreal.wmp.uitesting.dia.palette.Palette;
import com.qreal.wmp.uitesting.dia.property.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;
import com.qreal.wmp.uitesting.pages.AbstractPage;

/** {@inheritDoc} */
public class DefaultEditorPage extends AbstractPage implements EditorPage {
    
    private final Scene scene;
    
    private final Palette palette;
    
    private final PropertyEditor propertyEditor;
    
    private final EditorHeaderPanel headerPanel;
    
    /**
     * Describes page of the Editor and provides components.
     */
    public DefaultEditorPage(Scene scene, Palette palette, PropertyEditor propertyEditor,
                      EditorHeaderPanel headerPanel) {
        super();
        this.scene = scene;
        this.palette = palette;
        this.propertyEditor = propertyEditor;
        this.headerPanel = headerPanel;
    }
    
    public Scene getScene() {
        return scene;
    }
    
    @Override
    public Palette getPalette() {
        return palette;
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
