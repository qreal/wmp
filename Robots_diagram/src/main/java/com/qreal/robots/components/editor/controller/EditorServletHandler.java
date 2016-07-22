package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.database.diagrams.service.client.DiagramService;
import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;
import com.qreal.robots.components.editor.thrift.gen.EditorServiceThrift;
import com.qreal.robots.components.editor.thrift.gen.TDiagram;
import com.qreal.robots.components.editor.thrift.gen.TFolder;
import org.springframework.context.support.AbstractApplicationContext;

class EditorServletHandler implements EditorServiceThrift.Iface {

    private AbstractApplicationContext context;


    public EditorServletHandler(AbstractApplicationContext context) {
        this.context = context;
    }

    @Override
    public long saveDiagram(TDiagram diagram) throws org.apache.thrift.TException {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        return diagramService.saveDiagram(new Diagram(diagram), diagram.getFolderId());
    }

    @Override
    public void rewriteDiagram(TDiagram diagram) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        Diagram newDiagram = new Diagram(diagram);
        diagramService.rewriteDiagram(newDiagram);
    }

    @Override
    public void deleteDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        diagramService.deleteDiagram(diagramId);
    }

    @Override
    public TDiagram openDiagram(long diagramId) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        return diagramService.openDiagram(diagramId).toTDiagram();
    }

    @Override
    public long createFolder(TFolder folder) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        Folder newFolder = new Folder(folder);
        return diagramService.createFolder(newFolder);
    }

    @Override
    public void deleteFolder(long folderId) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        diagramService.deleteFolder(folderId);
    }

    @Override
    public TFolder getFolderTree() {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        return diagramService.getFolderTree().toTFolder();
    }

}