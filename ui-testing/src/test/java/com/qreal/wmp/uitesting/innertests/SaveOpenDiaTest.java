package com.qreal.wmp.uitesting.innertests;

import com.qreal.wmp.uitesting.Page;
import com.qreal.wmp.uitesting.PageLoader;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;
import com.qreal.wmp.uitesting.pages.EditorPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SaveOpenDiaTest {
    
    private static final Logger logger = LoggerFactory.getLogger(SaveOpenDiaTest.class);
    
    @Autowired
    private PageLoader pageLoader;
    
    private Scene scene;
    
    private Pallete pallete;
    
    private EditorHeaderPanel headerPanel;
    
    private final char[] alphabet = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    
    private final List<Block> elements = new ArrayList<>();
    
    private final List<Link> links = new ArrayList<>();
    
    private String diagram;
    
    @Before
    public void openEditor() {
        EditorPage editorPage = pageLoader.load(Page.EditorRobots);
        scene = editorPage.getScene();
        pallete = editorPage.getPallete();
        headerPanel = editorPage.getHeaderPanel();
        addElements();
        diagram = RandomStringUtils.random(10, alphabet);
        headerPanel.saveDiagram(diagram);
    }
    
    @Test
    public void saveDiagramTest() {
        assert headerPanel.isDiagramExist(diagram);
    }
    
    @Test
    public void openDiagramTest() {
        headerPanel.newDiagram();
        headerPanel.openDiagram(diagram);
        assert headerPanel.equalsDiagrams(diagram);
    }
    
    @Test
    public void equalsTrueTest() {
        headerPanel.newDiagram();
        addElements();
        assert headerPanel.equalsDiagrams(diagram);
    }
    
    @Test
    public void equalsFalseTest() {
        headerPanel.newDiagram();
        addElements();
        scene.moveToCell(elements.get(0), 10, 10);
        assert !headerPanel.equalsDiagrams(diagram);
    }
    
    private void addElements() {
        elements.clear();
        links.clear();
        elements.add(scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4));
        elements.add(scene.dragAndDrop(pallete.getElement("Motors Forward"), 10, 4));
        links.add(scene.addLink(elements.get(0), elements.get(1)));
        elements.add(scene.dragAndDrop(pallete.getElement("Painter Color"), 16, 4));
        links.add(scene.addLink(elements.get(1), elements.get(2)));
    }
}
