package com.qreal.wmp.uitesting.innertests;

import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.propery_editor.PropertyEditor;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import com.qreal.wmp.uitesting.services.Opener;
import org.junit.After;
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
public class DiaTest {

    @Autowired
    private Opener opener;

    @Autowired
    private Pallete pallete;

    @Autowired
    private Scene scene;

    @Autowired
    private PropertyEditor propertyEditor;
    
    /** Setup browser. */
    @Before
    public void openEditor() {
        opener.open("editor");
    }

    /** Drag element from pallete and drop on the scene. */
    @Test
    public void dragAndDrop() {
        final Block initialNode = scene.dragAndDrop(pallete.getElement("Initial Node"));
        assert scene.exist(initialNode);
    }

    /** Remove element from scene. */
    @Test
    public void remove() {
        final Block sceneElement = scene.dragAndDrop(pallete.getElement("Initial Node"));
        assert scene.exist(sceneElement);
        scene.remove(sceneElement);
        assert !scene.exist(sceneElement);
    }

    /** Add two elements and link them. */
    @Test
    public void addLink() {
        final Block initNode = scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4);
        final Block finalNode = scene.dragAndDrop(pallete.getElement("Final Node"), 4, 70);
        final Block motor = scene.dragAndDrop(pallete.getElement("Motors Forward"), 4, 7);
        Link link = scene.addLink(initNode, motor);
        Link link2 = scene.addLink(motor, finalNode);
        scene.moveToCell(motor, 72, 64);
        assert scene.exist(link);
        assert scene.exist(link2);
    }

    /** Set property 'Ports' of motor forward item to '123' and checks that all is correct. */
    @Test
    public void propertyEditor() {
        final Block motor = scene.dragAndDrop(pallete.getElement("Motors Forward"));
        propertyEditor.setProperty(motor.getInnerSeleniumElement(), "Ports", "123");
        assert propertyEditor.getProperty(motor.getInnerSeleniumElement(), "Ports").equals("123");
    }

    /** Move element to cell. */
    @Test
    public void moveElement() {
        final Block motor = scene.dragAndDrop(pallete.getElement("Motors Forward"));
        try {
            scene.moveToCell(motor, 40, 40);
            assert motor.getCoordinateOnScene().getXCell() == 40 && motor.getCoordinateOnScene().getYCell() == 40;
            scene.moveToCell(motor, 72, 64);
            assert motor.getCoordinateOnScene().getXCell() == 72 && motor.getCoordinateOnScene().getYCell() == 64;
            scene.moveToCell(motor, 0, 0);
            assert motor.getCoordinateOnScene().getXCell() == 0 && motor.getCoordinateOnScene().getYCell() == 0;
        } catch (ElementNotOnTheSceneException e) {
            e.printStackTrace();
        }
    }

    /** Close the browser. */
    @After
    public void cleanScene() {
        scene.clean();
    }
}
