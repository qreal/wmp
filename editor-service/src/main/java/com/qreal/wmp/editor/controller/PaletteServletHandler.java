package com.qreal.wmp.editor.controller;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.palettes.client.PaletteService;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import com.qreal.wmp.thrift.gen.PaletteServiceThrift;
import com.qreal.wmp.thrift.gen.TPalette;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Thrift PaletteRest controller.
 * RPC functions for palette: createPalette
 */
public class PaletteServletHandler implements PaletteServiceThrift.Iface {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    private final ApplicationContext context;

    public PaletteServletHandler(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Creates palette and assign it id.
     *
     * @param palette palette to create (Id must not be set)
     * @return new id of palette
     */
    @Override
    public long createPalette(TPalette palette) {
        PaletteService paletteService = (PaletteService) context.getBean("paletteService");
        long id = 0;
        Palette newPalette = new Palette(palette);
        try {
            id = paletteService.createPalette(newPalette);
        } catch (AbortedException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createPalette method encountered exception Aborted. Instead of paletteId will be returned 0.",
                    e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createPalette method encountered exception ErrorConnection. Instead of paletteId will be " +
                    "returned 0.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return id;
    }
}
