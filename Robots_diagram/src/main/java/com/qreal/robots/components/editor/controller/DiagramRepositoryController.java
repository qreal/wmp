package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.database.diagrams.service.DiagramService;
import com.qreal.robots.components.editor.model.diagram.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class DiagramRepositoryController {

@Autowired
private DiagramService diagramService;

    @ResponseBody
    @RequestMapping(value = "/getFolderTree", method = RequestMethod.GET)
    public Folder getFolderTree() {
        return diagramService.getFolderTree();
    }
}