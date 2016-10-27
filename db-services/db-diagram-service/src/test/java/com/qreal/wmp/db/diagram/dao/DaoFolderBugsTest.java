package com.qreal.wmp.db.diagram.dao;
        import com.qreal.wmp.db.diagram.config.AppInit;
        import com.qreal.wmp.db.diagram.model.Diagram;
        import com.qreal.wmp.db.diagram.model.Folder;
        import com.qreal.wmp.db.diagram.model.FolderConverter;
        import com.qreal.wmp.thrift.gen.TFolder;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.test.annotation.Rollback;
        import org.springframework.test.context.ContextConfiguration;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.HashSet;

        import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Tests for bugs with no session which were found in code.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoFolderBugsTest {

    @Autowired
    private FolderConverter converter;

    @Autowired
    private DiagramDao diagramDao;

    @Test
    @Rollback
    public void saveTreeOfFolders_saveTree_gotTree() throws Exception {
        Folder testFolder = new Folder("testFolder", "testUser");
        Folder testFolder1 = new Folder("testFolder1", "testUser");
        testFolder.getChildrenFolders().add(testFolder1);


        long idFolderCreated = diagramDao.saveFolder(testFolder);
        testFolder.setId(idFolderCreated);

        Folder gotFolder = diagramDao.getFolder(idFolderCreated);

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    @Test
    @Rollback
    public void updateTreeOfFolders_updateToTree_gotTree() throws Exception {
        Folder rootFolder = new Folder("root", "testUser");

        long idRoot = diagramDao.saveFolder(rootFolder);
        rootFolder.setId(idRoot);

        Folder testFolder = new Folder("testFolder", "testUser");

        long idFolderCreated = diagramDao.saveFolder(testFolder);
        testFolder.setId(idFolderCreated);

        rootFolder.getChildrenFolders().add(testFolder);
        testFolder.getParentFolders().add(rootFolder);

        diagramDao.updateFolder(rootFolder);

        testFolder.getOwners().add("testUser2");

        diagramDao.updateFolder(testFolder);

        Folder gotFolder = diagramDao.getFolder(idFolderCreated);

        assertThat(gotFolder).isEqualTo(testFolder);
    }

    @Test
    @Rollback
    public void toTFolderAndBackTree() throws Exception {
        Folder testFolder = new Folder("testFolder", "testUser");
        Folder testFolder1 = new Folder("testFolder1", "testUser");

        testFolder.getChildrenFolders().add(testFolder1);
        testFolder1.getParentFolders().add(testFolder);

        TFolder tTestFolder = testFolder.toTFolder();

        Folder gotFolder = converter.toFolder(tTestFolder);

        assertThat(testFolder).isEqualTo(gotFolder);
    }

    @Test
    @Rollback
    public void toTFolderAndBackTreeWithParentsAsId() throws Exception {
        TFolder testFolder = new TFolder();
        testFolder.setFolderName("testFolder");

        long idFolderCreated = diagramDao.saveFolder(converter.toFolder(testFolder));
        testFolder.setId(idFolderCreated);

        TFolder testFolder1 = new TFolder();
        testFolder1.setFolderName("testFolder1");
        testFolder1.setParentFolders(new HashSet<>());
        testFolder1.getParentFolders().add(idFolderCreated);

        Folder gotFolder = converter.toFolder(testFolder1);

        Folder shouldBeFolder = converter.toFolder(testFolder);
        TFolder shouldBeTFolder1 = new TFolder();
        shouldBeTFolder1.setFolderName("testFolder1");
        Folder shouldBeFolder1 = converter.toFolder(shouldBeTFolder1);
        shouldBeFolder.getChildrenFolders().add(shouldBeFolder1);
        shouldBeFolder1.getParentFolders().add(shouldBeFolder);

        assertThat(gotFolder).isEqualTo(shouldBeFolder1);

    }
}
