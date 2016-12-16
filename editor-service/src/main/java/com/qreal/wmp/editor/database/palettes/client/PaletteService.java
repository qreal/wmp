package com.qreal.wmp.editor.database.palettes.client;

import com.qreal.wmp.editor.database.exceptions.AbortedException;
import com.qreal.wmp.editor.database.exceptions.ErrorConnectionException;
import com.qreal.wmp.editor.database.exceptions.NotFoundException;
import com.qreal.wmp.editor.database.palettes.model.Palette;
import com.qreal.wmp.editor.database.palettes.model.PaletteView;
import org.apache.thrift.TException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface PaletteService {
    /**
     * Saves palette.
     *
     * @param palette  palette to save
     * @return new id of palette
     */
    Long createPalette(@NotNull Palette palette) throws AbortedException, ErrorConnectionException, TException;

    /** Returns palette.*/
    @NotNull
    Palette loadPalette(long paletteId) throws NotFoundException, ErrorConnectionException, TException;

    /** Returns palettes of user.*/
    @NotNull
    Set<PaletteView> getPalettes(String username) throws NotFoundException, ErrorConnectionException, TException;
}
