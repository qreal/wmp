package com.qreal.wmp.db.palette.dao;

import com.qreal.wmp.db.palette.exceptions.NotFoundException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.db.palette.model.PaletteView;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/** DAO for palette DB. */
public interface PaletteDao {
    /**
     * Saves a palette at local DB using Hibernate ORM and creates Id for it.
     * @param palette  palette to save (Id must not be set)
     * @return id of a palette
     */
    Long createPalette(@NotNull Palette palette);

    /** Returns a palette.
     *  @param paletteId id of a palette
     *  @return palette
     *  @throws NotFoundException if the palette with the paletteId was not found in DB
     */
    @NotNull Palette getPalette(Long paletteId) throws NotFoundException;

    /**
     *  Returns user's created palettes (name and id of palettes).
     *  @param userName name of a user
     *  @return palette views for user having the userName
     */
    @NotNull Set<PaletteView> getPaletteViewsByUserName(String userName) throws NotFoundException;
}
