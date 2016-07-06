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

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Set;

/**
 * Created by vladimir-zakharov on 25.03.16.
 */
public class RootIdXmlBuilder {

    private static final String template = "" +
            "<?xml version=\"1.0\"?>" +
            "<object parent=\"qrm:/\" id=\"qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID\">" +
            "  <children>" +
            "    %s" +
            "  </children>" +
            "  <properties>" +
            "    <QString key=\"name\" value=\"qrm:/ROOT_ID/ROOT_ID/ROOT_ID/ROOT_ID\"/>" +
            "  </properties>" +
            "</object>";

    public static Document buildXml(Set<String> elementIds) throws Exception {
        StringBuilder childrenStringBuilder = new StringBuilder();
        elementIds.forEach(id -> childrenStringBuilder.append(IdObjectXmlBuilder.buildXmlString(id)));

        String xmlString = String.format(template, childrenStringBuilder.toString());

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        return documentBuilder.parse(new InputSource(new StringReader(xmlString)));
    }


}
