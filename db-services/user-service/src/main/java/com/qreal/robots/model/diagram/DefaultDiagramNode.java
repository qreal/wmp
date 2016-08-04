package com.qreal.robots.model.diagram;

import com.qreal.robots.thrift.gen.TDefaultDiagramNode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diagram's node.
 */
@Entity
@Table(name = "nodes")
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
    private Set<NodeProperty> properties;

    public DefaultDiagramNode() {
    }

    /**
     * Constructor-converter from Thrift TDefaultDiagramNode to DefaultDiagramNode.
     */
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
            properties = tDefaultDiagramNode.getProperties().stream().map(NodeProperty::new).
                    collect(Collectors.toSet());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogicalId() {
        return logicalId;
    }

    public void setLogicalId(String logicalId) {
        this.logicalId = logicalId;
    }

    public String getGraphicalId() {
        return graphicalId;
    }

    public void setGraphicalId(String graphicalId) {
        this.graphicalId = graphicalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<NodeProperty> getProperties() {
        return properties;
    }

    public void setProperties(Set<NodeProperty> properties) {
        this.properties = properties;
    }

    /**
     * Converter from DefaultDiagramNode to Thrift TDefaultDiagramNode.
     */
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

        if (properties != null) {
            tDefaultDiagramNode.setProperties(properties.stream().map(NodeProperty::toTProperty).
                    collect(Collectors.toSet()));
        }
        return tDefaultDiagramNode;
    }
}
