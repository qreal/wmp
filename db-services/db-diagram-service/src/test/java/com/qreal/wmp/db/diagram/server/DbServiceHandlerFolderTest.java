package com.qreal.wmp.db.diagram.server;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.thrift.gen.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("testHandler")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerFolderTest {
    @Autowired
    private DiagramDao diagramDaoMocked;

    private DiagramDbServiceHandler handler;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new DiagramDbServiceHandler(context);
        }
    }

    @After
    public void deleteMocking() {
        reset(diagramDaoMocked);
    }

    /** Test createFolder operation for diagram. */
    @Test
    @Rollback
    public void createFolder_correctInput_diagramDaoCalled() throws Exception {
        TFolder tFolder = createFolder("testFolder");
        Folder folder = new Folder(tFolder);

        handler.createFolder(tFolder);

        verify(diagramDaoMocked).saveFolder(folder);
    }

    /** Test createFolder operation for diagram. */
    @Test
    @Rollback
    public void createFolder_idSet_throwsTIdAlreadyDefined() throws Exception {
        long idFolder = 0L;
        TFolder tFolder = createFolder("testFolder", idFolder);

        assertThatThrownBy(() -> handler.createFolder(tFolder)).isInstanceOf(TIdAlreadyDefined.class);
    }

    /** Test updateFolder operation for folder. */
    @Test
    @Rollback
    public void updateFolder_correctInput_diagramDaoCalled() throws Exception {
        long idFolder = 0L;
        TFolder tFolder = createFolder("testFolder", idFolder);
        Folder folder = new Folder(tFolder);

        handler.updateFolder(tFolder);

        verify(diagramDaoMocked).updateFolder(folder);
    }

    /** Test updateFolder operation for folder. */
    @Test
    @Rollback
    public void updateFolder_idNotSet_throwsTIdNotDefined() throws Exception {
        TFolder tFolder = createFolder("testDiagram");

        assertThatThrownBy((() -> handler.updateFolder(tFolder))).isInstanceOf(TIdNotDefined.class);
    }

    /** Test updateFolder operation for folder. */
    @Test
    @Rollback
    public void updateFolder_daoThrowsAborted_throwsTAborted() throws Exception {
        long idFolder = 0L;
        TFolder tFolder = createFolder("testFolder", idFolder);
        Folder folder = new Folder(tFolder);

        doThrow(new AbortedException("0", "Exception", "Exception")).
                when(diagramDaoMocked).updateFolder(folder);

        assertThatThrownBy(() -> handler.updateFolder(tFolder)).isInstanceOf(TAborted.class);
    }

    @Test
    @Rollback
    public void deleteFolder_existsFolder_diagramDaoCalled() throws Exception {
        long idFolder = 0L;

        handler.deleteFolder(idFolder);

        verify(diagramDaoMocked).deleteFolder(idFolder);
    }

    @Test
    @Rollback
    public void deleteFolder_daoThrowsAborted_throwsTAborted() throws Exception {
        long idFolder = 0L;

        doThrow(new AbortedException("0", "Exception", "Exception")).
                when(diagramDaoMocked).deleteFolder(idFolder);

        assertThatThrownBy(() -> handler.deleteFolder(idFolder)).isInstanceOf(TAborted.class);
    }

    @Test
    @Rollback
    public void getFolderTree_existsFolder_returnsFolderTree() throws Exception {
        String username = "testUser";
        Long idFolder = 0L;
        TFolder tFolder = createFolder("root", idFolder, username);
        Folder folder = new Folder(tFolder);

        when(diagramDaoMocked.getFolderTree(username)).thenReturn(folder);

        TFolder gotFolder = handler.getFolderTree(username);

        assertThat(gotFolder).isEqualTo(tFolder);
    }

    @Test
    @Rollback
    public void getFolderTree_notExistsFolder_throwsTNotFound() throws Exception {
        String username = "testUser";

        doThrow(new NotFoundException("0", "Exception")).when(diagramDaoMocked).getFolderTree(username);

        assertThatThrownBy(() -> handler.getFolderTree(username)).isInstanceOf(TNotFound.class);
    }

    private TFolder createFolder(String folderName) {
        TFolder tFolder = new TFolder();
        tFolder.setFolderName(folderName);
        return tFolder;
    }

    private TFolder createFolder(String folderName, Long idFolder) {
        TFolder tFolder = new TFolder();
        tFolder.setFolderName(folderName);
        tFolder.setId(idFolder);
        return tFolder;
    }

    private TFolder createFolder(String folderName, Long idFolder, String userName) {
        TFolder tFolder = new TFolder();
        tFolder.setFolderName(folderName);
        tFolder.setId(idFolder);

        HashSet<String> names = new HashSet<>();
        names.add(userName);
        tFolder.setOwners(names);

        return tFolder;
    }
}
