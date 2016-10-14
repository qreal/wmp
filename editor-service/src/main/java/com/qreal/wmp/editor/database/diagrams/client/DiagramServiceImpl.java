package com.qreal.wmp.editor.database.diagrams.client;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.thrift.gen.DiagramDbService;
import com.qreal.wmp.thrift.gen.TDiagram;
import com.qreal.wmp.thrift.gen.TFolder;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.jetbrains.annotations.NotNull;
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

    /** Connects to a Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client DiagramService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new DiagramDbService.Client(protocol);
    }

    @Override
    public Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException, ErrorConnectionException,
            TException {
        logger.trace("saveDiagram() was called with parameters: diagram = {}, folderId = {}.", diagram.getName(),
                folderId);
        TDiagram tDiagram = diagram.toTDiagram();
        tDiagram.setFolderId(folderId);
        Long result = null;
        transport.open();
        try {
            result = client.saveDiagram(tDiagram);
        } finally {
            transport.close();
        }
        logger.trace("saveDiagram() successfully saved diagram with id {}.", diagram.getId());
        return result;
    }

    @Override
    public @NotNull Diagram openDiagram(Long diagramId) throws NotFoundException, ErrorConnectionException, TException {
        logger.trace("openDiagram() was called with parameters: diagramId = {}.", diagramId);
        TDiagram tDiagram = null;
        transport.open();
        try {
            tDiagram = client.openDiagram(diagramId);
        } finally {
            transport.close();
        }
        logger.trace("openDiagram() successfully returned a diagram.");
        return new Diagram(tDiagram);
    }

    @Override
    public void rewriteDiagram(@NotNull Diagram diagram) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("rewriteDiagram() was called with parameters: diagram = {}", diagram.getName());
        transport.open();
        try {
            client.rewriteDiagram(diagram.toTDiagram());
        } finally {
            transport.close();
        }
        logger.trace("rewriteDiagram() successfully edited a diagram.");
    }

    @Override
    public void deleteDiagram(Long diagramId) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("deleteDiagram() was called with parameters: diagramId = {}.", diagramId);
        transport.open();
        try {
            client.deleteDiagram(diagramId);
        } finally {
            transport.close();
        }
        logger.trace("deleteDiagram() successfully deleted diagram with id {}", diagramId);
    }

    @Override
    public void createRootFolder(String userName) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("createRootFolder() was called with parameters: username = {}.", userName);
        Folder rootFolder = new Folder("root", userName);
        transport.open();
        try {
            TFolder newFolder = rootFolder.toTFolder();
            client.createFolder(newFolder);
        } finally {
            transport.close();
        }
        logger.trace("createRootFolder() successfully created root folder for {}.", userName);
    }

    @Override
    public Long createFolder(@NotNull Folder folder) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("createFolder() was called with parameters: folder = {}.", folder.getFolderName());
        Long result = 0L;
        folder.setUserName(AuthenticatedUser.getUserName());
        transport.open();
        try {
            TFolder newFolder = folder.toTFolder();
            result = client.createFolder(newFolder);
        } finally {
            transport.close();
        }
        logger.trace("createFolder() successfully created folder {}", folder.getFolderName());
        return result;
    }

    @Override
    public void deleteFolder(Long folderId) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("deleteFolder() was called with parameters: folderId = {}.", folderId);
        transport.open();
        try {
            client.deleteFolder(folderId);
        } finally {
            transport.close();
        }
        logger.trace("deleteFolder() successfully deleted folder with id {}.", folderId);
    }

    @Override
    @NotNull
    public Folder getFolderTree() throws NotFoundException, ErrorConnectionException, TException {
        logger.trace("getFolderTree() was called with parameters: userName = {}.",
                AuthenticatedUser.getUserName());
        TFolder folder = new TFolder();
        transport.open();
        try {
            folder = client.getFolderTree(AuthenticatedUser.getUserName());
        } finally {
            transport.close();
        }
        logger.trace("getFolderTree() successfully returned a folderTree.");
        return new Folder(folder);
    }
}
