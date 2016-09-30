package com.qreal.wmp.db.user.client.diagrams;

import com.qreal.wmp.db.user.exceptions.AbortedException;
import com.qreal.wmp.db.user.exceptions.ErrorConnectionException;
import com.qreal.wmp.db.user.model.diagram.Folder;
import com.qreal.wmp.thrift.gen.DiagramDbService;
import com.qreal.wmp.thrift.gen.TAborted;
import com.qreal.wmp.thrift.gen.TFolder;
import com.qreal.wmp.thrift.gen.TIdAlreadyDefined;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
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
    public void createRootFolder(String userName) throws AbortedException, ErrorConnectionException {
        logger.trace("createRootFolder() was called with parameters: username = {}.", userName);
        Folder rootFolder = new Folder("root", userName);
        try {
            transport.open();
            try {
                TFolder newFolder = rootFolder.toTFolder();
                client.createFolder(newFolder);
            } catch (TIdAlreadyDefined e) {
                logger.error("createRootFolder() encountered an IdAlreadyDefined exception. Folder was not created.",
                        e);
            } catch (TAborted e) {
                throw new AbortedException(e.getTextCause(), e.getMessage(), e.getFullClassName());
            } catch (TException e) {
                logger.error("Client DiagramService encountered a problem while sending createFolder request with " +
                        "parameters: newFolder = {}", rootFolder.getFolderName(), e);
                throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService " +
                        "encountered a problem while sending createFolder request");
            } finally {
                transport.close();
            }
        } catch (TTransportException e) {
            logger.error("Client DiagramService encountered problem while opening transport.", e);
            throw new ErrorConnectionException(DiagramServiceImpl.class.getName(), "Client DiagramService encountered " +
                    "a problem while opening transport.");
        }
        logger.trace("createRootFolder() created rootFolder for user {}.", userName);
    }
}
