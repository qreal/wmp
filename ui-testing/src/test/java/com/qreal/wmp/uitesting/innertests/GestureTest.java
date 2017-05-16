package com.qreal.wmp.uitesting.innertests;

import com.qreal.wmp.uitesting.Page;
import com.qreal.wmp.uitesting.PageLoader;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.palette.Palette;
import com.qreal.wmp.uitesting.dia.scene.Coordinate;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.mousegestures.GestureManipulator;
import com.qreal.wmp.uitesting.pages.editor.EditorPageWithGestures;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GestureTest {
    
    @Autowired
    private PageLoader pageLoader;
    
    private Scene scene;
    
    private Palette palette;
    
    private GestureManipulator gestureManipulator;
    
    @Before
    public void openEditor() {
        EditorPageWithGestures editorPage = pageLoader.load(Page.EditorRobots);
        scene = editorPage.getScene();
        palette = editorPage.getPalette();
        gestureManipulator = editorPage.getGestureManipulator();
    }
    
    @Test
    public void drawLinkTest() {
        Block initNode = scene.dragAndDrop(palette.getElement("Initial Node"), 4, 4);
        Block motorForward = scene.dragAndDrop(palette.getElement("Motors Forward"), 10, 4);
        assert scene.exist(gestureManipulator.drawLine(initNode, motorForward));
    }
    
    @Test
    public void drawTimer() {
        assert gestureManipulator.draw("Timer") != null;
    }
    
    @Test
    public void drawFinalNode() {
        assert gestureManipulator.draw("FinalNode") != null;
    }
    
    @Test
    public void drawTrikV6EnginesStop() {
        assert gestureManipulator.draw("TrikV6EnginesStop") != null;
    }
    
    @Test
    public void drawTrikV6EnginesForward() {
        assert gestureManipulator.draw("TrikV6EnginesForward") != null;
    }
    
    @Test
    public void drawTrikV6EnginesBackward() {
        assert gestureManipulator.draw("TrikV6EnginesBackward") != null;
    }
    
    @Test
    public void drawTrikSmile() {
        assert gestureManipulator.draw("TrikSmile") != null;
    }
    
    @Test
    public void drawInitialNode() {
        assert gestureManipulator.draw("InitialNode") != null;
    }
    
    @Test
    public void drawIfBlock() {
        assert gestureManipulator.draw("IfBlock") != null;
    }
    
    @Test
    public void drawFunction() {
        assert gestureManipulator.draw("Function") != null;
    }
    
    @Test
    public void randomFigure() {
        assert !gestureManipulator
                .drawByOffsets(
                        new Coordinate(100, 100),
                        Arrays.asList(50, 50, -150),
                        Arrays.asList(50, 100, 200))
                .isPresent();
    }
    
    @Test
    public void customArrow() throws ElementNotOnTheSceneException {
        Block initNode = scene.dragAndDrop(palette.getElement("Initial Node"), 4, 4);
        Block motorForward = scene.dragAndDrop(palette.getElement("Motors Forward"), 10, 4);
        Coordinate initNodeLoc = initNode.getCoordinateOnScene();
        Coordinate motorNodeLoc = motorForward.getCoordinateOnScene();
        Optional<SceneElement> created = gestureManipulator.drawByOffsets(
                initNode.getCoordinateOnScene(),
                Collections.singletonList(motorNodeLoc.getXAbsolute() - initNodeLoc.getXAbsolute()),
                Collections.singletonList(motorNodeLoc.getYAbsolute() - initNodeLoc.getYAbsolute()));
        assert created.isPresent() && created.get() instanceof Link;
    }
    
    @Test
    public void customFinalNode() {
        Optional<SceneElement> created = gestureManipulator.drawByOffsets(
                new Coordinate(100, 100),
                Arrays.asList(26, 26, 26, 26, 26, 26, 26, 26, -26, -26, -26, -26, 26, 26, 26, 26, -26, -26, -26, -26),
                Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, -26, -26, -26, -26, 26, 26, 26, 26, 26, 26, 26, 26));
        assert created.isPresent() && created.get() instanceof Block;
    }
}
