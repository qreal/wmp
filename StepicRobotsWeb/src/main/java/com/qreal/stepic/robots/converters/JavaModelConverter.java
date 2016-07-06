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

import com.qreal.stepic.robots.model.diagram.Diagram;
import com.qreal.stepic.robots.model.diagram.DiagramElement;
import com.qreal.stepic.robots.model.xml.DiagramElementXml;
import com.qreal.stepic.robots.model.xml.DiagramXml;
import com.qreal.stepic.robots.model.xml.builders.GraphicalPartXmlBuilder;
import com.qreal.stepic.robots.model.xml.builders.LogicalPartXmlBuilder;
import com.qreal.stepic.robots.model.xml.builders.RootIdXmlBuilder;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vladimir-zakharov on 25.05.15.
 */
public class JavaModelConverter {

    private DocumentBuilder documentBuilder;
    private Set<String> elementsIds = new HashSet<>();

    public JavaModelConverter() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = documentFactory.newDocumentBuilder();
    }

    public DiagramXml convertToXml(Diagram diagram) throws Exception {
        List<DiagramElementXml> diagramElements = new ArrayList<>();

        for (DiagramElement node : diagram.getNodes()) {
            diagramElements.add(convertElement(node));
        }

        for (DiagramElement link : diagram.getLinks()) {
            diagramElements.add(convertElement(link));
        }

        Document rootIdXml = RootIdXmlBuilder.buildXml(elementsIds);
        return new DiagramXml(rootIdXml, diagramElements);
    }

    private DiagramElementXml convertElement(DiagramElement element) throws Exception {
        Document logicalPart = LogicalPartXmlBuilder.buildXml(element);
        Document graphicalPart = GraphicalPartXmlBuilder.buildXml(element);

        this.elementsIds.add(String.format("qrm:/RobotsMetamodel/RobotsDiagram/%s/{%s}",
                element.getType(), element.getLogicalId()));
        if (element.getType().equals("RobotsDiagramNode")) {
            this.elementsIds.add(String.format("qrm:/RobotsMetamodel/RobotsDiagram/%s/{%s}",
                    element.getType(), element.getGraphicalId()));
        }
        return new DiagramElementXml(element.getLogicalId(), element.getGraphicalId(),
                element.getType(), logicalPart, graphicalPart);
    }

}
