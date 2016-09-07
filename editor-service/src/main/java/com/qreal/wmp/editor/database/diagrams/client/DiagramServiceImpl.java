package com.qreal.wmp.editor.database.diagrams.client;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.editor.database.exceptions.Aborted;
import com.qreal.wmp.editor.database.exceptions.ErrorConnection;
import com.qreal.wmp.editor.database.exceptions.NotFound;
import com.qreal.wmp.thrift.gen.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
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

    /** Constructor creates connection with Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client DiagramService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new DiagramDbService.Client(protocol);
    }

    @Override
    public Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws Aborted, ErrorConnection {
        logger.trace("saveDiagram method was called with parameters: diagram = {}, folderId = {}", diagram.getName(),
                folderId);
        TDiagram tDiagram = diagram.toTDiagram();
        tDiagram.setFolderId(folderId);
        Long result = null;
        try {
            transport.open();
            try {
                result = client.saveDiagram(tDiagram);
            } catch (TIdAlreadyDefined e) {
                logger.error("saveDiagram method encountered exception IdAlreadyDefined. Diagram was not saved.", e);
            } catch (TAborted e) {
                throw new Aborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending saveDiagram request with" +
                        " parameters: diagram = {}, folderId = {}", diagram.getName(), folderId, e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending saveDiagram request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("saveDiagram method saved diagram with id {}", diagram.getId());
        return result;
    }

    @Override
    @NotNull
    public Diagram openDiagram(Long diagramId) throws NotFound, ErrorConnection {
        logger.trace("openDiagram method was called with parameters: diagramId = {}", diagramId);
        TDiagram tDiagram = null;
        try {
            transport.open();
            try {
                tDiagram = client.openDiagram(diagramId);
            } catch (TNotFound e) {
                throw new NotFound(e.getId(), e.getMessage());
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending openDiagram request with " +
                        "parameters: diagramId = {}", diagramId, e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending openDiagram request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("openDiagram method returned diagram");
        return new Diagram(tDiagram);
    }

    @Override
    public void rewriteDiagram(@NotNull Diagram diagram) throws Aborted, ErrorConnection {
        logger.trace("rewriteDiagram method called with parameters: diagram = {}", diagram.getName());
        try {
            transport.open();
            try {
                client.rewriteDiagram(diagram.toTDiagram());
            } catch (TIdNotDefined e) {
                logger.error("rewriteDiagram method encountered exception IdNotDefined. You've tried to update " +
                        "diagram, but not specified it's id.", e);
            } catch (TAborted e) {
                throw new Aborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending rewriteDiagram request with " +
                        "parameters: diagram = {}", diagram.getName(), e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending rewriteDiagram request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("rewriteDiagram method edited diagram");
    }

    @Override
    public void deleteDiagram(Long diagramId) throws Aborted, ErrorConnection {
        logger.trace("deleteDiagram method called with parameters: diagramId = {}", diagramId);
        try {
            transport.open();
            try {
                client.deleteDiagram(diagramId);
            } catch (TAborted e) {
                throw new Aborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending deleteDiagram request with " +
                        "parameters: diagramId = {}", diagramId, e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending deleteDiagram request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("deleteDiagram method deleted diagram with id {}", diagramId);

    }

    @Override
    public void createRootFolder(String userName) throws Aborted, ErrorConnection {
        logger.trace("createRootFolder method called with parameters: username = {}", userName);
        Folder rootFolder = new Folder("root", userName);
        try {
            transport.open();
            try {
                TFolder newFolder = rootFolder.toTFolder();
                client.createFolder(newFolder);
            } catch (TAborted e) {
                throw new Aborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TIdAlreadyDefined e) {
                logger.error("createRootFolder method encountered exception IdAlreadyDefined. Folder was not created.",
                        e);
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending createFolder request with " +
                        "parameters: newFolder = {}", rootFolder.getFolderName(), e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending createFolder request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("createRootFolder method created rootFolder for {}", userName);
    }

    @Override
    public Long createFolder(@NotNull Folder folder) throws Aborted, ErrorConnection {
        logger.trace("createFolder method called with parameters: folder = {}", folder.getFolderName());
        Long result = 0L;
        folder.setUserName(AuthenticatedUser.getUserName());
        try {
            transport.open();
            try {
                TFolder newFolder = folder.toTFolder();
                result = client.createFolder(newFolder);
            } catch (TAborted e) {
                throw new Aborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TIdAlreadyDefined e) {
                logger.error("createFolder method encountered exception IdAlreadyDefined. Folder was not created.", e);
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending createFolder request with " +
                        "parameters: folder = {}", folder.getFolderName(), e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending createFolder request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("createFolder method created folder  {}", folder.getFolderName());
        return result;
    }

    @Override
    public void deleteFolder(Long folderId) throws Aborted, ErrorConnection {
        logger.trace("deleteFolder method called with parameters: folderId = {}", folderId);
        try {
            transport.open();
            try {
                client.deleteFolder(folderId);
            } catch (TAborted e) {
                throw new Aborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending deleteFolder request with " +
                        "parameters: folderId = {}", folderId, e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending deleteFolder request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("deleteFolder method deleted folder with id {}", folderId);
    }

    @Override
    @NotNull
    public Folder getFolderTree() throws NotFound, ErrorConnection {
        logger.trace("getFolderTree method called with parameters: userName = {}",
                AuthenticatedUser.getUserName());
        TFolder folder = new TFolder();
        try {
            transport.open();
            try {
                folder = client.getFolderTree(AuthenticatedUser.getUserName());
            } catch (TNotFound e) {
                throw new NotFound(e.getId(), e.getMessage());
            } catch (TErrorConnection e) {
                throw new ErrorConnection(e.getNameClient(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered problem while sending getFolderTree request with " +
                        "parameters: username = {}", AuthenticatedUser.getUserName(), e);
                throw new ErrorConnection(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                        "problem while sending getFolderTree request");
            }
            finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnection(DiagramService.class.getName(), "Client DiagramService encountered problem " +
                    "while opening transport.");
        }
        logger.trace("getFolderTree method returned folderTree");
        return new Folder(folder);
    }
}
