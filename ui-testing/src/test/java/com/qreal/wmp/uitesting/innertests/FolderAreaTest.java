package com.qreal.wmp.uitesting.innertests;

import com.qreal.wmp.uitesting.Page;
import com.qreal.wmp.uitesting.PageLoader;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.dia.pallete.Pallete;
import com.qreal.wmp.uitesting.dia.scene.Scene;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.headerpanel.EditorHeaderPanel;
import com.qreal.wmp.uitesting.headerpanel.folderwindow.FolderArea;
import com.qreal.wmp.uitesting.pages.DashboardPage;
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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FolderAreaTest {
    
    private static final Logger logger = LoggerFactory.getLogger(FolderAreaTest.class);
    
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
    public void newDiagramTest() {

        List<Block> elements = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        elements.add(scene.dragAndDrop(pallete.getElement("Initial Node"), 4, 4));
        elements.add(scene.dragAndDrop(pallete.getElement("Motors Forward"), 10, 4));
        links.add(scene.addLink(elements.get(0), elements.get(1)));
        elements.add(scene.dragAndDrop(pallete.getElement("Painter Color"), 16, 4));
        links.add(scene.addLink(elements.get(1), elements.get(2)));

        headerPanel.newDiagram();
        assert elements.stream().noneMatch(x -> scene.exist(x));
        assert links.stream().noneMatch(x -> scene.exist(x));
    }

    @Test
    public void clickDashboardTest() {
        DashboardPage dashboardPage = headerPanel.toDashboard();
        assert dashboardPage.onPage();
    }

    @Test
    public void createFolderTest() {
        final String folder = RandomStringUtils.random(10, alphabet);
        try (FolderArea folderArea = headerPanel.getFolderArea()) {
            folderArea.createFolder(folder);
            assert folderArea.isFolderExist(folder);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void warnIsFolderExistTest() throws Exception {
        final String folder = RandomStringUtils.random(10, alphabet);
        try (FolderArea folderArea = headerPanel.getFolderArea()) {
            folderArea.createFolder(folder).createFolder(folder);
        }
    }

    @Test
    public void moveForwardFolderTest() {
        final String folder = RandomStringUtils.random(10, alphabet);
        try (FolderArea folderArea = headerPanel.getFolderArea()) {
            Path oldPath = Paths.get("/" + folderArea.getCurrentPath());
            Path newPath = Paths.get("/" + folderArea.createFolder(folder).moveForward(folder).getCurrentPath());
            assert newPath.startsWith(oldPath);
            assert newPath.getFileName().toString().equals(folder);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    public void moveBackFolderTest() {
        final String folder = RandomStringUtils.random(10, alphabet);
        try (FolderArea folderArea = headerPanel.getFolderArea()) {
            folderArea.createFolder(folder);
            Path oldPath = Paths.get("/" + folderArea.moveForward(folder).getCurrentPath());
            Path newPath = Paths.get("/" + folderArea.moveBack().getCurrentPath());
            assert oldPath.startsWith(newPath);
            assert oldPath.getFileName().toString().equals(folder);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    public void moveFolderTest() {
        final String folder = RandomStringUtils.random(10, alphabet) +
                "/" + RandomStringUtils.random(10, alphabet);
        try (FolderArea folderArea = headerPanel.getFolderArea()) {
            folderArea.move(folder);
            assert folderArea.getCurrentPath().equals(folder);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    @Test
    public void deleteFolderTest() {
        final String folder = RandomStringUtils.random(10, alphabet);
        try (FolderArea folderArea = headerPanel.getFolderArea()) {
            folderArea.createFolder(folder).deleteFolder(folder);
            assert !folderArea.isFolderExist(folder);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
