package com.qreal.wmp.uitesting.dia.scene.providers;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.pages.editor.EditorPageFacade;
import com.qreal.wmp.uitesting.services.SelectorService;
import com.qreal.wmp.uitesting.services.SelectorService.Attribute;
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
    
    private final SelectorService selectorService;
    
    private final EditorPageFacade editorPageFacade;
    
    private Set<Block> blocks = new HashSet<>();
    
    private BlockProvider(SceneWindow sceneWindow, EditorPageFacade editorPageFacade, SelectorService selectorService) {
        this.sceneWindow = sceneWindow;
        this.selectorService = selectorService;
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
        Block block = new Block(
                newEl.attr("id"),
                By.id(newEl.attr("id")),
                editorPageFacade,
                selectorService.create("block"));
        
        blocks.add(block);
        return block;
    }
    
    public boolean exist(Block block) {
        return blocks.stream().anyMatch(anyBlock -> anyBlock.getName().equals(block.getName()));
    }
    
    public boolean isEmpty() {
        return blocks.isEmpty();
    }
    
    /** Scans scene and updates set of blocks. */
    public void recalculateBlocks() {
        blocks = $$(selectorService.get(Attribute.SELECTOR)).stream()
                .filter(x -> x.attr("class").contains(selectorService.get("block", Attribute.CLASS)))
                .map(x -> new Block(
                        x.attr("id"),
                        By.id(x.attr("id")),
                        editorPageFacade,
                        selectorService.create("block"))
                )
                .collect(Collectors.toSet());
    }
    
    @Contract("_, _, _ -> !null")
    public static BlockProvider getBlockProvider(
            SceneWindow sceneWindow,
            EditorPageFacade editorPageFacade,
            SelectorService selectorService) {
        
        return new BlockProvider(sceneWindow, editorPageFacade, selectorService);
    }
    
    /** Return new element of the scene. */
    public Optional<SelenideElement> updateBlocks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selectorService.get(Attribute.SELECTOR)));
        return allElements.stream()
                .filter(htmlElement ->
                        htmlElement.attr("class").contains(selectorService.get("block", Attribute.CLASS)) &&
                                blocks.stream().noneMatch(block -> block.getInnerSeleniumElement()
                                        .attr("id").equals(htmlElement.attr("id")))
                ).findFirst();
    }
}
