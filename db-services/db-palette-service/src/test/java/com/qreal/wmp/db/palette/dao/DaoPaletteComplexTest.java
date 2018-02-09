package com.qreal.wmp.db.palette.dao;

import com.qreal.wmp.db.palette.config.AppInit;
import com.qreal.wmp.db.palette.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoPaletteComplexTest {
    @Autowired
    private PaletteDao paletteDao;

    /** Test createPalette with node. */
    @Test
    @Rollback
    public void createPalette_withNode() throws Exception {
        Palette palette = createPalette("testPalette");

        Node node = createNode("nameNode", "imageNode");
        palette = addNodeToPalette(node, palette);

        long idPaletteCreated = paletteDao.createPalette(palette);

        Palette gotPalette = paletteDao.getPalette(idPaletteCreated);

        assertThat(gotPalette).isEqualTo(palette);
    }

    /** Test createPalette with node, which has property. */
    @Test
    @Rollback
    public void createPalette_withNode_withNodeProperty() throws Exception {
        Palette palette = createPalette("testPalette");

        Node node = createNode("nameNode", "imageNode");
        NodeProperty propertyNode = createProperty("nameNodeProperty", "valueNodeProperty", "typeNodeProperty");
        node = addPropertyToNode(propertyNode, node);
        palette = addNodeToPalette(node, palette);

        long idPaletteCreated = paletteDao.createPalette(palette);

        Palette gotPalette = paletteDao.getPalette(idPaletteCreated);

        assertThat(gotPalette).isEqualTo(palette);
    }

    /** Test createPalette with nodes. */
    @Test
    @Rollback
    public void createPalette_withNodes() throws Exception {
        Palette palette = createPalette("testPalette");

        Node node_1 = createNode("nameNode_!", "imageNode_1");
        NodeProperty propertyNode_1 = createProperty("nameNodeProperty_1", "valueNodeProperty_1", "typeNodeProperty_1");
        NodeProperty propertyNode_2 = createProperty("nameNodeProperty_2", "valueNodeProperty_2", "typeNodeProperty_2");
        node_1 = addPropertyToNode(propertyNode_1, node_1);
        node_1 = addPropertyToNode(propertyNode_2, node_1);

        Node node_2 = createNode("nameNode_2", "imageNode_2");

        Node node_3 = createNode("nameNode_3", "imageNode_3");
        NodeProperty propertyNode_3 = createProperty("nameNodeProperty_3", "valueNodeProperty_3", "typeNodeProperty_3");
        node_3 = addPropertyToNode(propertyNode_3, node_3);

        palette = addNodeToPalette(node_1, palette);
        palette = addNodeToPalette(node_2, palette);
        palette = addNodeToPalette(node_3, palette);

        long idPaletteCreated = paletteDao.createPalette(palette);

        Palette gotPalette = paletteDao.getPalette(idPaletteCreated);

        assertThat(gotPalette).isEqualTo(palette);
    }

    private Palette createPalette(String name) {
        Palette palette = new Palette();
        palette.setPaletteName(name);
        return palette;
    }

    private Node createNode(String name, String image) {
        Node node = new Node();
        node.setName(name);
        node.setImage(image);
        return node;
    }

    private NodeProperty createProperty(String name, String value, String type) {
        NodeProperty nodeProperty = new NodeProperty();
        nodeProperty.setName(name);
        nodeProperty.setValue(value);
        nodeProperty.setType(type);
        return nodeProperty;
    }

    private Node addPropertyToNode(NodeProperty nodeProperty, Node node) {
        node.getProperties().add(nodeProperty);
        return node;
    }

    private Palette addNodeToPalette(Node node, Palette palette) {
        palette.getNodes().add(node);
        return palette;
    }

}

