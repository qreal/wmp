package com.qreal.wmp.db.diagram.test.server;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.Aborted;
import com.qreal.wmp.db.diagram.exceptions.NotFound;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.db.diagram.server.DiagramDbServiceHandler;
import com.qreal.wmp.thrift.gen.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerFolderTest {
    public DiagramDbServiceHandler handler;

    @Autowired
    public ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new DiagramDbServiceHandler(context);
        }
        handler.setDiagramDao(mock(DiagramDao.class));
    }

    @After
    public void deleteMocking() {
        handler.rewindDiagramDao();
    }

    /** Test createFolder operation for diagram. */
    @Test
    @Rollback
    public void createFolder_correctInput_diagramDaoCalled() throws TAborted, TIdAlreadyDefined, Aborted {
        TFolder tFolder = createFolder("testFolder");
        Folder folder = new Folder(tFolder);

        handler.createFolder(tFolder);

        verify(handler.getDiagramDao()).saveFolder(folder);
    }

    /** Test createFolder operation for diagram. */
    @Test
    @Rollback
    public void createFolder_idSet_throwsTIdAlreadyDefined() throws TAborted, TIdAlreadyDefined, Aborted {
        long idFolder = 0L;
        TFolder tFolder = createFolder("testFolder", idFolder);

        assertThatThrownBy(() -> handler.createFolder(tFolder)).isInstanceOf(TIdAlreadyDefined.class);
    }

    @Test
    @Rollback
    public void deleteFolder_existsFolder_diagramDaoCalled() throws TAborted, TIdAlreadyDefined, Aborted {
        long idFolder = 0L;

        handler.deleteFolder(idFolder);

        verify(handler.getDiagramDao()).deleteFolder(idFolder);
    }

    @Test
    @Rollback
    public void deleteFolder_daoThrowsAborted_throwsTAborted() throws TAborted, TIdAlreadyDefined, Aborted {
        long idFolder = 0L;

        doThrow(new Aborted("0", "Exception", "Exception")).when(handler.getDiagramDao()).deleteFolder(idFolder);

        assertThatThrownBy(() -> handler.deleteFolder(idFolder)).isInstanceOf(TAborted.class);
    }

    @Test
    @Rollback
    public void getFolderTree_existsFolder_returnsFolderTree() throws TNotFound, NotFound {
        String username = "testUser";
        Long idFolder = 0L;
        TFolder tFolder = createFolder("root", idFolder, username);
        Folder folder = new Folder(tFolder);

        when(handler.getDiagramDao().getFolderTree(username)).thenReturn(folder);

        TFolder gotFolder = handler.getFolderTree(username);

        assertThat(gotFolder).isEqualTo(tFolder);
    }

    @Test
    @Rollback
    public void getFolderTree_notExistsFolder_throwsTNotFound() throws TNotFound, NotFound {
        String username = "testUser";

        doThrow(new NotFound("0", "Exception")).when(handler.getDiagramDao()).getFolderTree(username);

        assertThatThrownBy(() -> handler.getFolderTree(username)).isInstanceOf(TNotFound.class);
    }

    private TFolder createFolder(String folderName) {
        TFolder tFolder = new TFolder();
        tFolder.setFolderName(folderName);
        return  tFolder;
    }

    private TFolder createFolder(String folderName, Long idFolder) {
        TFolder tFolder = new TFolder();
        tFolder.setFolderName(folderName);
        tFolder.setId(idFolder);
        return  tFolder;
    }

    private TFolder createFolder(String folderName, Long idFolder, String userName) {
        TFolder tFolder = new TFolder();
        tFolder.setFolderName(folderName);
        tFolder.setId(idFolder);
        tFolder.setUserName(userName);
        return  tFolder;
    }
}
