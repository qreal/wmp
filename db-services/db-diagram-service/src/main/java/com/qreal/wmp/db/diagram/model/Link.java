package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TLink;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/** Link between nodes.*/
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
    private Set<Property> properties;

    public Link() {
    }

    /** Constructor-converter from Thrift TLink to Link.*/
    public Link(TLink tLink) {
        if (tLink.isSetGraphicalId()) {
            graphicalId = tLink.getGraphicalId();
        }

        if (tLink.isSetLogicalId()) {
            logicalId = tLink.getLogicalId();
        }

        if (tLink.isSetProperties()) {
            properties = tLink.getProperties().stream().map(Property::new).collect(Collectors.toSet());
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

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    /** Converter from Link to Thrift TLink.*/
    public TLink toTLink() {
        TLink tLink = new TLink();

        if (logicalId != null) {
            tLink.setLogicalId(logicalId);
        }

        if (graphicalId != null) {
            tLink.setGraphicalId(graphicalId);
        }

        if (properties != null) {
            tLink.setProperties(properties.stream().map(Property::toTProperty).collect(Collectors.toSet()));
        }
        return tLink;
    }
}
