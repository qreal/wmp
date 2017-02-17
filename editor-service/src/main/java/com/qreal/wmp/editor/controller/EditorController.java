package com.qreal.wmp.editor.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.qreal.wmp.editor.common.loaders.TypesLoader;
import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/{editorType}", method = RequestMethod.GET)
    public ModelAndView index(@PathVariable String editorType) {
        logger.info("User {} requested " + editorType + " editor", AuthenticatedUser.getUserName());
        return new ModelAndView("editor/" + editorType + "/editor");
    }

    @ResponseBody
    @RequestMapping(value = "/getTypes", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value = "kit") String kit,
                             @RequestParam(value = "task") String task) throws IOException {
        return typesLoader.getTypesJson(task);
    }
}