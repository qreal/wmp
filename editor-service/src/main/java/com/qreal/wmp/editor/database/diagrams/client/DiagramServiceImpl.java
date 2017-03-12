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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

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
    @Transactional
    synchronized public Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException,
            ErrorConnectionException,
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
    @Transactional
    synchronized public @NotNull Diagram getDiagram(Long diagramId) throws NotFoundException,
            ErrorConnectionException, TException {
        logger.trace("getDiagram() was called with parameters: diagramId = {}.", diagramId);
        TDiagram tDiagram = null;
        transport.open();
        try {
            tDiagram = client.getDiagram(diagramId);
        } finally {
            transport.close();
        }
        logger.trace("getDiagram() successfully returned a diagram.");
        return new Diagram(tDiagram);
    }

    @Override
    @Transactional
    synchronized public void updateDiagram(@NotNull Diagram diagram) throws AbortedException,
            ErrorConnectionException, TException {
        logger.trace("updateDiagram() was called with parameters: diagram = {}", diagram.getName());
        transport.open();
        try {
            client.updateDiagram(diagram.toTDiagram());
        } finally {
            transport.close();
        }
        logger.trace("updateDiagram() successfully edited a diagram.");
    }

    @Override
    @Transactional
    synchronized public void deleteDiagram(Long diagramId) throws AbortedException, ErrorConnectionException,
            TException {
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
    @Transactional
    synchronized public Long saveFolder(@NotNull Folder folder) throws AbortedException, ErrorConnectionException,
            TException {
        logger.trace("saveFolder() was called with parameters: folder = {}.", folder.getFolderName());
        Long result = 0L;

        Set<String> owner = new HashSet<>();
        owner.add(AuthenticatedUser.getUserName());
        folder.setOwners(owner);

        transport.open();
        try {
            TFolder newFolder = folder.toTFolder();
            result = client.saveFolder(newFolder);
        } finally {
            transport.close();
        }
        logger.trace("saveFolder() successfully created folder {}", folder.getFolderName());
        return result;
    }

    @Override
    @Transactional
    synchronized public Folder getFolder(Long folderId, String username) throws NotFoundException,
            ErrorConnectionException,
            TException {
        logger.trace("getFolder() was called with parameters: folderId = {}.", folderId);
        transport.open();
        TFolder tFolder;
        try {
            tFolder = client.getFolder(folderId, username);
        } finally {
            transport.close();
        }
        logger.trace("getFolder() successfully returned folder with id {}.", folderId);
        return new Folder(tFolder);
    }

    @Override
    @Transactional
    synchronized public void updateFolder(@NotNull Folder folder) throws AbortedException, ErrorConnectionException,
            TException {
        logger.trace("updateFolder() was called with parameters: folder = {}", folder.getFolderName());
        transport.open();
        try {
            client.updateFolder(folder.toTFolder());
        } finally {
            transport.close();
        }
        logger.trace("updateFolder() successfully updated a folder.");
    }

    @Override
    @Transactional
    synchronized public void deleteFolder(Long folderId) throws AbortedException, ErrorConnectionException, TException {
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
    @Transactional
    synchronized public void createRootFolder(String userName) throws AbortedException, ErrorConnectionException,
            TException {
        logger.trace("createRootFolder() was called with parameters: username = {}.", userName);
        Folder rootFolder = new Folder("root", userName);
        transport.open();
        try {
            TFolder newFolder = rootFolder.toTFolder();
            client.saveFolder(newFolder);
        } finally {
            transport.close();
        }
        logger.trace("createRootFolder() successfully created root folder for {}.", userName);
    }

    @Override
    @NotNull
    @Transactional
    synchronized public Folder getFolderTree(String username) throws NotFoundException, ErrorConnectionException,
            TException {
        logger.trace("getFolderTree() was called with parameters: owners = {}.",
                AuthenticatedUser.getUserName());
        TFolder folder = new TFolder();
        transport.open();
        try {
            folder = client.getFolderTree(username);
        } finally {
            transport.close();
        }
        logger.trace("getFolderTree() successfully returned a folderTree.");
        return new Folder(folder);
    }

    @Override
    @NotNull
    @Transactional
    synchronized public void shareFolderTo(String username, Folder folder) throws TException {
        logger.trace("shareFolderTo() was called with parameters: username = {}.", username);
        TFolder tFolder = folder.toTFolder();
        transport.open();
        try {
            client.shareFolderTo(username, tFolder);
        } finally {
            transport.close();
        }
        logger.trace("shareFolderTo() successfully shared folder.");
    }
}
