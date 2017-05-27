package com.qreal.wmp.editor.database.palettes.client;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.palettes.model.Palette;
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
import com.qreal.wmp.thrift.gen.AcceleoServiceThrift;
import com.qreal.wmp.thrift.gen.TPalette;

import javax.annotation.PostConstruct;

/** Thrift client side of PaletteDbService.*/
@Service("acceleoGeneratorService")
@PropertySource("classpath:client.properties")
public class AcceleoServiceImpl implements AcceleoService {
    private static final Logger logger = LoggerFactory.getLogger(PaletteServiceImpl.class);

    private TTransport transport;

    private AcceleoServiceThrift.Client client;

    @Value("${port.generator}")
    private int port;

    @Value("${path.generator}")
    private String url;

    /**
     * Connects to a Thrift TServer.
     */
    @PostConstruct
    public void start() {
        logger.info("Client AcceleoService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new AcceleoServiceThrift.Client(protocol);
    }

    @Override
    public void createMetamodel(@NotNull Palette palette) throws AbortedException, ErrorConnectionException, TException
    {
        logger.trace("createMetamodel() was called with parameters: name = {}.", palette.getName());
        transport.open();
        String user = AuthenticatedUser.getUserName();
        palette.setUserName(user);
        try {
            TPalette newPalette = palette.toTPalette();
            client.createMetamodel(newPalette);
        } finally {
            transport.close();
        }
        logger.trace("createMetamodel() successfully created palette {}", palette.getName());
    }
}
