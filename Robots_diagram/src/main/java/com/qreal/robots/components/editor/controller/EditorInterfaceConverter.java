package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.editor.model.diagram.*;
import com.qreal.robots.components.editor.thrift.gen.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EditorInterfaceConverter {
    public Diagram convertDiagramFromDAO(DiagramDAO diagram) {
        if (diagram == null)
            return null;

        Diagram newDiagram = new Diagram();
        newDiagram.setName(diagram.getName());
        if (diagram.getDiagramId() == -1)
            newDiagram.setDiagramId(null);
        else
            newDiagram.setDiagramId(diagram.getDiagramId());

        Iterator<DefaultDiagramNodeDAO> itr = diagram.getNodes().iterator();
        Set<DefaultDiagramNode> nodes = new HashSet<DefaultDiagramNode>();
        while(itr.hasNext()) {
            DefaultDiagramNodeDAO node = itr.next();
            DefaultDiagramNode newNode = new DefaultDiagramNode();

            newNode.setLogicalId(node.getLogicalId());
            newNode.setGraphicalId(node.getGraphicalId());
            newNode.setType(node.getType());

            Iterator<PropertyDAO> pItr = node.getProperties().iterator();
            Set<NodeProperty> properties = new HashSet<NodeProperty>();
            while(pItr.hasNext()) {
                PropertyDAO property = pItr.next();
                NodeProperty newProperty = new NodeProperty();
                newProperty.setName(property.getName());
                newProperty.setPropertyId(property.getPropertyId());
                newProperty.setType(property.getType());
                newProperty.setValue(property.getValue());
                properties.add(newProperty);
            }
            newNode.setProperties(properties);
            nodes.add(newNode);
        }
        newDiagram.setNodes(nodes);

        Iterator<LinkDAO> itrL = diagram.getLinks().iterator();
        Set<Link> links = new HashSet<Link>();
        while (itrL.hasNext()) {
            LinkDAO link = itrL.next();
            Link newLink = new Link();

            newLink.setLogicalId(link.getLogicalId());
            newLink.setGraphicalId(link.getGraphicalId());

            Iterator<PropertyDAO> pItr = link.getProperties().iterator();
            Set<LinkProperty> properties = new HashSet<LinkProperty>();
            while(pItr.hasNext()) {
                PropertyDAO property = pItr.next();
                LinkProperty newProperty = new LinkProperty();
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

    public DiagramDAO convertDiagramToDAO(Diagram diagram) {
        if (diagram == null)
            return null;

        DiagramDAO newDiagram = new DiagramDAO();
        newDiagram.setDiagramId(diagram.getDiagramId());
        newDiagram.setName(diagram.getName());

        Iterator<DefaultDiagramNode> itr = diagram.getNodes().iterator();
        Set<DefaultDiagramNodeDAO> nodes = new HashSet<DefaultDiagramNodeDAO>();
        while(itr.hasNext()) {
            DefaultDiagramNode node = itr.next();
            DefaultDiagramNodeDAO newNode = new DefaultDiagramNodeDAO();

            newNode.setLogicalId(node.getLogicalId());
            newNode.setGraphicalId(node.getGraphicalId());
            newNode.setType(node.getType());

            Iterator<NodeProperty> pItr = node.getProperties().iterator();
            Set<PropertyDAO> properties = new HashSet<PropertyDAO>();
            while(pItr.hasNext()) {
                NodeProperty property = pItr.next();
                PropertyDAO newProperty = new PropertyDAO();
                newProperty.setName(property.getName());
                newProperty.setPropertyId(property.getPropertyId());
                newProperty.setType(property.getType());
                newProperty.setValue(property.getValue());
                properties.add(newProperty);
            }
            newNode.setProperties(properties);
            nodes.add(newNode);
        }
        newDiagram.setNodes(nodes);

        Iterator<Link> itrL = diagram.getLinks().iterator();
        Set<LinkDAO> links = new HashSet<LinkDAO>();
        while (itrL.hasNext()) {
            Link link = itrL.next();
            LinkDAO newLink = new LinkDAO();

            newLink.setLogicalId(link.getLogicalId());
            newLink.setGraphicalId(link.getGraphicalId());

            Iterator<LinkProperty> pItr = link.getProperties().iterator();
            Set<PropertyDAO> properties = new HashSet<PropertyDAO>();
            while(pItr.hasNext()) {
                LinkProperty property = pItr.next();
                PropertyDAO newProperty = new PropertyDAO();
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

    public FolderDAO convertFolderTree(Folder node) {
        FolderDAO folder = new FolderDAO();
        folder.setFolderName(node.getFolderName());
        folder.setFolderId(node.getFolderId());
        folder.setFolderParentId(node.getFolderParentId());

        Set<DiagramDAO> diagrams = new HashSet<DiagramDAO>();
        Iterator<Diagram> itrD = node.getDiagrams().iterator();
        while (itrD.hasNext()) {
            Diagram diagram = itrD.next();
            diagrams.add(convertDiagramToDAO(diagram));
        }

        Set<FolderDAO> children = new HashSet<FolderDAO>();
        Iterator<Folder> itrF = node.getChildrenFolders().iterator();
        while (itrF.hasNext()) {
            Folder child = itrF.next();
            children.add(convertFolderTree(child));
        }

        folder.setDiagrams(diagrams);
        folder.setChildrenFolders(children);

        return folder;
    }
}