package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
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
public class DaoFolderTest {
    @Autowired
    private DiagramDao diagramDao;

    //TODO how to divide this and second test?
    /** Test save operation for folder. */
    @Test
    @Rollback
    public void saveFolder_correctFolder_folderSavedInDb() throws Exception {
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
    public void getFolder_correctId_folderGot() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");

        Folder gotFolder = diagramDao.getFolder(testFolder.getId());

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    /** Test saving folder tree. */
    @Test
    @Rollback
    public void getFolder_correctIdForTreeRoot_folderTreeGot() throws Exception {
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

        assertThatThrownBy(() -> diagramDao.getFolder(idFolderNotCorrect)).isInstanceOf(NotFoundException.class);
    }

    /** Test delete operation for folder. */
    @Test
    @Rollback
    public void deleteFolder_correctId_folderDeletedFromDb() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");

        diagramDao.deleteFolder(testFolder.getId());

        assertThatThrownBy(() -> diagramDao.getFolder(testFolder.getId())).isInstanceOf(NotFoundException.class);
    }

    /** Test delete operation for folder. */
    @Test
    @Rollback
    public void deleteFolder_notCorrectId_throwsAborted() {
        long idFolderNotCorrect = 0L;

        assertThatThrownBy(() -> diagramDao.deleteFolder(idFolderNotCorrect)).isInstanceOf(AbortedException.class);
    }

    /** Test isExists operation for folder. */
    @Test
    @Rollback
    public void isExistsFolder_existsFolder_returnsTrue() throws Exception {
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

    /** Test updateFolder operation for folder. */
    @Test
    @Rollback
    public void updateFolder_folderExists_updatesFolderInDb() throws Exception {
        Folder testFolder = createAndSaveFolder("testFolder", "testUser");

        Folder updateFolder = new Folder();
        updateFolder.setFolderName("testFolderRewrite");
        updateFolder.setId(testFolder.getId());
        updateFolder.getOwners().add("testUser");
        updateFolder.getOwners().add("testUser1");

        diagramDao.updateFolder(updateFolder);

        Folder gotFolder = diagramDao.getFolder(testFolder.getId());

        assertThat(gotFolder).isEqualTo(updateFolder);
    }

    /** Test updateFolder operation for folder. */
    @Test
    @Rollback
    public void updateFolder_folderNotExists_throwsAborted() throws Exception {
        Long folderIdNotCorrect = 0L;

        Folder updateFolder = new Folder();
        updateFolder.setFolderName("testFolderRewrite");
        updateFolder.setId(folderIdNotCorrect);

        assertThatThrownBy(() -> diagramDao.updateFolder(updateFolder)).isInstanceOf(AbortedException.class);
    }

    /** Test getFolderTree operation for one folder. */
    @Test
    @Rollback
    public void getFolderTree_correctUsername_folderGot() throws Exception {
        Folder testFolder = createAndSaveFolder("root", "testUser");

        Folder gotFolder = diagramDao.getFolderTree("testUser");

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    /** Test getFolderTree operation for one folder. */
    @Test
    @Rollback
    public void getFolderTree_notCorrectUsername_throwsNotFound() {
        String notCorrectUsername = "testUser";

        assertThatThrownBy(() -> diagramDao.getFolderTree(notCorrectUsername)).isInstanceOf(NotFoundException.class);
    }

    private Folder createFolder(String folderName, String username) {
        return new Folder(folderName, username);
    }

    private Folder createFolder(String folderName, String username, long id) {
        return new Folder(folderName, username, id);
    }

    private Folder createFolder(String folderName, String username, long id, Folder parent) {
        Folder folder = createFolder(folderName, username, id);
        parent.getChildrenFolders().add(folder);
        folder.getParentFolders().add(parent);
        return folder;
    }

    private Folder createAndSaveFolder(String folderName, String username) {
        Folder folder = new Folder(folderName, username);
        long idFolder = saveFolder(folder);
        folder.setId(idFolder);
        return folder;
    }

    private Folder createAndSaveFolder(String folderName, String username, Folder parent) {
        Folder folder = new Folder(folderName, username);
        parent.getChildrenFolders().add(folder);
        folder.getParentFolders().add(parent);
        long idFolder = saveFolder(folder);
        folder.setId(idFolder);
        return folder;
    }

    private long saveFolder(Folder folder) {
        long id = 0;
        try {
            id = diagramDao.saveFolder(folder);
        } catch (AbortedException e) {
            //For now never happens
        }
        return id;
    }

}
