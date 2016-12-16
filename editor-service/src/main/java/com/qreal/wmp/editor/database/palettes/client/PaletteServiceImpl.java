package com.qreal.wmp.editor.database.palettes.client;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import com.qreal.wmp.editor.database.palettes.model.PaletteView;
import com.qreal.wmp.thrift.gen.PaletteDbService;
import com.qreal.wmp.thrift.gen.TPalette;
import com.qreal.wmp.thrift.gen.TPaletteView;
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
import java.util.HashSet;
import java.util.Set;

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
        transport.open();
        Long result;
        String user = AuthenticatedUser.getUserName();
        palette.setUserName(user);
        try {
            TPalette newPalette = palette.toTPalette();
            result = client.createPalette(newPalette);
        } finally {
            transport.close();
        }
        logger.trace("createPalette() successfully created palette {}", palette.getName());
        return result;
    }

    @Override
    public @NotNull Palette loadPalette(long paletteId) throws NotFoundException, ErrorConnectionException,
            TException {
        logger.trace("loadPalette() was called with parameters: paletteId = {}.", paletteId);
        TPalette tPalette;
        transport.open();
        try {
            tPalette = client.loadPalette(paletteId);
        } finally {
            transport.close();
        }
        logger.trace("loadPallete() successfully returned a palette.");
        return new Palette(tPalette);
    }

    @Override
    @NotNull
    public Set<PaletteView> getPalettes(String userName) throws NotFoundException, ErrorConnectionException,
            TException {
        logger.trace("getPalettes() was called with parameters: owners = {}.", userName);
        Set<TPaletteView> palettes;
        transport.open();
        try {
            palettes = client.getPalettes(userName);
        } finally {
            transport.close();
        }
        logger.trace("getPalettes() successfully returned a palettes.");
        Set<PaletteView> result = new HashSet<>();
        for (TPaletteView tPalette: palettes) {
            result.add(new PaletteView(tPalette));
        }
        return result;
    }
}
