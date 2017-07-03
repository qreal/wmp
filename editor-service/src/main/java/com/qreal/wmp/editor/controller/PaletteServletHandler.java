package com.qreal.wmp.editor.controller;

import com.qreal.wmp.editor.common.utils.AuthenticatedUser;
import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.palettes.client.AcceleoService;
import com.qreal.wmp.editor.database.palettes.client.PaletteService;
import com.qreal.wmp.editor.database.palettes.model.Model;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import com.qreal.wmp.editor.database.palettes.model.PaletteView;
import com.qreal.wmp.thrift.gen.PaletteServiceThrift;
import com.qreal.wmp.thrift.gen.TPalette;
import com.qreal.wmp.thrift.gen.TPaletteView;
import com.qreal.wmp.thrift.gen.TModel;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.Set;

/**
 * Thrift PaletteRest controller.
 * RPC functions for palette: createPalette, loadPalette, getPaletteViews
 */
public class PaletteServletHandler implements PaletteServiceThrift.Iface {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    private final ApplicationContext context;

    public PaletteServletHandler(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Creates palette and assigns it an Id.
     *
     * @param palette palette to create (Id must not be set)
     * @return new id of a palette
     */
    @Override
    public long createPalette(TPalette palette) {
        PaletteService paletteService = (PaletteService) context.getBean("paletteService");
        Palette newPalette = new Palette(palette);
        long id = 0;
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

    @Override
    public TPalette loadPalette(long paletteId) {
        PaletteService paletteService = (PaletteService) context.getBean("paletteService");
        TPalette result = null;
        try {
            result = paletteService.loadPalette(paletteId).toTPalette();
        } catch (NotFoundException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("loadPalette method encountered exception NotFound. Instead of palette will be returned null" +
                    ".", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getDiagram method encountered exception ErrorConnection. Instead of palette will be " +
                    "returned null.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        logger.trace("load TPalette");
        return result;
    }

    /** Returns created palettes of user.*/
    @Override
    public Set<TPaletteView> getPaletteViews() {
        PaletteService paletteService = (PaletteService) context.getBean("paletteService");
        Set<TPaletteView> result = new HashSet<>();
        try {
            String userName = AuthenticatedUser.getUserName();
            Set<PaletteView> palettes = paletteService.getPaletteViewsByUserName(userName);
            for (PaletteView palette: palettes) {
                result.add(palette.toTPalette());
            }
        } catch (NotFoundException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getPaletteViews method encountered exception NotFound. Instead of palettes will be " +
                    "returned null.", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return null, but send exception to client side.
            logger.error("getPaletteViews method encountered exception ErrorConnection. Instead of palettes will be " +
                    "returned null.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
        return result;
    }

    @Override
    public void createMetamodel(TPalette palette) {
        AcceleoService acceleoGeneratorService = (AcceleoService)
                context.getBean("acceleoGeneratorService");
        Palette newPalette = new Palette(palette);
        try {
            acceleoGeneratorService.createMetamodel(newPalette);
        } catch (AbortedException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createMetamodel method encountered exception Aborted. Instead of paletteId will " +
                            "be returned 0.", e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createMetamodel method encountered exception ErrorConnection. Instead of paletteId will be " +
                    "returned 0.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }

    @Override
    public void createModel(TModel model) {
        AcceleoService acceleoGeneratorService = (AcceleoService)
                context.getBean("acceleoGeneratorService");
        Model newModel = new Model(model);
        try {
            acceleoGeneratorService.createModel(newModel);
        } catch (AbortedException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createModel method encountered exception Aborted. Instead of paletteId will be returned 0.",
                    e);
        } catch (ErrorConnectionException e) {
            //TODO Here we should not return 0, but send exception to client side.
            logger.error("createModel method encountered exception ErrorConnection. Instead of paletteId will be " +
                    "returned 0.", e);
        } catch (TException e) {
            logger.error("TException was not translated", e);
        }
    }
}
