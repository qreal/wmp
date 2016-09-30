package com.qreal.wmp.editor.database.diagrams.client;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.diagrams.model.Diagram;
import com.qreal.wmp.editor.database.diagrams.model.Folder;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
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

    /** Connects to a Thrift TServer.*/
    @PostConstruct
    public void start() {
        logger.info("Client DiagramService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new DiagramDbService.Client(protocol);
    }

    @Override
    public Long saveDiagram(@NotNull Diagram diagram, Long folderId) throws AbortedException, ErrorConnectionException {
        logger.trace("saveDiagram() was called with parameters: diagram = {}, folderId = {}.", diagram.getName(),
                folderId);
        TDiagram tDiagram = diagram.toTDiagram();
        tDiagram.setFolderId(folderId);
        Long result = null;
        try {
            transport.open();
            try {
                result = client.saveDiagram(tDiagram);
            } catch (TIdAlreadyDefined e) {
                logger.error("saveDiagram() encountered an IdAlreadyDefined exception. Diagram was not saved.", e);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending saveDiagram request with " +
                        "parameters: diagram = {}, folderId = {}", diagram.getName(), folderId, e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending saveDiagram request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService a encountered " +
                    "problem while opening transport.");
        }
        logger.trace("saveDiagram() successfully saved diagram with id {}.", diagram.getId());
        return result;
    }

    @Override
    public @NotNull Diagram openDiagram(Long diagramId) throws NotFoundException, ErrorConnectionException {
        logger.trace("openDiagram() was called with parameters: diagramId = {}.", diagramId);
        TDiagram tDiagram = null;
        try {
            transport.open();
            try {
                tDiagram = client.openDiagram(diagramId);
            } catch (TNotFound e) {
                throw new NotFoundException(e.getId(), e.getMessage());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending openDiagram request with " +
                        "parameters: diagramId = {}", diagramId, e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending openDiagram request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("openDiagram() successfully returned a diagram.");
        return new Diagram(tDiagram);
    }

    @Override
    public void rewriteDiagram(@NotNull Diagram diagram) throws AbortedException, ErrorConnectionException {
        logger.trace("rewriteDiagram() was called with parameters: diagram = {}", diagram.getName());
        try {
            transport.open();
            try {
                client.rewriteDiagram(diagram.toTDiagram());
            } catch (TIdNotDefined e) {
                logger.error("rewriteDiagram() encountered an IdNotDefined exception. You've tried to update " +
                        "diagram, but did not specified its id.", e);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending rewriteDiagram request with " +
                        "parameters: diagram = {}", diagram.getName(), e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending rewriteDiagram request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("rewriteDiagram() successfully edited a diagram.");
    }

    @Override
    public void deleteDiagram(Long diagramId) throws AbortedException, ErrorConnectionException {
        logger.trace("deleteDiagram() was called with parameters: diagramId = {}.", diagramId);
        try {
            transport.open();
            try {
                client.deleteDiagram(diagramId);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending deleteDiagram request with " +
                        "parameters: diagramId = {}.", diagramId, e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending deleteDiagram request.");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("deleteDiagram() successfully deleted diagram with id {}", diagramId);

    }

    @Override
    public void createRootFolder(String userName) throws AbortedException, ErrorConnectionException {
        logger.trace("createRootFolder() was called with parameters: username = {}.", userName);
        Folder rootFolder = new Folder("root", userName);
        try {
            transport.open();
            try {
                TFolder newFolder = rootFolder.toTFolder();
                client.createFolder(newFolder);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TIdAlreadyDefined e) {
                logger.error("createRootFolder method encountered an IdAlreadyDefined exception. " +
                                "Folder was not created.", e);
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending createFolder request with " +
                        "parameters: newFolder = {}", rootFolder.getFolderName(), e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending createFolder request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("createRootFolder() successfully created root folder for {}.", userName);
    }

    @Override
    public Long createFolder(@NotNull Folder folder) throws AbortedException, ErrorConnectionException {
        logger.trace("createFolder() was called with parameters: folder = {}.", folder.getFolderName());
        Long result = 0L;
        folder.setUserName(AuthenticatedUser.getUserName());
        try {
            transport.open();
            try {
                TFolder newFolder = folder.toTFolder();
                result = client.createFolder(newFolder);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TIdAlreadyDefined e) {
                logger.error("createFolder method encountered an IdAlreadyDefined exception. " +
                        "Folder was not created.", e);
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending createFolder request with " +
                        "parameters: folder = {}", folder.getFolderName(), e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending createFolder request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("createFolder() successfully created folder {}", folder.getFolderName());
        return result;
    }

    @Override
    public void deleteFolder(Long folderId) throws AbortedException, ErrorConnectionException {
        logger.trace("deleteFolder() was called with parameters: folderId = {}.", folderId);
        try {
            transport.open();
            try {
                client.deleteFolder(folderId);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending deleteFolder request with " +
                        "parameters: folderId = {}", folderId, e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending deleteFolder request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("deleteFolder() successfully deleted folder with id {}.", folderId);
    }

    @Override
    @NotNull
    public Folder getFolderTree() throws NotFoundException, ErrorConnectionException {
        logger.trace("getFolderTree() was called with parameters: userName = {}.",
                AuthenticatedUser.getUserName());
        TFolder folder = new TFolder();
        try {
            transport.open();
            try {
                folder = client.getFolderTree(AuthenticatedUser.getUserName());
            } catch (TNotFound e) {
                throw new NotFoundException(e.getId(), e.getMessage());
            } catch (TErrorConnection e) {
                throw new ErrorConnectionException(e.getClientName(), e.getMessage());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending getFolderTree request with " +
                        "parameters: username = {}.", AuthenticatedUser.getUserName(), e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending getFolderTree request.");
            }
            finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered a problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramService.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("getFolderTree() successfully returned a folderTree.");
        return new Folder(folder);
    }
}
