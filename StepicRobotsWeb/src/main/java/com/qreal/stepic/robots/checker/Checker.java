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

package com.qreal.stepic.robots.checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.stepic.robots.constants.PathConstants;
import com.qreal.stepic.robots.exceptions.NotExistsException;
import com.qreal.stepic.robots.exceptions.SubmitException;
import com.qreal.stepic.robots.model.checker.SolutionInfo;
import com.qreal.stepic.robots.model.diagram.Report;
import com.qreal.stepic.robots.model.diagram.ReportMessage;
import com.qreal.stepic.robots.model.diagram.SubmitResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.MessageSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by vladimir-zakharov on 24.08.15.
 */
public class Checker {

    public SubmitResponse submit(SolutionInfo solutionInfo, MessageSource messageSource, Locale locale)
            throws SubmitException {
        try {
            File solutionFolder = new File(String.format("%s/trikKit%s/tasks/%s/solutions/%s",
                    PathConstants.STEPIC_PATH, solutionInfo.getKit(), solutionInfo.getTaskId(),
                    solutionInfo.getUuidStr()));
            prepareSolutionFolder(solutionFolder, solutionInfo);
            runCheckerProcess(solutionInfo.getFilename(), solutionFolder, locale);
            return handleCheckingResult(solutionInfo, messageSource, locale);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SubmitException(messageSource.getMessage("label.checkingError", null, locale));
        }
    }

    public String getWorldModelFromMetainfo(String pathToMetaInfo) throws NotExistsException,
            IOException, ParserConfigurationException, SAXException {

        File metainfo = new File(pathToMetaInfo);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document metainfoXML = dBuilder.parse(metainfo);

        NodeList infos = metainfoXML.getElementsByTagName("info");
        for (int i = 0; i < infos.getLength(); i++) {
            Element info = (Element) infos.item(i);
            if (info.getAttribute("key").equals("worldModel")) {
                return StringEscapeUtils.unescapeXml(info.getAttribute("value"));
            }
        }
        throw new NotExistsException("There is no attribute key with value worldModel in the metainfo");
    }

    private Report parseReportFile(File file, MessageSource messageSource, Locale locale) throws SubmitException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<ReportMessage> messages = mapper.readValue(file, List.class);
            return new Report(messages);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SubmitException(messageSource.getMessage("label.reportError", null, locale));
        }
    }

    private void prepareSolutionFolder(File solutionFolder, SolutionInfo solutionInfo)
            throws IOException {
        File taskFields = new File(String.format("%s/trikKit%s/tasks/%s/fields",
                PathConstants.STEPIC_PATH, solutionInfo.getKit(), solutionInfo.getTaskId()));
        File solutionFields = new File(String.format("%s/fields/%s",
                solutionFolder.getPath(), solutionInfo.getFilenameWithoutExtension()));
        FileUtils.copyDirectory(taskFields, solutionFields);
    }

    private void runCheckerProcess(String filename, File solutionFolder, Locale locale)
            throws IOException, InterruptedException {
        ProcessBuilder interpreterProcBuilder = new ProcessBuilder(PathConstants.CHECKER_PATH, filename);
        Map<String, String> environment = interpreterProcBuilder.environment();

        if (locale.equals(new Locale("en", ""))) {
            environment.put("LANG", "en_US.utf8");
        } else {
            environment.put("LANG", "ru_RU.utf8");
        }
        interpreterProcBuilder.directory(solutionFolder);

        final Process process = interpreterProcBuilder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(isr);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();
    }

    private SubmitResponse handleCheckingResult(SolutionInfo solutionInfo, MessageSource messageSource, Locale locale)
            throws Exception {
        String filename = solutionInfo.getFilename();
        String nameWithoutExtension = filename.substring(0, filename.length() - 4);
        String kit = solutionInfo.getKit();
        String taskId = solutionInfo.getTaskId();
        String uuidStr = solutionInfo.getUuidStr();
        String trajectoryPath;
        Report report;
        String fieldXML;
        File failedField = new File(String.format("%s/trikKit%s/tasks/%s/solutions/%s/failed-field",
                PathConstants.STEPIC_PATH, kit, taskId, uuidStr));
        if (failedField.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(failedField));
            String pathToFailedField = br.readLine();
            fieldXML = new String(Files.readAllBytes(Paths.get(pathToFailedField)), StandardCharsets.UTF_8);
            String[] pathParts = pathToFailedField.split("/");
            String failedFilename = pathParts[pathParts.length - 1];
            String failedName = failedFilename.substring(0, failedFilename.length() - 4);
            trajectoryPath = String.format("%s/trikKit%s/tasks/%s/solutions/%s/trajectories/%s/%s",
                    PathConstants.STEPIC_PATH, kit, taskId, uuidStr, nameWithoutExtension, failedName);
            report = parseReportFile(new File(String.format("%s/trikKit%s/tasks/%s/solutions/%s/reports/%s/%s",
                    PathConstants.STEPIC_PATH, kit, taskId, uuidStr, nameWithoutExtension, failedName)),
                    messageSource, locale);
        } else {
            String pathToMetainfo = String.format("%s/trikKit%s/tasks/%s/%s/metaInfo.xml",
                    PathConstants.STEPIC_PATH, kit, taskId, taskId);

            fieldXML = getWorldModelFromMetainfo(pathToMetainfo);
            trajectoryPath = String.format("%s/trikKit%s/tasks/%s/solutions/%s/trajectory",
                    PathConstants.STEPIC_PATH, kit, taskId, uuidStr);

            report = parseReportFile(new File(String.format("%s/trikKit%s/tasks/%s/solutions/%s/report",
                    PathConstants.STEPIC_PATH, kit, taskId, uuidStr)), messageSource, locale);
        }

        String trace = new String(Files.readAllBytes(Paths.get(trajectoryPath)));

        return new SubmitResponse(messageSource.getMessage("label.successUpload", null, locale),
                report, trace, fieldXML);
    }

}
