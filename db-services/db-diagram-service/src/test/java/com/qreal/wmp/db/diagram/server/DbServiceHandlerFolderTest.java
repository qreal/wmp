package com.qreal.wmp.db.diagram.server;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.mapping.RelationMapping;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.thrift.gen.TFolder;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerFolderTest {

    @Autowired
    private DiagramDao diagramDao;

    private DiagramDbServiceHandler handler;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setHandler() {
        if (handler == null) {
            handler = new DiagramDbServiceHandler(context);
        }
    }

    /** Test shareTo operation. */
    @Test
    @Rollback
    public void shareTo_noSharedExists_Shares() throws TException {
        RelationMapping mapper = new RelationMapping(diagramDao);

        Folder rootFolderFirst = createAndSaveFolder("root", "123");
        TFolder testFolder = handler.getFolder(rootFolderFirst.getId(), "123");
        Folder rootFolderSecond = createAndSaveFolder("root", "1234");
        Folder folderToShare = createAndSaveFolder("1234", "1234", rootFolderSecond);

        handler.shareFolderTo("123", folderToShare.toTFolder("1234"));

        TFolder gotTFolder = handler.getFolder(rootFolderFirst.getId(), "123");
        Folder gotFolder = mapper.convertTFolder(gotTFolder);

        assertThat(gotFolder.getChildrenFolders().contains(folderToShare));

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
