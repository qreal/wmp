package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.database.diagrams.service.client.DiagramService;
import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;
import com.qreal.robots.thrift.gen.EditorServiceThrift;
import com.qreal.robots.thrift.gen.TDiagram;
import com.qreal.robots.thrift.gen.TFolder;
import org.springframework.context.support.AbstractApplicationContext;

public class EditorServletHandler implements EditorServiceThrift.Iface {

    private AbstractApplicationContext context;


    public EditorServletHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public long saveDiagram(TDiagram tDiagram) throws org.apache.thrift.TException {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Diagram diagram = new Diagram(tDiagram);
        return diagramService.saveDiagram(diagram, tDiagram.getFolderId());
    }

    @Override
    public void rewriteDiagram(TDiagram diagram) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Diagram newDiagram = new Diagram(diagram);
        diagramService.rewriteDiagram(newDiagram);
    }

    @Override
    public void deleteDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        diagramService.deleteDiagram(diagramId);
    }

    @Override
    public TDiagram openDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        return diagramService.openDiagram(diagramId).toTDiagram();
    }

    @Override
    public long createFolder(TFolder folder) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        Folder newFolder = new Folder(folder);
        return diagramService.createFolder(newFolder);
    }

    @Override
    public void deleteFolder(long folderId) {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        diagramService.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree() {
        DiagramService diagramService = (DiagramService) context.getBean("diagramService");
        return diagramService.getFolderTree().toTFolder();
    }

}