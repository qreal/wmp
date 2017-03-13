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
import java.util.Arrays;
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
	
	@Before
	public void openEditor() {
		EditorPage editorPage = pageLoader.load(Page.EditorRobots);
		scene = editorPage.getScene();
		pallete = editorPage.getPallete();
		headerPanel = editorPage.getHeaderPanel();
	}
	
	@Test
	public void saveDiagramTest() {
		List<Block> elements = new ArrayList<>();
		List<Link> links = new ArrayList<>();
		
		elements.add(scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4));
		elements.add(scene.dragAndDrop(pallete.getElement("Motors Forward"), 10, 4));
		links.add(scene.addLink(elements.get(0), elements.get(1)));
		elements.add(scene.dragAndDrop(pallete.getElement("Painter Color"), 16, 4));
		links.add(scene.addLink(elements.get(1), elements.get(2)));
		
		final String diagram = RandomStringUtils.random(10, alphabet);
		headerPanel.saveDiagram(diagram);
		assert headerPanel.isDiagramExist(diagram);
	}
}
