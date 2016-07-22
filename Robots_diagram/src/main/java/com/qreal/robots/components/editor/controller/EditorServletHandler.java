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

    private EditorInterfaceConverter converter;

    public EditorServletHandler(AbstractApplicationContext context) {
        this.converter = new EditorInterfaceConverter();
        this.context = context;
    }

    @Override
    public long saveDiagram(TDiagram diagram) throws org.apache.thrift.TException {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        return diagramService.saveDiagram(converter.convertFromTDiagram(diagram), diagram.getFolderId());
    }

    @Override
    public void rewriteDiagram(TDiagram diagram) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        Diagram newDiagram = converter.convertFromTDiagram(diagram);
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
        return converter.convertToTDiagram(diagramService.openDiagram(diagramId));
    }

    @Override
    public long createFolder(TFolder folder) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        Folder newFolder = converter.convertFromTFolder(folder);
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
        return converter.convertToTFolder(diagramService.getFolderTree());
    }

}