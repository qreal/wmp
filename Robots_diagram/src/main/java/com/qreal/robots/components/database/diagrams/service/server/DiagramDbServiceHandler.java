package com.qreal.robots.components.database.diagrams.service.server;

import com.qreal.robots.components.database.diagrams.thrift.gen.DiagramDbService;
import com.qreal.robots.components.database.diagrams.thrift.gen.TDiagramDAO;
import com.qreal.robots.components.database.diagrams.thrift.gen.TFolderDAO;
import com.qreal.robots.components.editor.controller.EditorInterfaceConverter;
import org.apache.thrift.TException;
import org.springframework.context.support.AbstractApplicationContext;

public class DiagramDbServiceHandler implements DiagramDbService.Iface {

    private AbstractApplicationContext context;
    private EditorInterfaceConverter converter;

    public DiagramDbServiceHandler(AbstractApplicationContext context) {
        this.context = context;
        converter = new EditorInterfaceConverter();
    }

    @Override
    public long saveDiagram(TDiagramDAO diagram) throws TException {
        return 0;
    }

    @Override
    public TDiagramDAO openDiagram(long diagramID) throws TException {
        return null;
    }

    @Override
    public void deleteDiagram(long diagramId) throws TException {

    }

    @Override
    public void rewriteDiagram(TDiagramDAO diagram) throws TException {

    }

    @Override
    public long createFolder(TFolderDAO folder) throws TException {
        return 0;
    }

    @Override
    public void deleteFolder(long folderId) throws TException {

    }

    @Override
    public TFolderDAO getFolderTree() throws TException {
        return null;
    }
}
