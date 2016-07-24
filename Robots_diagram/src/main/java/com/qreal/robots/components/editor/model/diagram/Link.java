package com.qreal.robots.components.editor.model.diagram;

import com.qreal.robots.thrift.gen.TLink;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "links")
public class Link implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "logical_id")
    private String logicalId;

    @Column(name = "graphical_id")
    private String graphicalId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "link_id", referencedColumnName = "id")
    private Set<LinkProperty> properties;

    public Link() {
    }

    public Link(TLink tLink) {
        if (tLink.isSetGraphicalId()) {
            this.graphicalId = tLink.getGraphicalId();
        }

        if (tLink.isSetLogicalId()) {
            this.logicalId = tLink.getLogicalId();
        }

        if (tLink.isSetProperties()) {
            this.properties = tLink.getProperties().stream().map(LinkProperty::new).collect(Collectors.toSet());
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

    public Set<LinkProperty> getProperties() {
        return properties;
    }

    public void setProperties(Set<LinkProperty> properties) {
        this.properties = properties;
    }

    public TLink toTLink() {
        TLink tLink = new TLink();

        if (this.logicalId != null) {
            tLink.setLogicalId(this.logicalId);
        }

        if (this.graphicalId != null) {
            tLink.setGraphicalId(this.graphicalId);
        }

        if (this.properties != null) {
            tLink.setProperties(properties.stream().map(LinkProperty::toTProperty).collect(Collectors.toSet()));
        }
        return tLink;
    }
}
