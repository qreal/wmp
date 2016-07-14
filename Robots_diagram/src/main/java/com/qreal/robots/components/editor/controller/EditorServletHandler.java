package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.database.diagrams.service.DiagramService;
import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.DiagramRequest;
import com.qreal.robots.components.editor.thrift.gen.DiagramDAO;
import com.qreal.robots.components.editor.thrift.gen.EditorServiceThrift;
import org.springframework.context.support.AbstractApplicationContext;

class EditorServletHandler implements EditorServiceThrift.Iface {

    private AbstractApplicationContext context;
    private EditorInterfaceConverter converter;

    public EditorServletHandler(AbstractApplicationContext context) {
        this.converter = new EditorInterfaceConverter();
        this.context = context;
    }

    @Override
    public long saveDiagram(DiagramDAO diagram) throws org.apache.thrift.TException {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        Diagram newDiagram = converter.convertDiagram(diagram);
        DiagramRequest request = new DiagramRequest();
        request.setDiagram(newDiagram);
        request.setFolderId(diagram.getFolderId());
        return diagramService.saveDiagram(request);
    }

    @Override
    public void rewriteDiagram(DiagramDAO diagram) {
        DiagramService diagramService = (DiagramService) context.getBean("DiagramService");
        Diagram newDiagram = converter.convertDiagram(diagram);
        diagramService.rewriteDiagram(newDiagram);
    }
}