package com.qreal.robots.components.database.diagrams.service.client;

import com.qreal.robots.components.database.diagrams.DAO.DiagramDAO;
import com.qreal.robots.components.database.diagrams.service.server.EditorInterfaceConverter;
import com.qreal.robots.components.database.diagrams.thrift.gen.DiagramDbService;
import com.qreal.robots.components.database.diagrams.thrift.gen.TDiagram;
import com.qreal.robots.components.database.diagrams.thrift.gen.TFolder;
import com.qreal.robots.components.editor.model.diagram.Diagram;
import com.qreal.robots.components.editor.model.diagram.Folder;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("DiagramService")
public class DiagramServiceImpl implements DiagramService {

    private EditorInterfaceConverter converter;

    private TTransport transport;


    private DiagramDbService.Client client;

    public DiagramServiceImpl() {
        transport = new TSocket("localhost", 9093);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new DiagramDbService.Client(protocol);
        converter = new EditorInterfaceConverter();
    }

    @Override
    public Long saveDiagram(Diagram diagram, Long folderId) {
        TDiagram tDiagram = converter.convertToTDiagram(diagram);
        tDiagram.setFolderId(folderId);
        Long result = null;
        try {
            transport.open();
            result = client.saveDiagram(tDiagram);
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Diagram openDiagram(Long diagramId) {
        TDiagram tDiagram = null;
        try {
            transport.open();
            tDiagram = client.openDiagram(diagramId);
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
        return converter.convertFromTDiagram(tDiagram);
    }

    @Override
    public void rewriteDiagram(Diagram diagram) {
        try {
            transport.open();
            client.rewriteDiagram(converter.convertToTDiagram(diagram));
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDiagram(Long diagramId) {
        try {
            transport.open();
            client.deleteDiagram(diagramId);
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRootFolder(String userName) {
        Folder rootFolder = new Folder("root", userName);
        try {
            transport.open();
            TFolder newFolder = converter.convertToTFolder(rootFolder);
            client.createFolder(newFolder);
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long createFolder(Folder folder) {
        Long result = new Long(0);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        folder.setUserName(userName);
        try {
            transport.open();
            TFolder newFolder = converter.convertToTFolder(folder);
            result = client.createFolder(newFolder);
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteFolder(Long folderId) {
        try {
            transport.open();
            client.deleteFolder(folderId);
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Folder getFolderTree() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        TFolder folder = new TFolder();
        try {
            transport.open();
            folder = client.getFolderTree(userName);
            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
        Folder newFolder = converter.convertFromTFolder(folder);
        return newFolder;
    }
}