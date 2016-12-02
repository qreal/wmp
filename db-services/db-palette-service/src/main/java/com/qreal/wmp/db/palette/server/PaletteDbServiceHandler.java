package com.qreal.wmp.db.palette.server;

import com.qreal.wmp.db.palette.dao.PaletteDao;
import com.qreal.wmp.db.palette.exceptions.AbortedException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.thrift.gen.*;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

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
        long id;
        if (palette.isSetId()) {
            throw new TIdAlreadyDefined("Palette Id not null. To save a palette you should not assign Id to it.");
        }
        try {
            id = paletteDao.createPalette(new Palette(palette));
        } catch (AbortedException e) {
            //For now never happens
            throw new TAborted(e.getTextCause(), e.getMessage(), e.getFullClassName());
        }
        return id;
    }

}
