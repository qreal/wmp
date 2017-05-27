package com.qreal.wmp.generator.model;

import com.qreal.wmp.thrift.gen.TNode;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Palette's node.*/
@Data
public class Node implements Serializable {
    private String id;

    private String name;

    private String image;

    private Set<NodeProperty> properties = new HashSet<>();

    public Node() {
    }

    /** Constructor-converter from Thrift TNode to Node.*/
    public Node(TNode tNode) {
        if (tNode.isSetNodeId()) {
            id = tNode.getNodeId();
        }

        if (tNode.isSetName()) {
            name = tNode.getName();
        }

        if (tNode.isSetImage()) {
            image = tNode.getImage();
        }

        if (tNode.isSetProperties()) {
            properties = tNode.getProperties().stream().map(NodeProperty::new).
                    collect(Collectors.toSet());
        }
    }

    /** Converter from Node to Thrift TNode.*/
    public TNode toTNode() {
        TNode tNode = new TNode();

        if (id != null) {
            tNode.setNodeId(id);
        }

        if (name != null) {
            tNode.setName(name);
        }

        if (image != null) {
            tNode.setImage(image);
        }

        if (properties != null && !properties.isEmpty()) {
            tNode.setProperties(properties.stream().map(NodeProperty::toTNodeProperty).
                    collect(Collectors.toSet()));
        }
        return tNode;
    }
}
