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

package com.qreal.stepic.robots.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qreal.stepic.robots.constants.PathConstants;
import com.qreal.stepic.robots.converters.JavaModelConverter;
import com.qreal.stepic.robots.converters.XmlSaveConverter;
import com.qreal.stepic.robots.exceptions.SubmitException;
import com.qreal.stepic.robots.exporters.DiagramExporter;
import com.qreal.stepic.robots.loaders.TypesLoader;
import com.qreal.stepic.robots.model.checker.Description;
import com.qreal.stepic.robots.model.checker.SolutionInfo;
import com.qreal.stepic.robots.model.diagram.Diagram;
import com.qreal.stepic.robots.model.diagram.OpenResponse;
import com.qreal.stepic.robots.model.diagram.SubmitResponse;
import com.qreal.stepic.robots.translators.PropertyValueTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by vladimir-zakharov on 25.10.14.
 */
@Controller
@RequestMapping("/online")
public class OnlineSolutionController extends SolutionController {

    @Autowired
    private MessageSource messageSource;

    private TypesLoader typesLoader;
    private DiagramExporter diagramExporter;

    public OnlineSolutionController() {
        typesLoader = new TypesLoader();
        diagramExporter = new DiagramExporter();
    }

    @RequestMapping(value = "{id}", params = { "kit", "name", "title" }, method = RequestMethod.GET)
    public ModelAndView showTask(HttpServletRequest request, @PathVariable String id,
                                 @RequestParam(value="kit") String kit,
                                 @RequestParam(value="name") String name,
                                 @RequestParam(value="title") String title, Locale locale)
            throws NoSuchRequestHandlingMethodException, IOException {
        if (getTypes(kit, id, locale) == null) {
            throw new NoSuchRequestHandlingMethodException(request);
        }
        ModelAndView modelAndView = new ModelAndView("checker/onlineSolution");
        modelAndView.addObject("kit", kit);
        modelAndView.addObject("title", title);
        modelAndView.addObject("id", id);
        modelAndView.addObject("name", name);

        Description description;
        description = getDescription(kit, id, locale);
        modelAndView.addObject("description", description);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "getTypes/{id}", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value="kit") String kit, @PathVariable String id, Locale locale)
            throws IOException {
        return typesLoader.getTypesJson(kit, id, locale);
    }

    @ResponseBody
    @RequestMapping(value = "open/{id}", method = RequestMethod.POST)
    public OpenResponse open(@PathVariable String id, @RequestParam(value="kit") String kit) throws Exception {
        XmlSaveConverter converter = new XmlSaveConverter();
        compressor.decompress(kit, id);
        String fieldXML = checker.getWorldModelFromMetainfo(String.format("%s/trikKit%s/tasks/%s/%s/metaInfo.xml",
                PathConstants.STEPIC_PATH, kit, id, id));
        File treeDirectory = new File(String.format("%s/trikKit%s/tasks/%s/%s/tree", PathConstants.STEPIC_PATH,
                kit, id, id));
        Diagram diagram = converter.convertToJavaModel(treeDirectory);
        return new OpenResponse(diagram, fieldXML);
    }

    @ResponseBody
    @RequestMapping(value = "submit/{id}", method = RequestMethod.POST)
    public SubmitResponse submit(@RequestParam(value="kit") String kit,
                                 @RequestParam(value="diagram") String diagramString,
                                 @PathVariable String id, Locale locale) throws SubmitException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Diagram diagram = mapper.readValue(diagramString, Diagram.class);
            PropertyValueTranslator translator = new PropertyValueTranslator();
            translator.translateAllPropertiesValue(diagram.getNodes(), locale);
            translator.translateAllPropertiesValue(diagram.getLinks(), locale);
            String uuidStr = String.valueOf(diagramExporter.exportDiagram(diagram, kit, id));
            compressor.compress(id, String.format("%s/trikKit%s/tasks/%s/solutions/%s", PathConstants.STEPIC_PATH,
                    kit, id, uuidStr));
            return checker.submit(new SolutionInfo(id + ".qrs", kit, id, uuidStr), messageSource, locale);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SubmitException(messageSource.getMessage("label.commonError", null, locale));
        }
    }

}
