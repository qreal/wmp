package com.qreal.wmp.db.palette.dao;

import com.qreal.wmp.db.palette.exceptions.AbortedException;
import com.qreal.wmp.db.palette.exceptions.NotFoundException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.db.palette.model.PaletteView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
public class PaletteDaoImpl implements PaletteDao {
    private static final Logger logger = LoggerFactory.getLogger(PaletteDaoImpl.class);

    private final SessionFactory sessionFactory;

    public PaletteDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long createPalette(@NotNull Palette palette) throws AbortedException {
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

    @Override
    @NotNull
    public Set<PaletteView> getPaletteViewsByUserName(String userName) {
        logger.trace("getPaletteViewsByUserName() called with parameters: user = {}", userName);
        Session session = sessionFactory.getCurrentSession();

        List<Palette> palettes = session.createQuery("from Palette where userName=:userName").
                setParameter("userName", userName).list();
        logger.trace("getPaletteViewsByUserName() extracted list of results from session with {} elements.",
                palettes.size());

        return palettes.stream().map(p -> new PaletteView(p.getId(), p.getPaletteName())).collect(Collectors.toSet());
    }
}
