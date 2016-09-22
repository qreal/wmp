package com.qreal.wmp.db.diagram.test.dao;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.Aborted;
import com.qreal.wmp.db.diagram.exceptions.NotFound;
import com.qreal.wmp.db.diagram.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests saving of all models in hibernate.
 * Error here witnesses about error in hibernate model.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoDiagramComplexTest {
    @Autowired
    public DiagramDao diagramDao;

    /** Test saving diagram with properties and links. */
    @Test
    @Rollback
    public void saveDiagram_diagramWithAllPossibleItems_gotDiagram() throws Aborted, NotFound {
        Folder testFolder = new Folder("testFolder", "testUser");
        long idFolderCreated = diagramDao.saveFolder(testFolder);

        Diagram testDiagram = new Diagram();
        testDiagram.setName("testDiagram");

        DefaultDiagramNode node = new DefaultDiagramNode();
        node.setGraphicalId("graphicalId");
        node.setLogicalId("logicalId");
        node.setType("type");

        Property propertyNode = new Property();
        propertyNode.setName("namePropertyNode");
        propertyNode.setValue("valuePropertyNode");
        propertyNode.setType("typePropertyNode");
        node.getProperties().add(propertyNode);

        testDiagram.getNodes().add(node);

        Link link = new Link();
        link.setGraphicalId("graphicalId");
        link.setLogicalId("logicalId");

        Property propertyLink = new Property();
        propertyLink.setName("namePropertyLink");
        propertyLink.setValue("valuePropertyLink");
        propertyLink.setType("typePropertyLink");
        link.getProperties().add(propertyLink);

        testDiagram.getLinks().add(link);

        long idDiagramCreated = diagramDao.saveDiagram(testDiagram, idFolderCreated);

        Diagram gotDiagram = diagramDao.getDiagram(idDiagramCreated);

        assertThat(gotDiagram).isEqualTo(testDiagram);
    }
}
