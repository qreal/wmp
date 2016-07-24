package com.qreal.robots.components.editor.model.diagram;


import com.qreal.robots.thrift.gen.TDefaultDiagramNode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

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

    public DefaultDiagramNode(TDefaultDiagramNode tDefaultDiagramNode) {
        if (tDefaultDiagramNode.isSetLogicalId()) {
            this.logicalId = tDefaultDiagramNode.getLogicalId();
        }

        if (tDefaultDiagramNode.isSetGraphicalId()) {
            this.graphicalId = tDefaultDiagramNode.getGraphicalId();
        }

        if (tDefaultDiagramNode.isSetType()) {
            this.type = tDefaultDiagramNode.getType();
        }

        if (tDefaultDiagramNode.isSetProperties()) {
            this.properties = tDefaultDiagramNode.getProperties().stream().map(NodeProperty::new).collect(Collectors.toSet());
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

    TDefaultDiagramNode toTDefaultDiagramNode() {
        TDefaultDiagramNode tDefaultDiagramNode = new TDefaultDiagramNode();

        if (this.logicalId != null) {
            tDefaultDiagramNode.setLogicalId(this.logicalId);
        }

        if (this.graphicalId != null) {
            tDefaultDiagramNode.setGraphicalId(this.graphicalId);
        }

        if (this.type != null) {
            tDefaultDiagramNode.setType(this.type);
        }

        if (this.properties != null) {
            tDefaultDiagramNode.setProperties(this.properties.stream().map(NodeProperty::toTProperty).collect(Collectors.toSet()));
        }
        return tDefaultDiagramNode;
    }
}
