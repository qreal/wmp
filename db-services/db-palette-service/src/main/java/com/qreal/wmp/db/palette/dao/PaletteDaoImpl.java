package com.qreal.wmp.db.palette.dao;

import com.qreal.wmp.db.palette.model.Palette;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PaletteDaoImpl implements PaletteDao {
    private static final Logger logger = LoggerFactory.getLogger(PaletteDaoImpl.class);

    private final SessionFactory sessionFactory;

    public PaletteDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves a palette at local DB using Hibernate ORM.
     *
     * @param palette palette to create (Id must not be set)
     */
    @Override
    public Long createPalette(@NotNull Palette palette) {
        logger.trace("createPalette() was called with parameters: palette = {}.", palette.getName());
        Session session = sessionFactory.getCurrentSession();
        session.save(palette);
        Long paletteId = palette.getId();
        logger.trace("createPalette() successfully created a palette with id {}", paletteId);
        return paletteId;
    }

}
