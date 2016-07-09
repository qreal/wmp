package com.qreal.robots.components.editor.model.diagram;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

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

}
