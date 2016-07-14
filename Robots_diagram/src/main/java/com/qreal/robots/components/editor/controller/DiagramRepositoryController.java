package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;
import com.qreal.robots.components.editor.model.diagram.OpenRequest;
import com.qreal.robots.components.database.diagrams.service.DiagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
public class DiagramRepositoryController {

    @Autowired
    private DiagramService diagramService;

    @ResponseBody
    @RequestMapping(value = "/openDiagram", method = RequestMethod.POST)
    public Diagram openDiagram(@RequestBody OpenRequest request) {
        return diagramService.openDiagram(request.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/updateDiagram", method = RequestMethod.POST)
    public void rewriteDiagram(@RequestBody Diagram diagram) {
        diagramService.rewriteDiagram(diagram);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteDiagram", method = RequestMethod.POST)
    public void deleteDiagram(@RequestBody OpenRequest request) {
        diagramService.deleteDiagram(request.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/createFolder", method = RequestMethod.POST)
    public Long createFolder(@RequestBody Folder folder) {
        return diagramService.createFolder(folder);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST)
    public void deleteFolder(@RequestBody OpenRequest request) {
        diagramService.deleteFolder(request.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/getFolderTree", method = RequestMethod.GET)
    public Folder getFolderTree() {
        return diagramService.getFolderTree();
    }
}