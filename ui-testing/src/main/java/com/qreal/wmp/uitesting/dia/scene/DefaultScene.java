package com.qreal.wmp.uitesting.dia.scene;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Predicate;
import com.qreal.wmp.uitesting.dia.palette.PaletteElement;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.dia.scene.providers.BlockProvider;
import com.qreal.wmp.uitesting.dia.scene.providers.LinkProvider;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/** {@inheritDoc} */
public class DefaultScene implements Scene {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultScene.class);
    
    private final WebDriver webDriver;
    
    private final SceneWindow sceneWindow;
    
    private final BlockProvider blockProvider;
    
    private final LinkProvider linkProvider;
    
    private final SelectorService selectorService;
    
    /** For actions such as mouse move we need driver of current page. */
    public DefaultScene(WebDriver webDriver,
                        SceneWindow sceneWindow,
                        BlockProvider blockProvider,
                        LinkProvider linkProvider,
                        SelectorService selectorService) {
        
        this.webDriver = webDriver;
        // For actions such as mouse move we need driver of current page.
        if (webDriver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) webDriver).executeScript(
                    createDiv("SceneWindowLeft") + createDiv("SceneWindowTop") +
                            createDiv("SceneWindowHorSize") + createDiv("SceneWindowVerSize")
            );
        }
        this.sceneWindow = sceneWindow;
        this.blockProvider = blockProvider;
        this.linkProvider = linkProvider;
        this.selectorService = selectorService;
    }
    
    @Override
    public Block dragAndDrop(final PaletteElement element) {
        element.getInnerSeleniumElement().dragAndDropTo("#" + selectorService.get(SelectorService.Attribute.ID));
        return blockProvider.getNewBlock();
    }
    
    @Override
    public Block dragAndDrop(final PaletteElement element, int cellX, int cellY) {
        Block newBlock = dragAndDrop(element);
        blockProvider.moveToCell(newBlock, cellX, cellY);
        return newBlock;
    }
    
    @Override
    public void moveToCell(Block block, int cellX, int cellY) {
        blockProvider.moveToCell(block, cellX, cellY);
    }
    
    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean exist(SceneElement element) {
        if (element instanceof Block) {
            blockProvider.recalculateBlocks();
            return blockProvider.exist((Block) element);
        }
        if (element instanceof Link) {
            linkProvider.recalculateLinks();
            return linkProvider.exist((Link) element);
        }
        return false;
    }
    
    @Override
    public void remove(SceneElement element) throws ElementNotOnTheSceneException {
        if (element instanceof Link) {
            removeSceneElement(((Link) element).getTarget());
        } else {
            removeSceneElement(element);
        }
    }
    
    @Override
    public Link addLink(final Block source, final Block target) {
        return linkProvider.addLink(source, target);
    }
    
    @Override
    public List<Block> getBlocks() {
        return blockProvider.getBlocks();
    }
    
    @Override
    public void clean() {
        blockProvider.recalculateBlocks();
        linkProvider.recalculateLinks();
        if (!linkProvider.isEmpty()) {
            try {
                remove(linkProvider.getLinks().get(0));
            } catch (ElementNotOnTheSceneException e) {
                logger.error("It's impossible to remove link, because it is not on the Scene.");
            }
            clean();
        } else {
            if (!blockProvider.isEmpty()) {
                try {
                    remove(blockProvider.getBlocks().get(0));
                } catch (ElementNotOnTheSceneException e) {
                    logger.error("It's impossible to remove block, because it is not on the scene.");
                }
                clean();
            } else {
                logger.info("Clean scene");
            }
        }
    }
    
    public BlockProvider getBlockProvider() {
        return blockProvider;
    }
    
    public LinkProvider getLinkProvider() {
        return linkProvider;
    }
    
    @Contract(pure = true)
    private static String createDiv(String divName) {
        return "$('body').append('<div id=\"" + divName + "\" style=\"position:absolute;visibility:hidden;\"></div>');";
    }
    
    @Contract("null -> fail")
    private void removeSceneElement(SceneElement sceneElement) throws ElementNotOnTheSceneException {
        sceneWindow.focus(sceneElement.getCoordinateOnScene());
        logger.info("Remove element {} form scene", sceneElement.getInnerSeleniumElement().toString());
        new Actions(webDriver)
                .contextClick(sceneElement.getInnerSeleniumElement())
                .build()
                .perform();
        SelenideElement contextMenu = $(By.id(selectorService.get("contextMenu", SelectorService.Attribute.ID)));
        contextMenu.shouldBe(Condition.visible);
        contextMenu.click();
        (new WebDriverWait(webDriver, 5))
                .until((Predicate<WebDriver>) webDriver -> {
                    assert webDriver != null;
                    return webDriver.findElements(sceneElement.getBy()).size() == 0;
                });
    }
}
