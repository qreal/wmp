package com.qreal.wmp.uitesting.innertests;

import com.qreal.wmp.uitesting.Page;
import com.qreal.wmp.uitesting.PageLoader;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.palette.Palette;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;
import com.qreal.wmp.uitesting.pages.editor.EditorPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class SaveOpenDiagramTest {
    
    @Autowired
    private PageLoader pageLoader;
    
    private Scene scene;
    
    private Palette palette;
    
    private EditorHeaderPanel headerPanel;
    
    private final char[] alphabet = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    
    private final List<Block> elements = new ArrayList<>();
    
    private final List<Link> links = new ArrayList<>();
    
    private String diagram;
    
    /** Open editor page and actions before each test. */
    @Before
    public void openEditor() {
        EditorPage editorPage = pageLoader.load(Page.EditorRobots);
        scene = editorPage.getScene();
        palette = editorPage.getPalette();
        headerPanel = editorPage.getHeaderPanel();
        addElements();
        diagram = RandomStringUtils.random(10, alphabet);
        headerPanel.saveDiagram(diagram);
    }
    
    @Test
    public void saveDiagramTest() {
        assert headerPanel.isDiagramExist(diagram);
    }
    
    /*
        @Test
        public void openDiagramTest() {
            headerPanel.newDiagram();
            headerPanel.openDiagram(diagram);
            assert headerPanel.equalsDiagrams(diagram);
        }
    */
    
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
    
    @Test
    public void saveAfterChangesTest() {
        scene.moveToCell(elements.get(0), 10, 10);
        headerPanel.saveDiagram();
        assert headerPanel.equalsDiagrams(diagram);
    }
    
    private void addElements() {
        elements.clear();
        links.clear();
        elements.add(scene.dragAndDrop(palette.getElement("Initial Node"), 4, 4));
        elements.add(scene.dragAndDrop(palette.getElement("Motors Forward"), 10, 4));
        links.add(scene.addLink(elements.get(0), elements.get(1)));
        elements.add(scene.dragAndDrop(palette.getElement("Painter Color"), 16, 4));
        links.add(scene.addLink(elements.get(1), elements.get(2)));
    }
}
