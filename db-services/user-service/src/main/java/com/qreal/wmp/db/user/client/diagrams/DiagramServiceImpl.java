package com.qreal.wmp.db.user.client.diagrams;

import com.qreal.wmp.db.user.model.diagram.Folder;
import com.qreal.wmp.thrift.gen.DiagramDbService;
import com.qreal.wmp.thrift.gen.TFolder;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Thrift client side of DiagramDbService.
 */
@Service("diagramService")
public class DiagramServiceImpl implements DiagramService {

    private static final Logger logger = LoggerFactory.getLogger(DiagramServiceImpl.class);

    private TTransport transport;

    private DiagramDbService.Client client;

    /**
     * Constructor creates connection with Thrift TServer.
     */
    public DiagramServiceImpl() {
        String url = "localhost";
        int port = 9093;
        logger.info("Client DiagramService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new DiagramDbService.Client(protocol);
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

    private Long createFolder(Folder folder) {
        logger.trace("createFolder method called with parameters: folder = {}", folder.getFolderName());
        Long result = 0L;
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
}
