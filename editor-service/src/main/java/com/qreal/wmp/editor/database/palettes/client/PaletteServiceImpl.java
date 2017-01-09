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
import java.util.Set;
import java.util.stream.Collectors;

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
    public @NotNull Long createPalette(@NotNull Palette palette) throws AbortedException, ErrorConnectionException,
            TException {
        logger.trace("createPalette() was called with parameters: name = {}.", palette.getName());
        transport.open();
        String user = AuthenticatedUser.getUserName();
        palette.setUserName(user);
        Long result;
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
    public @NotNull Palette loadPalette(@NotNull long paletteId) throws NotFoundException, ErrorConnectionException,
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
    public Set<PaletteView> getPaletteViewsByUserName(String userName) throws NotFoundException,
            ErrorConnectionException, TException {
        logger.trace("getPaletteViews() was called with parameters: owners = {}.", userName);
        Set<TPaletteView> palettes;
        transport.open();
        try {
            palettes = client.getPaletteViewsByUserName(userName);
        } finally {
            transport.close();
        }
        logger.trace("getPaletteViews() successfully returned a palettes.");
        return palettes.stream().map(tPalette -> new PaletteView(tPalette)).collect(Collectors.toSet());
    }
}
