package com.qreal.robots.components.editor.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.qreal.robots.common.loaders.TypesLoader;
import com.qreal.robots.common.utils.AuthenticatedUser;
import com.qreal.robots.components.authorization.controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class EditorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private TypesLoader typesLoader;

    public EditorController() {
        this.typesLoader = new TypesLoader();
    }

    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    public ModelAndView index() {
        logger.info("User {} requested editor", AuthenticatedUser.getUserName());
        return new ModelAndView("editor/editor");
    }

    @ResponseBody
    @RequestMapping(value = "getTypes/", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value="kit") String kit) throws IOException {
        return typesLoader.getTypesJson();
    }
}