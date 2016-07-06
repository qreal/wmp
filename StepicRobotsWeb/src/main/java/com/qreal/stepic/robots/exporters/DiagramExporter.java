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

package com.qreal.stepic.robots.exporters;

import com.qreal.stepic.robots.constants.PathConstants;
import com.qreal.stepic.robots.converters.JavaModelConverter;
import com.qreal.stepic.robots.model.diagram.Diagram;
import com.qreal.stepic.robots.model.xml.DiagramElementXml;
import com.qreal.stepic.robots.model.xml.DiagramXml;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by vladimir-zakharov on 24.03.16.
 */
public class DiagramExporter {

    Transformer transformer;

    public UUID exportDiagram(Diagram diagram, String kit, String taskId) throws Exception {
        JavaModelConverter converter = new JavaModelConverter();
        DiagramXml diagramXml = converter.convertToXml(diagram);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        String directoryPath = String.format("%s/trikKit%s/tasks/%s", PathConstants.STEPIC_PATH, kit, taskId);
        String targetPath = String.format("%s/solutions/%s/%s", directoryPath,
                String.valueOf(diagramXml.getUuid()), taskId);
        File targetDirectory = new File(targetPath);
        targetDirectory.mkdirs();

        new File(String.format("%s/%s", targetPath, PathConstants.PATH_TO_GRAPHICAL_PART)).mkdirs();
        new File(String.format("%s/%s", targetPath, PathConstants.PATH_TO_LOGICAL_PART)).mkdirs();
        new File(String.format("%s/%s", targetPath, PathConstants.PATH_TO_ROOT_ID)).mkdirs();

        copyDefaultFiles(taskId, directoryPath, targetPath);
        exportDiagramXml(diagramXml, targetPath);
        exportRootId(diagramXml.getRootIdXml(), targetPath);
        return diagramXml.getUuid();
    }

    private void exportDiagramXml(DiagramXml diagramXml, String targetPath) throws Exception {
        for (DiagramElementXml elementXml : diagramXml.getDiagramElements()) {
            exportDiagramElement(elementXml, targetPath);
        }
    }

    private void exportDiagramElement(DiagramElementXml elementXml, String targetPath) throws Exception {
        exportElementLogicalPart(elementXml, String.format("%s/%s/%s", targetPath,
                PathConstants.PATH_TO_LOGICAL_PART, elementXml.getType()));
        exportElementGraphicalPart(elementXml, String.format("%s/%s/%s", targetPath,
                PathConstants.PATH_TO_GRAPHICAL_PART, elementXml.getType()));
    }

    private void exportElementLogicalPart(DiagramElementXml elementXml, String targetPath) throws Exception {
        File xmlTargetDirectory = new File(targetPath);
        xmlTargetDirectory.mkdir();

        File targetFile = new File(String.format("%s/{%s}", targetPath, elementXml.getLogicalId()));
        targetFile.createNewFile();

        transformXmlToFile(elementXml.getLogicalPart(), targetFile);
    }

    private void exportElementGraphicalPart(DiagramElementXml elementXml, String targetPath) throws Exception {
        File xmlTargetDirectory = new File(targetPath);
        xmlTargetDirectory.mkdir();

        File targetFile = new File(String.format("%s/{%s}", targetPath, elementXml.getGraphicalId()));
        targetFile.createNewFile();

        transformXmlToFile(elementXml.getGraphicalPart(), targetFile);
    }

    private void exportRootId(Document rootId, String targetPath) throws Exception {
        File rootIdFile = new File(String.format("%s/%s/ROOT_ID",targetPath, PathConstants.PATH_TO_ROOT_ID));
        rootIdFile.createNewFile();

        transformXmlToFile(rootId, rootIdFile);
    }

    private void copyDefaultFiles(String taskId, String directoryPath, String targetPath) throws Exception {
        File taskMetaInfo = new File(String.format("%s/%s/metaInfo.xml", directoryPath, taskId));
        File targetMetaInfo = new File(String.format("%s/metaInfo.xml", targetPath));
        targetMetaInfo.createNewFile();
        FileUtils.copyFile(taskMetaInfo, targetMetaInfo);

        File taskLogicalSubprogramDiagrams = new File(String.format("%s/%s/%s/SubprogramDiagram", directoryPath,
                taskId, PathConstants.PATH_TO_LOGICAL_PART));
        File taskGraphicalSubprogramDiagrams = new File(String.format("%s/%s/%s/SubprogramDiagram", directoryPath,
                taskId, PathConstants.PATH_TO_GRAPHICAL_PART));
        if (taskLogicalSubprogramDiagrams.exists() && taskGraphicalSubprogramDiagrams.exists()) {
            File targetLogicalSubprogramDiagrams = new File(String.format("%s/%s/SubprogramDiagram", targetPath,
                    PathConstants.PATH_TO_LOGICAL_PART));
            FileUtils.copyDirectory(taskLogicalSubprogramDiagrams, targetLogicalSubprogramDiagrams);

            File targetGraphicalSubprogramDiagrams = new File(String.format("%s/%s/SubprogramDiagram", targetPath,
                    PathConstants.PATH_TO_GRAPHICAL_PART));
            FileUtils.copyDirectory(taskGraphicalSubprogramDiagrams, targetGraphicalSubprogramDiagrams);

            copySubprogramNodes(taskId, directoryPath, targetPath);
        }
    }

    private void copySubprogramNodes(String taskId, String directoryPath, String targetPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        File subprogramDiagramsFolder = new File(String.format("%s/%s/%s/SubprogramDiagram", directoryPath, taskId,
                PathConstants.PATH_TO_GRAPHICAL_PART));
        File[] subprogramDiagrams = subprogramDiagramsFolder.listFiles();

        for (File subprogram : subprogramDiagrams) {
            Set<String> childPaths = getNodeChildPaths(subprogram, builder);
            for (String childPath : childPaths) {
                File taskGraphicalPartNode = new File(String.format("%s/%s/%s/%s", directoryPath, taskId,
                        PathConstants.PATH_TO_GRAPHICAL_PART, childPath));
                File targetGraphicalPartNode = new File(String.format("%s/%s/%s", targetPath,
                        PathConstants.PATH_TO_GRAPHICAL_PART, childPath));
                FileUtils.copyFile(taskGraphicalPartNode, targetGraphicalPartNode);

                String logicalPartPath = getLogicalPartPath(taskGraphicalPartNode, builder);

                File taskLogicalPartNode = new File(String.format("%s/%s/%s/%s", directoryPath, taskId,
                        PathConstants.PATH_TO_LOGICAL_PART, logicalPartPath));
                File targetLogicalPartNode = new File(String.format("%s/%s/%s", targetPath,
                        PathConstants.PATH_TO_LOGICAL_PART, logicalPartPath));
                FileUtils.copyFile(taskLogicalPartNode, targetLogicalPartNode);
            }
        }
    }

    private Set<String> getNodeChildPaths(final File node, final DocumentBuilder builder) throws Exception {
        Set<String> childPaths = new HashSet<>();
        Document doc = builder.parse(node);
        Element children = (Element) doc.getElementsByTagName("children").item(0);
        NodeList childList = children.getElementsByTagName("object");

        for (int i = 0; i < childList.getLength(); i++) {
            Element child = (Element) childList.item(i);
            String idAttr = child.getAttribute("id");
            String[] parts = idAttr.split("/");
            String path = parts[parts.length - 2] + "/" + parts[parts.length - 1];
            childPaths.add(path);
        }

        return childPaths;
    }

    private String getLogicalPartPath(final File graphicalPartNode, final DocumentBuilder builder) throws Exception {
        Document doc = builder.parse(graphicalPartNode);
        Element element = doc.getDocumentElement();
        String logicalIdAttr = element.getAttribute("logicalId");
        String parts[] = logicalIdAttr.split("/");
        return parts[parts.length - 2] + "/" + parts[parts.length - 1];
    }

    private void transformXmlToFile(Document xml, File file) throws Exception {
        DOMSource source = new DOMSource(xml);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

}
