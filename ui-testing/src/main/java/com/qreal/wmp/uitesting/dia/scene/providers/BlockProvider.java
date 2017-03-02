package com.qreal.wmp.uitesting.dia.scene.providers;

import com.codeborne.selenide.SelenideElement;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;

public class BlockProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(BlockProvider.class);
    
    private final SceneWindow sceneWindow;
    
    private final String selector;
    
    private Set<Block> blocks = new HashSet<>();
    
    private BlockProvider(SceneWindow sceneWindow, String selector) {
        this.sceneWindow = sceneWindow;
        this.selector = selector;
    }
    
    /** Move element to cell with x and y coordinates. */
    public void moveToCell(final Block block, final int cell_x, final int cell_y) {
        logger.info("Move element {} to cell ({}, {})", block, cell_x, cell_y);
        try {
            sceneWindow.move(block, new Coordinate(cell_x * 25, cell_y * 25));
        } catch (ElementNotOnTheSceneException e) {
            logger.error(e.getMessage());
        }
    }
    
    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks.stream().collect(Collectors.toList()));
    }
    
    /** Return added block. */
    public Block getNewBlock() {
        final SelenideElement newEl = updateBlocks().orElseThrow(NotFoundException::new);
        logger.info("Add element {} to scene", newEl);
        Block block = new Block(newEl.attr("id"), By.id(newEl.attr("id")));
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
                .map(x -> new Block(x.attr("id"), By.id(x.attr("id"))))
                .collect(Collectors.toSet());
    }
    
    @Contract("_, _ -> !null")
    public static BlockProvider getBlockProvider(SceneWindow sceneWindow, String selector) {
        return new BlockProvider(sceneWindow, selector);
    }
    
    /** Return new element of the scene. */
    private Optional<SelenideElement> updateBlocks() {
        final List<SelenideElement> allElements = $$(By.cssSelector(selector + " #v_7 > *"));
        return allElements.stream()
                .filter(htmlElement ->
                        htmlElement.attr("class").contains("element devs ImageWithPorts") &&
                                blocks.stream().noneMatch(block -> block.getInnerSeleniumElement()
                                        .attr("id").equals(htmlElement.attr("id")))
                ).findFirst();
    }
}
