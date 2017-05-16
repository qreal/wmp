package com.qreal.wmp.uitesting.dia.scene.providers;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;

/** Encapsulates blocks operations. */
public class BlockProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(BlockProvider.class);
    
    private final SceneWindow sceneWindow;
    
    private final String selector;
    
    private final EditorPageFacade editorPageFacade;
    
    private Set<Block> blocks = new HashSet<>();
    
    private BlockProvider(SceneWindow sceneWindow, String selector, EditorPageFacade editorPageFacade) {
        this.sceneWindow = sceneWindow;
        this.selector = selector;
        this.editorPageFacade = editorPageFacade;
    }
    
    /** Move element to cell with x and y coordinates. */
    public void moveToCell(final Block block, final int cellX, final int cellY) {
        logger.info("Move element {} to cell ({}, {})", block, cellX, cellY);
        try {
            sceneWindow.move(block,
                    new Coordinate(cellX * Coordinate.POINT_IN_CELL, cellY * Coordinate.POINT_IN_CELL));
        } catch (ElementNotOnTheSceneException e) {
            logger.error("It is impossible to move element, which is not on the Scene");
        }
    }
    
    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks.stream().collect(Collectors.toList()));
    }
    
    /** Return added block. */
    public Block getNewBlock() {
        final SelenideElement newEl = updateBlocks().orElseThrow(NotFoundException::new);
        logger.info("Add element {} to scene", newEl);
        Block block = new Block(newEl.attr("id"), By.id(newEl.attr("id")), editorPageFacade);
        blocks.add(block);
        return block;
    }
    
    public boolean exist(Block block) {
        return blocks.stream().anyMatch(anyBlock -> anyBlock.getName().equals(block.getName()));
    }
    
    public boolean isEmpty() {
        return blocks.isEmpty();
    }
    
    public void recalculateBlocks() {
        blocks = $$(By.cssSelector(selector + " #v_7 > *")).stream()
                .filter(x -> x.attr("class").contains(Block.CLASS_NAME))
                .map(x -> new Block(x.attr("id"), By.id(x.attr("id")), editorPageFacade))
                .collect(Collectors.toSet());
    }
    
    @Contract("_, _, _ -> !null")
    public static BlockProvider getBlockProvider(
            SceneWindow sceneWindow,
            String selector,
            EditorPageFacade editorPageFacade) {
        
        return new BlockProvider(sceneWindow, selector, editorPageFacade);
    }
    
    /** Return new element of the scene. */
    public Optional<SelenideElement> updateBlocks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selector + " #v_7 > *"));
        return allElements.stream()
                .filter(htmlElement ->
                        htmlElement.attr("class").contains("element devs ImageWithPorts") &&
                                blocks.stream().noneMatch(block -> block.getInnerSeleniumElement()
                                        .attr("id").equals(htmlElement.attr("id")))
                ).findFirst();
    }
}
