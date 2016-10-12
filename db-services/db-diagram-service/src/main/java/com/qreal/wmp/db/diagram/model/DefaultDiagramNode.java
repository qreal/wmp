package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TDefaultDiagramNode;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Diagram's node.*/
@Entity
@Table(name = "nodes")
@Data
public class DefaultDiagramNode implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "logical_id")
    private String logicalId;

    @Column(name = "graphical_id")
    private String graphicalId;

    @Column(name = "type")
    private String type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "node_id", referencedColumnName = "id")
    private Set<Property> properties = new HashSet<>();

    public DefaultDiagramNode() {
    }

    /** Constructor-converter from Thrift TDefaultDiagramNode to DefaultDiagramNode.*/
    public DefaultDiagramNode(TDefaultDiagramNode tDefaultDiagramNode) {
        if (tDefaultDiagramNode.isSetLogicalId()) {
            logicalId = tDefaultDiagramNode.getLogicalId();
        }

        if (tDefaultDiagramNode.isSetGraphicalId()) {
            graphicalId = tDefaultDiagramNode.getGraphicalId();
        }

        if (tDefaultDiagramNode.isSetType()) {
            type = tDefaultDiagramNode.getType();
        }

        if (tDefaultDiagramNode.isSetProperties()) {
            properties = tDefaultDiagramNode.getProperties().stream().map(Property::new).
                    collect(Collectors.toSet());
        }
    }

    /** Converter from DefaultDiagramNode to Thrift TDefaultDiagramNode.*/
    public TDefaultDiagramNode toTDefaultDiagramNode() {
        TDefaultDiagramNode tDefaultDiagramNode = new TDefaultDiagramNode();

        if (logicalId != null) {
            tDefaultDiagramNode.setLogicalId(logicalId);
        }

        if (graphicalId != null) {
            tDefaultDiagramNode.setGraphicalId(graphicalId);
        }

        if (type != null) {
            tDefaultDiagramNode.setType(type);
        }

        if (properties != null && !properties.isEmpty()) {
            tDefaultDiagramNode.setProperties(properties.stream().map(Property::toTProperty).
                    collect(Collectors.toSet()));
        }
        return tDefaultDiagramNode;
    }
}
