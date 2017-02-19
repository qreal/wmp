package com.qreal.wmp.uitesting.testspace;

import com.qreal.wmp.uitesting.Opener;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.model.Block;
import com.qreal.wmp.uitesting.dia.model.Link;
import com.qreal.wmp.uitesting.dia.services.Pallete;
import com.qreal.wmp.uitesting.dia.services.PropertyEditor;
import com.qreal.wmp.uitesting.dia.services.Scene;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DiagramConstructingTest {

    @Autowired
    private Opener opener;

    @Autowired
    private Pallete pallete;

    @Autowired
    private Scene scene;

    @Autowired
    private PropertyEditor propertyEditor;

    private ArrayList<Block> elements;
    private ArrayList<Link> links;

    @Before
    public void openEditor() {
        opener.open("editor");
     
        elements = new ArrayList<>();
        links = new ArrayList<>();

        elements.add(scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4));
        elements.add(scene.dragAndDrop(pallete.getElement("Motors Forward"), 10, 4));
        links.add(scene.addLink(elements.get(0), elements.get(1)));
        elements.add(scene.dragAndDrop(pallete.getElement("Painter Color"), 16, 4));
        links.add(scene.addLink(elements.get(1), elements.get(2)));
        elements.add(scene.dragAndDrop(pallete.getElement("Timer"), 22, 4));
        links.add(scene.addLink(elements.get(2), elements.get(3)));
        elements.add(scene.dragAndDrop(pallete.getElement("Final Node"), 28, 4));
        links.add(scene.addLink(elements.get(3), elements.get(4)));
    }

    @Test
    public void digramFiveNodes() {
        assert allExist();
    }

    @Test
    public void moveSomeNodes() {
        scene.moveToCell(elements.get(1), 20, 20);
        scene.moveToCell(elements.get(0), 20, 10);
        scene.moveToCell(elements.get(1), 0, 20);
        assert allExist();
    }

    @Test
    public void fillProperties() {
        propertyEditor.setProperty(elements.get(1).getInnerSeleniumElement(), "Power", "80");
        assert propertyEditor.getProperty(elements.get(1).getInnerSeleniumElement(), "Power").equals("80");
        propertyEditor.setProperty(elements.get(2).getInnerSeleniumElement(), "Color", "green");
        assert propertyEditor.getProperty(elements.get(2).getInnerSeleniumElement(), "Color").equals("green");
        propertyEditor.setProperty(elements.get(3).getInnerSeleniumElement(), "Delay", "200");
        assert propertyEditor.getProperty(elements.get(3).getInnerSeleniumElement(), "Delay").equals("200");
    }

    @After
    public void cleanScene() {
        scene.clean();
    }

    private boolean allExist() {
        return elements.stream().allMatch(scene::exist) && links.stream().anyMatch(scene::exist);
    }

}
