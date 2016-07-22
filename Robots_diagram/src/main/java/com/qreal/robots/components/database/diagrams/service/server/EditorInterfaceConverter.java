package com.qreal.robots.components.database.diagrams.service.server;

import com.qreal.robots.components.database.diagrams.thrift.gen.*;
import com.qreal.robots.components.editor.model.diagram.*;

import java.util.*;

//TODO move to Diagram when it is possible

public class EditorInterfaceConverter {
    public static Diagram convertFromTDiagram(TDiagram diagram) {

        Diagram newDiagram = new Diagram();
        newDiagram.setName(diagram.getName());
        if (diagram.isSetDiagramId()) {
            newDiagram.setDiagramId(diagram.getDiagramId());
        }

        Set<DefaultDiagramNode> nodes = new HashSet<DefaultDiagramNode>();
        if (diagram.getNodes() != null) {
            Iterator<TDefaultDiagramNode> itr = diagram.getNodes().iterator();
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
        newDiagram.setNodes(nodes);

        Set<Link> links = new HashSet<Link>();
        if (diagram.getLinks() != null) {
            Iterator<TLink> itrL = diagram.getLinks().iterator();
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
        newDiagram.setLinks(links);

        return newDiagram;
    }

    public static TDiagram convertToTDiagram(Diagram diagram) {

        TDiagram newDiagram = new TDiagram();
        if (diagram.getDiagramId() != null) {
            newDiagram.setDiagramId(diagram.getDiagramId());
        }
        newDiagram.setName(diagram.getName());

        Set<TDefaultDiagramNode> nodes = new HashSet<TDefaultDiagramNode>();
        if (diagram.getNodes() != null) {
            Iterator<DefaultDiagramNode> itr = diagram.getNodes().iterator();
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

        Iterator<Link> itrL = diagram.getLinks().iterator();
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

    public static TFolder convertToTFolder(Folder node) {


        TFolder folder = new TFolder();
        if (node.getFolderId() != null) {
            folder.setFolderId(node.getFolderId());
        }
        folder.setFolderName(node.getFolderName());
        if (node.getFolderParentId() != null) {
            folder.setFolderParentId(node.getFolderParentId());
        }
        if (node.getUserName() != null) {
            folder.setUserName(node.getUserName());
        }

        Set<TDiagram> diagrams = new HashSet<TDiagram>();
        if (node.getDiagrams() != null) {
            Iterator<Diagram> itrD = node.getDiagrams().iterator();
            while (itrD.hasNext()) {
                Diagram diagram = itrD.next();
                diagrams.add(convertToTDiagram(diagram));
            }
        }
        Set<TFolder> children = new HashSet<TFolder>();
        if (node.getChildrenFolders() != null) {
            Iterator<Folder> itrF = node.getChildrenFolders().iterator();
            while (itrF.hasNext()) {
                Folder child = itrF.next();
                children.add(convertToTFolder(child));
            }
        }
        folder.setDiagrams(diagrams);
        folder.setChildrenFolders(children);

        return folder;
    }


    public static Folder convertFromTFolder(TFolder node) {
        Folder folder = new Folder();
        if (node.isSetFolderId()) {
            folder.setFolderId(node.getFolderId());
        }
        if (node.isSetFolderName()) {
            folder.setFolderName(node.getFolderName());
        }
        if (node.isSetFolderParentId()) {
            folder.setFolderParentId(node.getFolderParentId());
        }
        if (node.isSetUserName()) {
            folder.setUserName(node.getUserName());
        }
        List<Diagram> diagrams = new ArrayList<>();
        if (node.getDiagrams() != null) {
            Iterator<TDiagram> itrD = node.getDiagrams().iterator();
            while (itrD.hasNext()) {
                TDiagram diagram = itrD.next();
                diagrams.add(convertFromTDiagram(diagram));
            }
        }

        List<Folder> children = new ArrayList<Folder>();
        if (node.getChildrenFolders() != null) {
            Iterator<TFolder> itrF = node.getChildrenFolders().iterator();
            while (itrF.hasNext()) {
                TFolder child = itrF.next();
                children.add(convertFromTFolder(child));
            }
        }

        folder.setDiagrams(diagrams);
        folder.setChildrenFolders(children);

        return folder;
    }
}