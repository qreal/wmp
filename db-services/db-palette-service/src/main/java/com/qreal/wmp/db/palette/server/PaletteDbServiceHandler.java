package com.qreal.wmp.db.palette.server;

import com.qreal.wmp.db.palette.dao.PaletteDao;
import com.qreal.wmp.db.palette.exceptions.AbortedException;
import com.qreal.wmp.db.palette.exceptions.NotFoundException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.db.palette.model.PaletteView;
import com.qreal.wmp.thrift.gen.*;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/** Thrift server side handler for PaletteDBService.*/
@Transactional
public class PaletteDbServiceHandler implements PaletteDbService.Iface {
    private PaletteDao paletteDao;

    public PaletteDbServiceHandler(ApplicationContext context) {
        paletteDao = (PaletteDao) context.getBean("paletteDao");
        assert paletteDao != null;
    }

    @Override
    public long createPalette(TPalette palette) throws TAborted, TIdAlreadyDefined {
        long paletteId = 0;
        if (palette.isSetId()) {
            throw new TIdAlreadyDefined("Palette Id not null. To save a palette you should not assign Id to it.");
        }
        try {
            paletteId = paletteDao.createPalette(new Palette(palette));
        } catch (AbortedException e) {
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
        return paletteId;
    }

    @Override
    public TPalette loadPalette(long paletteId) throws TNotFound {
        Palette palette;
        try {
            palette = paletteDao.getPalette(paletteId);
        }
        catch (NotFoundException e) {
            throw new TNotFound(String.valueOf(paletteId), "Palette not found.");
        }
        return palette.toTPalette();
    }

    @Override
    public Set<TPaletteView> getPalettes(String username) throws TNotFound {
        Set<PaletteView> palettes;
        Set<TPaletteView> result = new HashSet<>();
        try {
            palettes = paletteDao.getPalettes(username);
        } catch (NotFoundException e) {
            throw new TNotFound(username, "Palettes for specified user not found.");
        }
        for (PaletteView palette: palettes) {
            result.add(palette.toTPalette());
        }
        return result;
    }
}
