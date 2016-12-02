package com.qreal.wmp.db.palette.model;

import com.qreal.wmp.thrift.gen.TNode;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Palette's node.*/
@Entity
@Table(name = "nodes")
@Data
public class Node implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "node_id", referencedColumnName = "id")
    private Set<NodeProperty> properties = new HashSet<>();

    public Node() {
    }

    /** Constructor-converter from Thrift TDefaultDiagramNode to Node.*/
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

    /** Converter from Node to Thrift TDefaultDiagramNode.*/
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
            tNode.setProperties(properties.stream().map(NodeProperty::toTProperty).
                    collect(Collectors.toSet()));
        }
        return tNode;
    }
}
