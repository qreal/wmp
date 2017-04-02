package com.qreal.wmp.uitesting.innertests;


import com.qreal.wmp.uitesting.Page;
import com.qreal.wmp.uitesting.PageLoader;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.mousegestures.GestureManipulator;
import com.qreal.wmp.uitesting.pages.EditorPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GestureTest {
    
    @Autowired
    private PageLoader pageLoader;
    
    private Scene scene;
    
    private Pallete pallete;
    
    private GestureManipulator gestureManipulator;
    
    @Before
    public void openEditor() {
        EditorPage editorPage = pageLoader.load(Page.EditorRobots);
        scene = editorPage.getScene();
        pallete = editorPage.getPallete();
        gestureManipulator = editorPage.getGestureManipulator();
    }
    
    @Test
    public void drawLinkTest() {
        Block initNode = scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4);
        Block motorForward = scene.dragAndDrop(pallete.getElement("Motors Forward"), 10, 4);
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
}
