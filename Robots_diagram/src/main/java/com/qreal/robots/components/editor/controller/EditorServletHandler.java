package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.database.diagrams.service.client.DiagramService;
import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.DiagramRequest;
import com.qreal.robots.components.editor.model.diagram.Folder;
import com.qreal.robots.components.editor.thrift.gen.EditorServiceThrift;
import com.qreal.robots.components.editor.thrift.gen.TDiagram;
import com.qreal.robots.components.editor.thrift.gen.TFolder;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.ArrayList;
import java.util.List;

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
        Diagram newDiagram = converter.convertFromTDiagram(diagram);
        DiagramRequest request = new DiagramRequest();
        request.setDiagram(newDiagram);
        request.setFolderId(diagram.getFolderId());
        return diagramService.saveDiagram(request);
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
        Folder newFolder = new Folder();
        newFolder.setFolderName(folder.getFolderName());
        newFolder.setFolderParentId(folder.getFolderParentId());
        List<Folder> children = new ArrayList<Folder>();
        newFolder.setChildrenFolders(children);
        List<Diagram> diagrams = new ArrayList<Diagram>();
        newFolder.setDiagrams(diagrams);

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
        return converter.convertFolderTree(diagramService.getFolderTree());
    }
}