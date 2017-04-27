package com.qreal.wmp.uitesting.dia.scene;

import com.qreal.wmp.uitesting.dia.palette.PaletteElement;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.dia.scene.providers.BlockProvider;
import com.qreal.wmp.uitesting.dia.scene.providers.LinkProvider;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindowImpl;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** Provides Scene interface and getters to its components. */
public class SceneProxy implements Scene {
    
    public static final String SELECTOR = ".scene-wrapper";
    
    private final BlockProvider blockProvider;
    
    private final LinkProvider linkProvider;
    
    private final SceneWindow sceneWindow;
    
    private final Scene scene;
    
    private SceneProxy(WebDriver driver, EditorPageFacade editorPageFacade, SelectorService selectorService) {
        SelectorService sceneSelectorService = selectorService.create("scene");
        sceneWindow = SceneWindowImpl.getSceneWindow(driver);
        blockProvider = BlockProvider
                .getBlockProvider(sceneWindow, editorPageFacade, sceneSelectorService.create("poolOfElements"));
        linkProvider = LinkProvider
                .getLinkProvider(driver, editorPageFacade, sceneSelectorService.create("poolOfElements"));
        scene = new DefaultScene(driver, sceneWindow, blockProvider, linkProvider, sceneSelectorService);
    }
    
    public BlockProvider getBlockProvider() {
        return blockProvider;
    }
    
    public LinkProvider getLinkProvider() {
        return linkProvider;
    }
    
    public SceneWindow getSceneWindow() {
        return sceneWindow;
    }
    
    /** {@inheritDoc} */
    @Override
    public Block dragAndDrop(PaletteElement paletteElement) {
        return scene.dragAndDrop(paletteElement);
    }
    
    /** {@inheritDoc} */
    @Override
    public Block dragAndDrop(PaletteElement element, int cellX, int cellY) {
        return scene.dragAndDrop(element, cellX, cellY);
    }
    
    /** {@inheritDoc} */
    @Override
    public void moveToCell(Block block, int cellX, int cellY) {
        scene.moveToCell(block, cellX, cellY);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean exist(SceneElement element) {
        return scene.exist(element);
    }
    
    /** {@inheritDoc} */
    @Override
    public void remove(SceneElement element) throws ElementNotOnTheSceneException {
        scene.remove(element);
    }
    
    /** {@inheritDoc} */
    @Override
    public Link addLink(Block source, Block target) {
        return scene.addLink(source, target);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Block> getBlocks() {
        return scene.getBlocks();
    }
    
    /** {@inheritDoc} */
    @Override
    public void clean() {
        scene.clean();
    }
    
    @Contract("_, _, _ -> !null")
    public static SceneProxy getSceneProxy(
            WebDriver driver,
            EditorPageFacade editorPageFacade,
            SelectorService selectorService) {
        
        return new SceneProxy(driver, editorPageFacade, selectorService);
    }
}
