package com.qreal.wmp.db.palette.dao;

import com.qreal.wmp.db.palette.exceptions.NotFoundException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.db.palette.model.PaletteView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        logger.trace("createPalette() was called with parameters: name = {}.", palette.getPaletteName());
        Session session = sessionFactory.getCurrentSession();
        session.save(palette);
        Long paletteId = palette.getId();
        logger.trace("createPalette() successfully created a palette with id {}", paletteId);

        return paletteId;
    }

    @Override
    public @NotNull Palette getPalette(Long paletteId) throws NotFoundException {
        logger.trace("getPalette() was called with parameters: id = {}.", paletteId);
        Session session = sessionFactory.getCurrentSession();
        Palette palette = (Palette) session.get(Palette.class, paletteId);
        if (palette == null) {
            throw new NotFoundException(String.valueOf(paletteId), "Palette not found.");
        }
        logger.trace("getPalette() extracted a palette successfully.");
        return palette;
    }

    /**
     * Returns user's palettes.
     * @param userName name of a user
     */
    @Override
    @NotNull
    public Set<PaletteView> getPalettes(String userName) throws NotFoundException {
        logger.trace("getPalettes() called with parameters: user = {}", userName);
        Session session = sessionFactory.getCurrentSession();

        Set<PaletteView> result = new HashSet<>();
        List<Palette> palettes = session.createQuery("from Palette where userName=:userName").
                setParameter("userName", userName).list();
        logger.trace("getPalettes() extracted list of results from session with {} elements.", palettes.size());
        for (int i = 0; i < palettes.size(); i++) {
            Palette palette = palettes.get(i);
            result.add(new PaletteView(palette.getId(), palette.getPaletteName()));
        }
        return result;
    }
}
