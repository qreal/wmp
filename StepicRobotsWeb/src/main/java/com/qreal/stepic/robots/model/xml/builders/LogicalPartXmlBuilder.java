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

package com.qreal.stepic.robots.model.xml.builders;

import com.qreal.stepic.robots.model.diagram.DiagramElement;
import com.qreal.stepic.robots.model.diagram.IdObject;
import com.qreal.stepic.robots.model.diagram.Property;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Set;

/**
 * Created by vladimir-zakharov on 24.03.16.
 */
public class LogicalPartXmlBuilder {

    private static final String template = "" +
            "<object id=\"qrm:/RobotsMetamodel/RobotsDiagram/%s/{%s}\" " +
            "parent=\"qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID\">" +
            "  <children/>" +
            "  <properties>" +
            "    %s" +
            "    <incomingExplosions type=\"qReal::IdList\">" +
            "      %s" +
            "    </incomingExplosions>" +
            "    <links type=\"qReal::IdList\">" +
            "      %s" +
            "    </links>" +
            "  </properties>" +
            "</object>";

    public static Document buildXml(DiagramElement element) throws Exception {
        StringBuilder propertiesStringBuilder = new StringBuilder();
        Set<Property> properties = element.getLogicalProperties();
        properties.forEach(property -> propertiesStringBuilder.append(
                PropertyXmlBuilder.buildXmlString(property.getType(), property.getName(), property.getValue())));

        StringBuilder incomingExplosionsBuilder = new StringBuilder();
        Set<IdObject> incomingExplosions = element.getIncomingExplosions();
        incomingExplosions.forEach(idObject -> incomingExplosionsBuilder.append(
                IdObjectXmlBuilder.buildXmlString(idObject.getId(), "Subprogram")));

        StringBuilder linksStringBuilder = new StringBuilder();
        Set<IdObject> linkIdObjects = element.getLogicalLinksIds();
        linkIdObjects.forEach(idObject -> linksStringBuilder.append(
                IdObjectXmlBuilder.buildXmlString(idObject.getId(), "ControlFlow")));

        String xmlString = String.format(template, element.getType(), element.getLogicalId(),
                propertiesStringBuilder.toString(), incomingExplosionsBuilder.toString(),
                linksStringBuilder.toString());

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        return documentBuilder.parse(new InputSource(new StringReader(xmlString)));
    }

}
