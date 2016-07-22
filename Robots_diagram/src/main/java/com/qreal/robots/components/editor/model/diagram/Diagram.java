package com.qreal.robots.components.editor.model.diagram;

import com.qreal.robots.components.editor.thrift.gen.TDiagram;
import com.qreal.robots.components.editor.thrift.gen.TDefaultDiagramNode;
import com.qreal.robots.components.editor.thrift.gen.TLink;
import com.qreal.robots.components.editor.thrift.gen.TProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "diagrams")
public class Diagram implements Serializable {

    @Id
    @Column(name = "diagram_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diagramId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<DefaultDiagramNode> nodes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "diagram_id", referencedColumnName = "diagram_id")
    private Set<Link> links;

    public Diagram() {
    }

    public Diagram(TDiagram tDiagram) {

        this.setName(tDiagram.getName());

        if (tDiagram.isSetDiagramId()) {
            this.setDiagramId(tDiagram.getDiagramId());
        }

        Set<DefaultDiagramNode> nodes = new HashSet<DefaultDiagramNode>();
        if (tDiagram.getNodes() != null) {
            Iterator<TDefaultDiagramNode> itr = tDiagram.getNodes().iterator();
            while (itr.hasNext()) {
                TDefaultDiagramNode node = itr.next();
                DefaultDiagramNode newNode = new DefaultDiagramNode();

                newNode.setLogicalId(node.getLogicalId());
                newNode.setGraphicalId(node.getGraphicalId());
                newNode.setType(node.getType());

                Set<NodeProperty> properties = new HashSet<NodeProperty>();
                if (node.getProperties() != null) {
                    Iterator<TProperty> pItr = node.getProperties().iterator();
                    while (pItr.hasNext()) {
                        TProperty property = pItr.next();
                        NodeProperty newProperty = new NodeProperty();
                        newProperty.setName(property.getName());
                        newProperty.setPropertyId(property.getPropertyId());
                        newProperty.setType(property.getType());
                        newProperty.setValue(property.getValue());
                        properties.add(newProperty);
                    }
                }
                newNode.setProperties(properties);
                nodes.add(newNode);
            }
        }
        this.setNodes(nodes);

        Set<Link> links = new HashSet<Link>();
        if (tDiagram.getLinks() != null) {
            Iterator<TLink> itrL = tDiagram.getLinks().iterator();
            while (itrL.hasNext()) {
                TLink link = itrL.next();
                Link newLink = new Link();

                newLink.setLogicalId(link.getLogicalId());
                newLink.setGraphicalId(link.getGraphicalId());

                Set<LinkProperty> properties = new HashSet<LinkProperty>();
                if (link.getProperties() != null) {
                    Iterator<TProperty> pItr = link.getProperties().iterator();
                    while (pItr.hasNext()) {
                        TProperty property = pItr.next();
                        LinkProperty newProperty = new LinkProperty();
                        newProperty.setName(property.getName());
                        newProperty.setPropertyId(property.getPropertyId());
                        newProperty.setType(property.getType());
                        newProperty.setValue(property.getValue());
                        properties.add(newProperty);
                    }
                }
                newLink.setProperties(properties);
                links.add(newLink);
            }
        }
        this.setLinks(links);
    }

    public TDiagram toTDiagram() {

        TDiagram newDiagram = new TDiagram();
        if (this.getDiagramId() != null) {
            newDiagram.setDiagramId(this.getDiagramId());
        }
        newDiagram.setName(this.getName());

        Set<TDefaultDiagramNode> nodes = new HashSet<TDefaultDiagramNode>();
        if (this.getNodes() != null) {
            Iterator<DefaultDiagramNode> itr = this.getNodes().iterator();
            while (itr.hasNext()) {
                DefaultDiagramNode node = itr.next();
                TDefaultDiagramNode newNode = new TDefaultDiagramNode();

                newNode.setLogicalId(node.getLogicalId());
                newNode.setGraphicalId(node.getGraphicalId());
                newNode.setType(node.getType());

                Set<TProperty> properties = new HashSet<TProperty>();
                if (node.getProperties() != null) {
                    Iterator<NodeProperty> pItr = node.getProperties().iterator();
                    while (pItr.hasNext()) {
                        NodeProperty property = pItr.next();
                        TProperty newProperty = new TProperty();
                        newProperty.setName(property.getName());
                        newProperty.setPropertyId(property.getPropertyId());
                        newProperty.setType(property.getType());
                        newProperty.setValue(property.getValue());
                        properties.add(newProperty);
                    }
                }
                newNode.setProperties(properties);
                nodes.add(newNode);
            }
        }
        newDiagram.setNodes(nodes);

        Iterator<Link> itrL = this.getLinks().iterator();
        Set<TLink> links = new HashSet<TLink>();
        while (itrL.hasNext()) {
            Link link = itrL.next();
            TLink newLink = new TLink();

            newLink.setLogicalId(link.getLogicalId());
            newLink.setGraphicalId(link.getGraphicalId());

            Iterator<LinkProperty> pItr = link.getProperties().iterator();
            Set<TProperty> properties = new HashSet<TProperty>();
            while(pItr.hasNext()) {
                LinkProperty property = pItr.next();
                TProperty newProperty = new TProperty();
                newProperty.setName(property.getName());
                newProperty.setPropertyId(property.getPropertyId());
                newProperty.setType(property.getType());
                newProperty.setValue(property.getValue());
                properties.add(newProperty);
            }
            newLink.setProperties(properties);
            links.add(newLink);
        }
        newDiagram.setLinks(links);

        return newDiagram;
    }

    public Long getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(Long diagramId) {
        this.diagramId = diagramId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNodes(Set<DefaultDiagramNode> nodes) {
        this.nodes = nodes;
    }

    public Set<DefaultDiagramNode> getNodes() {
        return nodes;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Set<Link> getLinks() {
        return links;
    }
}
