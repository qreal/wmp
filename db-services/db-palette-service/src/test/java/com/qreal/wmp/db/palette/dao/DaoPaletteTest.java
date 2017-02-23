package com.qreal.wmp.db.palette.dao;


import com.qreal.wmp.db.palette.config.AppInit;
import com.qreal.wmp.db.palette.exceptions.AbortedException;
import com.qreal.wmp.db.palette.exceptions.NotFoundException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.db.palette.model.PaletteView;
import com.qreal.wmp.thrift.gen.TNotFound;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DaoPaletteTest {
    @Autowired
    private PaletteDao paletteDao;

    /** Test createPalette operation for palette. */
    @Test
    @Rollback
    public void createPalette_paletteSavedInDb() throws Exception {
        Palette testPalette = new Palette("testPalette", "testUser");
        long idPalette = paletteDao.createPalette(testPalette);

        assertThat(idPalette).isNotNull();
    }

    /** Test getPalette operation for palette. */
    @Test
    @Rollback
    public void createPalette_and_getPalette() throws Exception {
        Palette testPalette = new Palette("testPalette", "testUser");
        long idPaletteCreated = paletteDao.createPalette(testPalette);

        Palette gotPalette = paletteDao.getPalette(idPaletteCreated);

        assertThat(gotPalette).isEqualTo(testPalette);
    }

    /** Test getPalette operation for palette. */
    @Test
    @Rollback
    public void getPalette_notCorrectId_throwsNotFound() {
        long idPaletteNotCorrect = 0L;

        assertThatThrownBy(() -> paletteDao.getPalette(idPaletteNotCorrect)).isInstanceOf(NotFoundException.class);
    }

    /** Test getPaletteViews operation for palette. */
    @Test
    @Rollback
    public void getPaletteViews_returnsPaletteViews() throws Exception {
        String namePalette_1 = "testPalette_1";
        String namePalette_2 = "testPalette_2";
        String namePalette_3 = "testPalette_3";

        String userName = "testUser";

        Palette testPalette_1 = new Palette(namePalette_1, userName);
        Palette testPalette_2 = new Palette(namePalette_2, userName);
        Palette testPalette_3 = new Palette(namePalette_3, userName);

        long idPalette_1 = paletteDao.createPalette(testPalette_1);
        long idPalette_2 = paletteDao.createPalette(testPalette_2);
        long idPalette_3 = paletteDao.createPalette(testPalette_3);

        Set<PaletteView> paletteViews = new HashSet<>();
        paletteViews.add(new PaletteView(idPalette_1, namePalette_1));
        paletteViews.add(new PaletteView(idPalette_2, namePalette_2));
        paletteViews.add(new PaletteView(idPalette_3, namePalette_3));

        assertThat(paletteDao.getPaletteViewsByUserName(userName)).isEqualTo(paletteViews);
    }
}

