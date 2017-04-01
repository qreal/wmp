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
}
