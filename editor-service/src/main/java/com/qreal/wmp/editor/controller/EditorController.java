package com.qreal.wmp.editor.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.qreal.wmp.editor.common.loaders.TypesLoader;
import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Main controller of Editor service.
 * Pages: editor
 */
@Controller
public class EditorController {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    private final TypesLoader typesLoader;

    @Autowired
    public EditorController(TypesLoader typesLoader) {
        this.typesLoader = typesLoader;
    }

    @RequestMapping(value = "/robots", method = RequestMethod.GET)
    public ModelAndView robotsIndex() {
        logger.info("User {} requested robots editor", AuthenticatedUser.getUserName());
        ModelAndView modelAndView = new ModelAndView("editor/robots/editor");
        modelAndView.getModel().put("selectors", getConfig("/editor/robots"));
        return modelAndView;
    }

    @RequestMapping(value = "/bpmn", method = RequestMethod.GET)
    public ModelAndView bpmnIndex() {
        logger.info("User {} requested bpmn editor", AuthenticatedUser.getUserName());
        ModelAndView modelAndView = new ModelAndView("editor/bpmn/editor");
        modelAndView.getModel().put("selectors", getConfig("/editor/bpmn"));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/getTypes", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value = "kit") String kit,
                             @RequestParam(value = "task") String task) throws IOException {
        return typesLoader.getTypesJson(task);
    }
    
    private static String getConfig(String page) {
        return new RestTemplate().getForObject("http://localhost:8081/selectors" + page, String.class);
    }
}
