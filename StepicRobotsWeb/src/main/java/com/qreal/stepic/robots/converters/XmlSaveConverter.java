/*
 * Copyright Vladimir Zakharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.stepic.robots.converters;

import com.qreal.stepic.robots.model.diagram.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by vladimir-zakharov on 25.04.15.
 */
public class XmlSaveConverter {

    private DocumentBuilder builder;

    public XmlSaveConverter() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        builder = factory.newDocumentBuilder();
    }

    public Diagram convertToJavaModel(File folder) throws Exception {
        Map<String, ElementGraphicalPart> graphicalPartsMap = new HashMap<>();
        Map<String, ElementLogicalPart> logicalPartsMap = new HashMap<>();

        File graphicalFolder = new File(folder.getPath() + "/graphical");
        graphicalPartsMap.putAll(convertGraphicalModel(graphicalFolder));

        File logicalFolder = new File(folder.getPath() + "/logical");
        logicalPartsMap.putAll(convertLogicalModel(logicalFolder));

        File robotDiagramNodeFolder = new File(folder.getPath() +
                "/graphical/RobotsMetamodel/RobotsDiagram/RobotsDiagramNode");
        File robotDiagramNodeFile = robotDiagramNodeFolder.listFiles()[0];
        Set<String> robotDiagramNodeChildGraphicalIds = getNodeChildIds(robotDiagramNodeFile);

        graphicalPartsMap = graphicalPartsMap.entrySet()
                .stream()
                .filter(entry -> robotDiagramNodeChildGraphicalIds.contains(entry.getValue().getId()))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        ElementGraphicalPart robotDiagramNodeGraphicalPart = convertRobotDiagramNodeGraphicalPart(robotDiagramNodeFile);
        graphicalPartsMap.put(robotDiagramNodeGraphicalPart.getLogicalId(), robotDiagramNodeGraphicalPart);

        logicalPartsMap.keySet().retainAll(graphicalPartsMap.keySet());

        Diagram diagram = buildDiagramFromMaps(graphicalPartsMap, logicalPartsMap);

        Set<DiagramElement> additionalNodes = new HashSet<>();

        File subprogramDiagramsFolder = new File(folder.getPath() +
                "/logical/RobotsMetamodel/RobotsDiagram/SubprogramDiagram");
        if (subprogramDiagramsFolder.exists()) {
            Map<String, ElementLogicalPart> subprogramDiagramLogicalPartsMap =
                    convertSubprogramDiagramLogicalParts(subprogramDiagramsFolder);
            subprogramDiagramLogicalPartsMap.entrySet().forEach(entry ->
                    additionalNodes.add(buildElementFromLogicalPart(entry.getValue())));
        }

        diagram.addNodes(additionalNodes);

        return diagram;
    }

    private ElementGraphicalPart convertRobotDiagramNodeGraphicalPart(File robotDiagramNodeFile)
            throws SAXException, IOException {
        Document doc = builder.parse(robotDiagramNodeFile);
        Element element = doc.getDocumentElement();
        return convertGraphicalPart(element);
    }

    private Map<String, ElementLogicalPart> convertSubprogramDiagramLogicalParts(File subprogramDiagramsFolder)
            throws SAXException, IOException {
        Map<String, ElementLogicalPart> logicalPartsMap = new HashMap<>();
        for (final File subprogramDiagramFile : subprogramDiagramsFolder.listFiles()) {
            Document doc = builder.parse(subprogramDiagramFile);
            Element element = doc.getDocumentElement();
            ElementLogicalPart logicalPart = convertLogicalPart(element);
            logicalPartsMap.put(logicalPart.getId(), logicalPart);
        }
        return logicalPartsMap;
    }

    private Map<String, ElementGraphicalPart> convertGraphicalModel(final File folder)
            throws SAXException, IOException {
        Map<String, ElementGraphicalPart> graphicalPartsMap = new HashMap<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                graphicalPartsMap.putAll(convertGraphicalModel(fileEntry));
            } else {
                Document doc = builder.parse(fileEntry);
                Element element = doc.getDocumentElement();

                if (element.hasAttribute("logicalId") && element.getAttribute("logicalId") != "qrm:/") {
                    ElementGraphicalPart graphicalPart = convertGraphicalPart(element);
                    graphicalPartsMap.put(graphicalPart.getLogicalId(), graphicalPart);
                }
            }
        }
        return graphicalPartsMap;
    }

    private Map<String, ElementLogicalPart> convertLogicalModel(final File folder) throws SAXException, IOException {
        Map<String, ElementLogicalPart> logicalPartsMap = new HashMap<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                logicalPartsMap.putAll(convertLogicalModel(fileEntry));
            } else {
                Document doc = builder.parse(fileEntry);
                Element element = doc.getDocumentElement();
                ElementLogicalPart logicalPart = convertLogicalPart(element);
                logicalPartsMap.put(logicalPart.getId(), logicalPart);
            }
        }
        return logicalPartsMap;
    }

    private Set<String> getNodeChildIds(final File node) throws SAXException, IOException {
        Set<String> children = new HashSet<>();
        Document doc = builder.parse(node);
        Element childrenElement = (Element) doc.getElementsByTagName("children").item(0);
        NodeList childList = childrenElement.getElementsByTagName("object");

        for (int i = 0; i < childList.getLength(); i++) {
            Element child = (Element) childList.item(i);
            String idParts[] = child.getAttribute("id").split("/");
            children.add(removeBraces(idParts[idParts.length - 1]));
        }
        return children;
    }

    private ElementLogicalPart convertLogicalPart(Element element) {
        String logicalIdAttr = element.getAttribute("id");
        String parts[] = logicalIdAttr.split("/");

        String logicalId = removeBraces(parts[parts.length - 1]);
        String type = parts[parts.length - 2];


        Element propertiesElement = (Element) element.getElementsByTagName("properties").item(0);
        Set<Property> properties = convertProperties(propertiesElement);
        return new ElementLogicalPart(logicalId, type, properties);
    }

    private ElementGraphicalPart convertGraphicalPart(Element element) {
        String logicalIdAttr = element.getAttribute("logicalId");
        String logicalIdParts[] = logicalIdAttr.split("/");

        String logicalId = removeBraces(logicalIdParts[logicalIdParts.length - 1]);
        String type = logicalIdParts[logicalIdParts.length - 2];

        String graphicalIdAttr = element.getAttribute("id");
        String graphicalIdParts[] = graphicalIdAttr.split("/");

        String graphicalId = removeBraces(graphicalIdParts[graphicalIdParts.length - 1]);

        Element propertiesElement = (Element) element.getElementsByTagName("properties").item(0);
        Set<Property> properties = convertProperties(propertiesElement);
        return new ElementGraphicalPart(graphicalId, logicalId, type, properties);
    }

    private Set<Property> convertProperties(Element propertiesElement) {
        Set<Property> properties = new HashSet<Property>();
        NodeList propertiesList = propertiesElement.getChildNodes();
        for (int i = 0; i < propertiesList.getLength(); i++) {
            Node node = propertiesList.item(i);
            if (node instanceof Element) {
                Element propertyElement = (Element) node;
                if (propertyElement.hasAttribute("key")) {
                    properties.add(new Property(propertyElement.getAttribute("key"),
                            propertyElement.getAttribute("value"), propertyElement.getTagName()));
                }
            }
        }
        return properties;
    }

    private Diagram buildDiagramFromMaps(Map<String, ElementGraphicalPart> graphicalPartsMap,
                                         Map<String, ElementLogicalPart> logicalPartsMap) {
        Set<DiagramElement> nodes = new HashSet<>();
        Set<DiagramElement> links = new HashSet<>();
        graphicalPartsMap.forEach((key, graphicalPart) -> {
            DiagramElement diagramElement;
            if (logicalPartsMap.containsKey(key)) {
                ElementLogicalPart logicalPart = logicalPartsMap.get(key);
                diagramElement = new DiagramElement(logicalPart.getId(), graphicalPart.getId(),
                        logicalPart.getType(), logicalPart.getProperties(), graphicalPart.getProperties());
                if (logicalPart.getType().equals("ControlFlow")) {
                    links.add(diagramElement);
                } else {
                    nodes.add(diagramElement);
                }
            }
        });
        return new Diagram(nodes, links);
    }

    private DiagramElement buildElementFromLogicalPart(ElementLogicalPart logicalPart) {
       return new DiagramElement(logicalPart.getId(), logicalPart.getType(),
                logicalPart.getProperties());
    }

    private String removeBraces(String idString) {
        String pattern = "\\{(.*)\\}";
        return idString.replaceAll(pattern, "$1");
    }
}
