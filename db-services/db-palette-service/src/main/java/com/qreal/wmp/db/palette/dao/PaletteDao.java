package com.qreal.wmp.db.palette.dao;

import com.qreal.wmp.db.palette.exceptions.AbortedException;
import com.qreal.wmp.db.palette.model.Palette;
import org.jetbrains.annotations.NotNull;

/** DAO for palette DB. */
public interface PaletteDao {
    /**
     * Saves a palette and creates Id for it.
     * @param palette  palette to save (Id must not be set)
     * @return new id of a palette
     */
    Long createPalette(@NotNull Palette palette) throws AbortedException;
}
