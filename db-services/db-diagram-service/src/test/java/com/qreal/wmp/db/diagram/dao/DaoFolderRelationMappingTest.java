package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.model.Folder;
import com.qreal.wmp.db.diagram.server.RelationMapping;
import com.qreal.wmp.thrift.gen.TFolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoFolderRelationMappingTest {
    @Autowired
    private DiagramDao diagramDao;

    @Test
    @Rollback
    public void tFolderToFolderRoot_fullTreeGave_gotTree() throws Exception {
        RelationMapping mapper = new RelationMapping(diagramDao);

        Folder testFolder = createFolder("testFolder", "testUser", 1L);
        Folder testFolder1 = createFolder("testFolder1", "testUser", 2L, testFolder);

        TFolder testTFolder = createTFolder(testFolder.getFolderName(), testFolder.getOwners(), testFolder.getId());
        TFolder testTFolder1 = createTFolder(testFolder1.getFolderName(), testFolder1.getOwners(), testFolder1.getId(),
                testTFolder);

        Folder gotFolder = mapper.convertTFolder(testTFolder);

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    @Test
    @Rollback
    public void folderToTFolderRoot_fullTreeGave_gotTree() throws Exception {
        Folder testFolder = createFolder("testFolder", "testUser", 1L);
        Folder testFolder1 = createFolder("testFolder1", "testUser", 2L, testFolder);

        TFolder testTFolder = createTFolder(testFolder.getFolderName(), testFolder.getOwners(), testFolder.getId());
        TFolder testTFolder1 = createTFolder(testFolder1.getFolderName(), testFolder1.getOwners(), testFolder1.getId(),
                testTFolder);

        TFolder gotFolder = testFolder.toTFolder("testUser");

        assertThat(gotFolder).isEqualTo(testTFolder);
    }

    @Test
    @Rollback
    public void folderToTFolderLeaf_fullTreeGave_gotTree() throws Exception {
        Folder testFolder = createFolder("testFolder", "testUser", 1L);
        Folder testFolder1 = createFolder("testFolder1", "testUser", 2L, testFolder);

        TFolder testTFolder = createTFolder(testFolder.getFolderName(), testFolder.getOwners(), testFolder.getId());
        TFolder testTFolder1 = createTFolder(testFolder1.getFolderName(), testFolder1.getOwners(), testFolder1.getId(),
                testTFolder);

        TFolder gotFolder = testFolder1.toTFolder("testUser");

        assertThat(gotFolder).isEqualTo(testTFolder1);
    }

    @Test
    @Rollback
    public void tFolderToFolderLeaf_withLoading_gotTree() throws Exception {
        RelationMapping mapper = new RelationMapping(diagramDao);

        Folder testFolder = createAndSaveFolder("testFolder", "testUser");
        Folder testFolder1 = createFolder("testFolder1", "testUser", 2L, testFolder);

        TFolder testTFolder = createTFolder(testFolder.getFolderName(), testFolder.getOwners(), testFolder.getId());
        TFolder testTFolder1 = createTFolder(testFolder1.getFolderName(), testFolder1.getOwners(), testFolder1.getId(),
                testTFolder);

        Folder gotFolder = mapper.convertTFolder(testTFolder1);

        assertThat(gotFolder).isEqualTo(testFolder1);
        //assertThat(gotFolder.getParentFolders()).isEqualTo(testFolder1.getParentFolders());
    }

    @Test
    @Rollback
    public void tFolderToFolderRoot_withLoading_gotTree3() throws Exception {
        RelationMapping mapper = new RelationMapping(diagramDao);

        Folder testFolder = createAndSaveFolder("testFolder", "testUser");
        Folder testFolder1 = createAndSaveFolder("testFolder1", "testUser", testFolder);
        Folder testFolder2 = createFolder("testFolder2", "testUser", 3L, testFolder1);

        TFolder testTFolder = createTFolder(testFolder.getFolderName(), testFolder.getOwners(), testFolder.getId());
        TFolder testTFolder1 = createTFolder(testFolder1.getFolderName(), testFolder1.getOwners(), testFolder1.getId(),
                testTFolder);
        TFolder testTFolder2 = createTFolder(testFolder2.getFolderName(), testFolder2.getOwners(), testFolder2.getId(),
                testTFolder1);

        Folder gotFolder = mapper.convertTFolder(testTFolder2);

        assertThat(gotFolder).isEqualTo(testFolder2);
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

    private TFolder createTFolder(String folderName, Set<String> owners, long id) {
        TFolder tFolder = new TFolder();
        tFolder.setFolderName(folderName);
        tFolder.setOwners(owners);
        tFolder.setId(id);
        return tFolder;
    }

    private TFolder createTFolder(String folderName, Set<String> owners, long id, TFolder tParent)  {
        TFolder tFolder = createTFolder(folderName, owners, id);
        tFolder.setFolderParentId(tParent.getId());
        if (!tParent.isSetChildrenFolders()) {
            tParent.setChildrenFolders(new HashSet<>());
        }
        tParent.getChildrenFolders().add(tFolder);
        return tFolder;
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
