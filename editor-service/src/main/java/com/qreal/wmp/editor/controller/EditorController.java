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
        return new ModelAndView("robots/editor");
    }

    @RequestMapping(value = "/dsm", method = RequestMethod.GET)
    public ModelAndView dsmIndex() {
        logger.info("User {} requested robots editor", AuthenticatedUser.getUserName());
        return new ModelAndView("dsm/editor");
    }

    @ResponseBody
    @RequestMapping(value = "/getTypes", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value = "task") String task) throws IOException {
        return typesLoader.getTypesJson(task);
    }
}
