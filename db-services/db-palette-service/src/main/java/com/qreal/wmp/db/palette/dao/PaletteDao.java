package com.qreal.wmp.db.palette.dao;

import com.qreal.wmp.db.palette.exceptions.AbortedException;
import com.qreal.wmp.db.palette.exceptions.NotFoundException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.db.palette.model.PaletteView;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/** DAO for palette DB. */
public interface PaletteDao {
    /**
     * Saves a palette and creates Id for it.
     * @param palette  palette to save (Id must not be set)
     * @return new id of a palette
     */
    Long createPalette(@NotNull Palette palette) throws AbortedException;


    /** Returns a palette.
     *  @param paletteId id of a palette
     *  @return palette
     */
    @NotNull Palette getPalette(Long paletteId) throws NotFoundException;

    /** Returns user's created palettes.*/
    @NotNull Set<PaletteView> getPalettes(String userName) throws NotFoundException;
}
