package com.qreal.wmp.editor.database.diagrams.client;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.thrift.gen.DiagramDbService;
import com.qreal.wmp.thrift.gen.TDiagram;
import com.qreal.wmp.thrift.gen.TFolder;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/** Thrift client side of DiagramDbService.*/
@Service("diagramService")
@PropertySource("classpath:client.properties")
public class DiagramServiceImpl implements DiagramService {

    private static final Logger logger = LoggerFactory.getLogger(DiagramServiceImpl.class);

    private TTransport transport;

    private DiagramDbService.Client client;

    @Value("${port.db.diagram}")
    private int port;

    @Value("${path.db.diagram}")
    private String url;

    /** Constructor creates connection with Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client DiagramService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new DiagramDbService.Client(protocol);
    }

    @Override
    public Long saveDiagram(Diagram diagram, Long folderId) {
        logger.trace("saveDiagram method was called with parameters: diagram = {}, folderId = {}", diagram.getName(),
                folderId);
        TDiagram tDiagram = diagram.toTDiagram();
        tDiagram.setFolderId(folderId);
        Long result = null;
        try {
            transport.open();
            result = client.saveDiagram(tDiagram);
            transport.close();
            logger.trace("saveDiagram method saved diagram with id {}", diagram.getId());
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending saveDiagram request with" +
                    " parameters: diagram = {}, folderId = {}", diagram.getName(), folderId, e);
        }
        return result;
    }

    @Override
    public Diagram openDiagram(Long diagramId) {
        logger.trace("openDiagram method was called with parameters: diagramId = {}", diagramId);
        TDiagram tDiagram = null;
        try {
            transport.open();
            tDiagram = client.openDiagram(diagramId);
            transport.close();
            logger.trace("openDiagram method returned diagram");
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending openDiagram request with " +
                    "parameters: diagramId = {}", diagramId, e);
        }
        return new Diagram(tDiagram);
    }

    @Override
    public void rewriteDiagram(Diagram diagram) {
        logger.trace("rewriteDiagram method called with parameters: diagram = {}", diagram.getName());
        try {
            transport.open();
            client.rewriteDiagram(diagram.toTDiagram());
            transport.close();
            logger.trace("rewriteDiagram method edited diagram");
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending rewriteDiagram request with " +
                    "parameters: diagram = {}", diagram.getName(), e);
        }
    }

    @Override
    public void deleteDiagram(Long diagramId) {
        logger.trace("deleteDiagram method called with parameters: diagramId = {}", diagramId);
        try {
            transport.open();
            client.deleteDiagram(diagramId);
            transport.close();
            logger.trace("deleteDiagram method deleted diagram with id {}", diagramId);
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending deleteDiagram request with " +
                    "parameters: diagramId = {}", diagramId, e);
        }
    }

    @Override
    public void createRootFolder(String userName) {
        logger.trace("createRootFolder method called with parameters: username = {}", userName);
        Folder rootFolder = new Folder("root", userName);
        try {
            transport.open();
            TFolder newFolder = rootFolder.toTFolder();
            client.createFolder(newFolder);
            transport.close();
            logger.trace("createRootFolder method created rootFolder for {}", userName);
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending createFolder request with " +
                    "parameters: newFolder = {}", rootFolder.getFolderName(), e);
        }
    }

    @Override
    public Long createFolder(Folder folder) {
        logger.trace("createFolder method called with parameters: folder = {}", folder.getFolderName());
        Long result = 0L;
        folder.setUserName(AuthenticatedUser.getUserName());
        try {
            transport.open();
            TFolder newFolder = folder.toTFolder();
            result = client.createFolder(newFolder);
            transport.close();
            logger.trace("createFolder method created folder  {}", folder.getFolderName());
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending createFolder request with " +
                    "parameters: folder = {}", folder.getFolderName(), e);
        }
        return result;
    }

    @Override
    public void deleteFolder(Long folderId) {
        logger.trace("deleteFolder method called with parameters: folderId = {}", folderId);
        try {
            transport.open();
            client.deleteFolder(folderId);
            transport.close();
            logger.trace("deleteFolder method deleted folder with id {}", folderId);
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending deleteFolder request with " +
                    "parameters: folderId = {}", folderId, e);
        }
    }

    @Override
    public Folder getFolderTree() {
        logger.trace("getFolderTree method called with parametrs: userName = {}",
                AuthenticatedUser.getUserName());
        TFolder folder = new TFolder();
        try {
            transport.open();
            folder = client.getFolderTree(AuthenticatedUser.getUserName());
            transport.close();
            logger.trace("getFolderTree method returned folderTree");
        } catch (TException e) {
            logger.error("Client DiagramService encountered problem while sending getFolderTree request with " +
                    "parameters: username = {}", AuthenticatedUser.getUserName(), e);
        }
        return new Folder(folder);
    }
}
