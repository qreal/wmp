package com.qreal.wmp.uitesting.dia.scene;

import com.qreal.wmp.uitesting.dia.pallete.PalleteElement;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.dia.scene.providers.BlockProvider;
import com.qreal.wmp.uitesting.dia.scene.providers.LinkProvider;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindowImpl;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.pages.EditorPageFacade;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class SceneProxy implements Scene {
    
    public static final String SELECTOR = ".scene-wrapper";
    
    private final BlockProvider blockProvider;
    
    private final LinkProvider linkProvider;
    
    private final Scene scene;
    
    private SceneProxy(WebDriver driver, EditorPageFacade editorPageFacade) {
        SceneWindow sceneWindow = SceneWindowImpl.getSceneWindow(driver);
        blockProvider = BlockProvider.getBlockProvider(sceneWindow, SELECTOR, editorPageFacade);
        linkProvider = LinkProvider.getLinkProvider(SELECTOR, driver, editorPageFacade);
        scene = new DefaultScene(driver, SELECTOR, sceneWindow, blockProvider, linkProvider);
    }
    
    public BlockProvider getBlockProvider() {
        return blockProvider;
    }
    
    public LinkProvider getLinkProvider() {
        return linkProvider;
    }
    
    @Override
    public Block dragAndDrop(PalleteElement palleteElement) {
        return scene.dragAndDrop(palleteElement);
    }
    
    @Override
    public Block dragAndDrop(PalleteElement element, int cellX, int cellY) {
        return scene.dragAndDrop(element, cellX, cellY);
    }
    
    @Override
    public void moveToCell(Block block, int cellX, int cellY) {
        scene.moveToCell(block, cellX, cellY);
    }
    
    @Override
    public boolean exist(SceneElement element) {
        return scene.exist(element);
    }
    
    @Override
    public void remove(SceneElement element) throws ElementNotOnTheSceneException {
        scene.remove(element);
    }
    
    @Override
    public Link addLink(Block source, Block target) {
        return scene.addLink(source, target);
    }
    
    @Override
    public List<Block> getBlocks() {
        return scene.getBlocks();
    }
    
    @Override
    public void clean() {
        scene.clean();
    }
    
    @Contract("_, _ -> !null")
    public static SceneProxy getSceneProxy(WebDriver driver, EditorPageFacade editorPageFacade) {
        return new SceneProxy(driver, editorPageFacade);
    }
}
