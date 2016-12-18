package com.qreal.wmp.db.diagram.model;

import com.qreal.wmp.thrift.gen.TLink;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Link between nodes.*/
@Entity
@Table(name = "links")
@Data
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

    @Column(name = "type")
    private String type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "link_id", referencedColumnName = "id")
    private Set<Property> properties = new HashSet<>();

    public Link() {
    }

    /** Constructor-converter from Thrift TLink to Link.*/
    public Link(TLink tLink) {

        if (tLink.isSetId()) {
            id = tLink.getId();
        }

        if (tLink.isSetGraphicalId()) {
            graphicalId = tLink.getGraphicalId();
        }

        if (tLink.isSetLogicalId()) {
            logicalId = tLink.getLogicalId();
        }

        if (tLink.isSetType()) {
            type = tLink.getType();
        }

        if (tLink.isSetProperties()) {
            properties = tLink.getProperties().stream().map(Property::new).collect(Collectors.toSet());
        }
    }

    /** Converter from Link to Thrift TLink.*/
    public TLink toTLink() {
        TLink tLink = new TLink();

        if (id != null) {
            tLink.setId(id);
        }

        if (logicalId != null) {
            tLink.setLogicalId(logicalId);
        }

        if (graphicalId != null) {
            tLink.setGraphicalId(graphicalId);
        }

        if (type != null) {
            tLink.setType(type);
        }

        if (properties != null && !properties.isEmpty()) {
            tLink.setProperties(properties.stream().map(Property::toTProperty).collect(Collectors.toSet()));
        }
        return tLink;
    }
}
