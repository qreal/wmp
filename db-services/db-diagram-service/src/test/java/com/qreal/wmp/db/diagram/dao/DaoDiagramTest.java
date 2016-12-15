package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.model.Folder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoDiagramTest {
    @Autowired
    private DiagramDao diagramDao;

    //TODO how to divide this and third test?
    /** Test saveDiagram operation for diagram. */
    @Test
    @Rollback
    public void saveDiagram_correctDiagramAndFolder_diagramSavedInDb() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");

        Diagram testDiagram = new Diagram();
        testDiagram.setName("testDiagram");
        long idDiagramCreated = diagramDao.saveDiagram(testDiagram, testFolder.getId());
        testDiagram.setId(idDiagramCreated);

        Diagram gotDiagram = diagramDao.getDiagram(idDiagramCreated);

        assertThat(gotDiagram).isEqualTo(testDiagram);
    }

    /** Test saveDiagram operation for diagram. */
    @Test
    @Rollback
    public void saveDiagram_correctDiagramAndNotCorrectFolder_throwsAborted() {
        long idFolderNotCorrect = 0L;

        Diagram testDiagram = new Diagram();
        testDiagram.setName("testDiagram");

        assertThatThrownBy(() -> diagramDao.saveDiagram(testDiagram, idFolderNotCorrect)).
                isInstanceOf(AbortedException.class);
    }

    //TODO how to divide this and first test?
    /** Test getDiagram operation for diagram. */
    @Test
    @Rollback
    public void getDiagram_diagramExists_gotDiagram() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");
        Diagram testDiagram = createAndSaveDiagram("testDiagram", testFolder);

        Diagram gotDiagram = diagramDao.getDiagram(testDiagram.getId());

        assertThat(gotDiagram).isEqualTo(testDiagram);
    }

    /** Test getDiagram operation for diagram. */
    @Test
    @Rollback
    public void getDiagram_diagramNotExists_throwsNotFound() throws Exception {
        long idDiagramNotCorrect = 0L;

        assertThatThrownBy(() ->  diagramDao.getDiagram(idDiagramNotCorrect)).isInstanceOf(NotFoundException.class);
    }

    /** Test deleteDiagram operation for diagram. */
    @Test
    @Rollback
    public void deleteDiagram_diagramExists_diagramDeletedFromDb() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");
        Diagram testDiagram = createAndSaveDiagram("testDiagram", testFolder);

        diagramDao.deleteDiagram(testDiagram.getId());

        assertThatThrownBy(() -> diagramDao.getDiagram(testDiagram.getId())).isInstanceOf(NotFoundException.class);
    }

    /** Test deleteDiagram operation for diagram. */
    @Test
    @Rollback
    public void deleteDiagram_diagramNotExists_throwsAborted() throws Exception {
        long idDiagramNotCorrect = 0L;

        assertThatThrownBy(() -> diagramDao.deleteDiagram(idDiagramNotCorrect)).isInstanceOf(AbortedException.class);
    }

    /** Test isExistsDiagram operation for folder. */
    @Test
    @Rollback
    public void isExistsDiagram_diagramExists_returnsTrue() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");
        Diagram testDiagram = createAndSaveDiagram("testDiagram", testFolder);

        assertThat(diagramDao.isExistsDiagram(testDiagram.getId())).isTrue();
    }

    /** Test isExistsDiagram operation for folder. */
    @Test
    @Rollback
    public void isExistsDiagram_diagramNotExists_returnsFalse() throws Exception {
        long idDiagramNotCorrect = 0L;

        assertThat(diagramDao.isExistsDiagram(idDiagramNotCorrect)).isFalse();
    }

    /** Test updateDiagram operation for diagram. */
    @Test
    @Rollback
    public void rewriteDiagram_diagramExists_rewritesDiagramInDb() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");
        Diagram testDiagram = createAndSaveDiagram("testDiagram", testFolder);

        Diagram rewriteDiagram = new Diagram();
        rewriteDiagram.setName("testDiagramRewrite");
        rewriteDiagram.setId(testDiagram.getId());

        diagramDao.rewriteDiagram(rewriteDiagram);

        Diagram gotDiagram = diagramDao.getDiagram(testDiagram.getId());

        assertThat(gotDiagram).isEqualTo(rewriteDiagram);
    }

    /** Test updateDiagram operation for diagram. */
    @Test
    @Rollback
    public void rewriteDiagram_diagramNotExists_throwsAborted() throws Exception {
        Long diagramIdNotCorrect = 0L;

        Diagram rewriteDiagram = new Diagram();
        rewriteDiagram.setName("testDiagramRewrite");
        rewriteDiagram.setId(diagramIdNotCorrect);

        assertThatThrownBy(() -> diagramDao.rewriteDiagram(rewriteDiagram)).isInstanceOf(AbortedException.class);
    }

    private Folder createAndSaveFolder(String nameOfFolder, String nameOfUser) throws Exception {
        Folder testFolder = new Folder(nameOfFolder, nameOfUser);
        long idFolderCreated = diagramDao.saveFolder(testFolder);
        testFolder.setId(idFolderCreated);
        return testFolder;
    }

    private Diagram createAndSaveDiagram(String nameOfDiagram, Folder folder) throws Exception {
        Diagram testDiagram = new Diagram();
        testDiagram.setName(nameOfDiagram);
        long idDiagramCreated = diagramDao.saveDiagram(testDiagram, folder.getId());
        testDiagram.setId(idDiagramCreated);
        return testDiagram;
    }
}
