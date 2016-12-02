package com.qreal.wmp.editor.database.palettes.client;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import com.qreal.wmp.thrift.gen.PaletteDbService;
import com.qreal.wmp.thrift.gen.TPalette;
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

/** Thrift client side of PaletteDbService.*/
@Service("paletteService")
@PropertySource("classpath:client.properties")
public class PaletteServiceImpl implements PaletteService {
    private static final Logger logger = LoggerFactory.getLogger(PaletteServiceImpl.class);

    private TTransport transport;

    private PaletteDbService.Client client;

    @Value("${port.db.palette}")
    private int port;

    @Value("${path.db.palette}")
    private String url;

    /**
     * Connects to a Thrift TServer.
     */
    @PostConstruct
    public void start() {
        logger.info("Client PaletteService was created with Thrift socket on url = {}, port = {}", url, port);
        transport = new TSocket(url, port);
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new PaletteDbService.Client(protocol);
    }

    @Override
    public Long createPalette(@NotNull Palette palette) throws AbortedException, ErrorConnectionException, TException {
        logger.trace("createPalette() was called with parameters: name = {}.", palette.getName());
        Long result = 0L;
        transport.open();
        try {
            TPalette newPalette = palette.toTPalette();
            result = client.createPalette(newPalette);
        } finally {
            transport.close();
        }
        logger.trace("createPalette() successfully created palette {}", palette.getName());
        return result;
    }
}
