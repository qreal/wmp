package com.qreal.wmp.db.diagram.test.dao;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.Aborted;
import com.qreal.wmp.db.diagram.exceptions.NotFound;
import com.qreal.wmp.db.diagram.model.Folder;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoFolderTest {
    @Autowired
    public DiagramDao diagramDao;

    //TODO how to divide this and second test?
    /** Test save operation for folder. */
    @Test
    @Rollback
    public void saveFolder_correctFolder_folderSavedInDb() throws Aborted, NotFound {
        Folder testFolder = new Folder("testFolder", "testUser");
        long idFolderCreated = diagramDao.saveFolder(testFolder);
        testFolder.setId(idFolderCreated);

        Folder gotFolder = diagramDao.getFolder(idFolderCreated);

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    //TODO how to divide this and first test?
    /** Test get operation for folder. */
    @Test
    @Rollback
    public void getFolder_correctId_folderGot() throws Aborted, NotFound {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");

        Folder gotFolder = diagramDao.getFolder(testFolder.getId());

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    /** Test saving folder tree. */
    @Test
    @Rollback
    public void getFolder_correctIdForTreeRoot_folderTreeGot() throws NotFound, Aborted {
        Folder childFolder = new Folder("child", "testUser");
        Folder parentFolder = new Folder("parent", "testUser");
        parentFolder.getChildrenFolders().add(childFolder);

        //FIXME
        //Don't understand how client can set folderParentId for hibernate
        long idFolderCreated = diagramDao.saveFolder(parentFolder);

        Folder gotFolder = diagramDao.getFolder(idFolderCreated);

        assertThat(gotFolder).isEqualTo(parentFolder);
    }

    /** Test get operation for folder. */
    @Test
    @Rollback
    public void getFolder_notCorrectId_throwsNotFound() {
        long idFolderNotCorrect = 0L;

        assertThatThrownBy(() -> diagramDao.getFolder(idFolderNotCorrect)).isInstanceOf(NotFound.class);
    }

    /** Test delete operation for folder. */
    @Test
    @Rollback
    public void deleteFolder_correctId_folderDeletedFromDb() throws Aborted {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");

        diagramDao.deleteFolder(testFolder.getId());

        assertThatThrownBy(() -> diagramDao.getFolder(testFolder.getId())).isInstanceOf(NotFound.class);
    }

    /** Test delete operation for folder. */
    @Test
    @Rollback
    public void deleteFolder_notCorrectId_throwsAborted() {
        long idFolderNotCorrect = 0L;

        assertThatThrownBy(() -> diagramDao.deleteFolder(idFolderNotCorrect)).isInstanceOf(Aborted.class);
    }

    /** Test isExists operation for folder. */
    @Test
    @Rollback
    public void isExistsFolder_existsFolder_returnsTrue() throws Aborted {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");

        assertThat(diagramDao.isExistsFolder(testFolder.getId())).isTrue();
    }

    /** Test delete operation for folder. */
    @Test
    @Rollback
    public void isExistsFolder_notExistsFolder_returnsFalse() {
        long idFolderNotCorrect = 0L;

        assertThat(diagramDao.isExistsFolder(idFolderNotCorrect)).isFalse();
    }

    /** Test getFolderTree operation for one folder. */
    @Test
    @Rollback
    public void getFolderTree_correctUsername_folderGot() throws Aborted, NotFound {
        Folder testFolder = createAndSaveFolder("root", "testUser");

        Folder gotFolder = diagramDao.getFolderTree("testUser");

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    /** Test getFolderTree operation for one folder. */
    @Test
    @Rollback
    public void getFolderTree_notCorrectUsername_throwsNotFound() {
        String notCorrectUsername = "testUser";

        assertThatThrownBy(() -> diagramDao.getFolderTree(notCorrectUsername)).isInstanceOf(NotFound.class);
    }

    private Folder createAndSaveFolder(String nameOfFolder, String nameOfUser) throws Aborted {
        Folder testFolder = new Folder(nameOfFolder, nameOfUser);
        long idFolderCreated = diagramDao.saveFolder(testFolder);
        testFolder.setId(idFolderCreated);
        return testFolder;
    }

}
