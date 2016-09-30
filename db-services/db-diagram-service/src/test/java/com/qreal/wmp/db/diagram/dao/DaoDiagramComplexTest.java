package com.qreal.wmp.db.diagram.dao;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
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
 * Tests for saving models in Hibernate.
 * Error here witnesses about error in Hibernate model.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoDiagramComplexTest {
    @Autowired
    private DiagramDao diagramDao;

    /** Test saving diagram with properties and links. */
    @Test
    @Rollback
    public void saveDiagram_diagramWithAllPossibleItems_gotDiagram() throws Exception {
        long idFolderCreated = createAndSaveFolder("testFolder", "testUser");
        Diagram diagram = createDiagram("testDiagram");

        DefaultDiagramNode node = createDiagramNode("graphicalId", "logicalId", "type");
        Property propertyNode = createProperty("namePropertyNode", "valuePropertyNode", "typePropertyNode");
        node = addPropertyToNode(propertyNode, node);
        diagram = addNodeToDiagram(node, diagram);

        Link link = createLink("graphicalId", "logicalId");
        Property propertyLink = createProperty("namePropertyLink", "valuePropertyLink", "typePropertyLink");
        link = addPropertyToLink(propertyLink, link);
        diagram = addLinkToDiagram(link, diagram);

        long idDiagramCreated = diagramDao.saveDiagram(diagram, idFolderCreated);

        Diagram gotDiagram = diagramDao.getDiagram(idDiagramCreated);

        assertThat(gotDiagram).isEqualTo(diagram);
    }

    private long createAndSaveFolder(String name, String owner) throws Exception {
        Folder testFolder = new Folder(name, owner);
        return diagramDao.saveFolder(testFolder);
    }

    private Diagram createDiagram(String name) {
        Diagram diagram = new Diagram();
        diagram.setName(name);
        return diagram;
    }

    private DefaultDiagramNode createDiagramNode(String graphicalId, String logicalId, String type) {
        DefaultDiagramNode node = new DefaultDiagramNode();
        node.setGraphicalId(graphicalId);
        node.setLogicalId(logicalId);
        node.setType(type);
        return node;
    }

    private Property createProperty(String name, String value, String type) {
        Property property = new Property();
        property.setName(name);
        property.setValue(value);
        property.setType(type);
        return property;
    }

    private DefaultDiagramNode addPropertyToNode(Property property, DefaultDiagramNode node) {
        node.getProperties().add(property);
        return node;
    }

    private Link addPropertyToLink(Property property, Link link) {
        link.getProperties().add(property);
        return link;
    }

    private Diagram addNodeToDiagram(DefaultDiagramNode node, Diagram diagram) {
        diagram.getNodes().add(node);
        return diagram;
    }

    private Diagram addLinkToDiagram(Link link, Diagram diagram) {
        diagram.getLinks().add(link);
        return diagram;
    }

    private Link createLink(String graphicalId, String logicalId) {
        Link link = new Link();
        link.setGraphicalId(graphicalId);
        link.setLogicalId(logicalId);
        return link;
    }
}
