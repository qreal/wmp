package com.qreal.wmp.db.palette.server;

import com.qreal.wmp.db.palette.config.AppInit;
import com.qreal.wmp.db.palette.dao.PaletteDao;
import com.qreal.wmp.db.palette.exceptions.NotFoundException;
import com.qreal.wmp.db.palette.model.Palette;
import com.qreal.wmp.db.palette.model.PaletteView;
import com.qreal.wmp.thrift.gen.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("testHandler")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerTest {
    @Autowired
    private PaletteDao paletteDaoMocked;

    private PaletteDbServiceHandler handler;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new PaletteDbServiceHandler(context);
        }
    }

    @After
    public void deleteMocking() {
        reset(paletteDaoMocked);
    }

    /** Test createPalette operation for palette. */
    @Test
    @Rollback
    public void createPalette_correctInput_paletteDaoCalled() throws Exception {
        TPalette testPalette = createPalette("testPalette", "testUser");

        handler.createPalette(testPalette);

        verify(paletteDaoMocked).createPalette(new Palette(testPalette));
    }

    /** Test createPalette operation for palette. */
    @Test
    @Rollback
    public void createPalette_idSet_throwsTIdAlreadyDefined() {
        long idPalette = 0L;
        TPalette testPalette = createPalette("testPalette", "testUser", idPalette);

        assertThatThrownBy(() -> handler.createPalette(testPalette)).isInstanceOf(TIdAlreadyDefined.class);
    }

    /** Test getPalette operation for palette. */
    @Test
    @Rollback
    public void getPalette_paletteExists_returnsTPalette() throws Exception {
        long idPalette = 0L;
        TPalette tPalette = createPalette("testPalette", "testUser", idPalette);
        Palette palette = new Palette(tPalette);

        when(paletteDaoMocked.getPalette(idPalette)).thenReturn(palette);

        TPalette gotPalette = handler.loadPalette(idPalette);

        assertThat(gotPalette).isEqualTo(tPalette);
    }

    /** Test getPalette operation for palette. */
    @Test
    @Rollback
    public void getPalette_paletteNotExists_throwsTNotFound() throws Exception {
        long idPaletteNotCorrect = 0L;

        when(paletteDaoMocked.getPalette(idPaletteNotCorrect)).
                thenThrow(new NotFoundException("0", "Exception"));

        assertThatThrownBy(() -> handler.loadPalette(idPaletteNotCorrect)).isInstanceOf(TNotFound.class);
    }

    /** Test getPaletteViewsByUserName operation for palette.*/
    @Test
    @Rollback
    public void getPaletteViews_returnsPaletteViews() throws Exception {
        String userName = "testUser";
        TPaletteView tPaletteView_1 = new TPaletteView(0L, "testPalette_1");
        TPaletteView tPaletteView_2 = new TPaletteView(1L, "testPalette_2");
        TPaletteView tPaletteView_3 = new TPaletteView(2L, "testPalette_3");

        Set<TPaletteView> tPaletteViews = new HashSet<>();
        tPaletteViews.add(tPaletteView_1);
        tPaletteViews.add(tPaletteView_2);
        tPaletteViews.add(tPaletteView_3);

        PaletteView paletteView_1 = new PaletteView(tPaletteView_1);
        PaletteView paletteView_2 = new PaletteView(tPaletteView_2);
        PaletteView paletteView_3 = new PaletteView(tPaletteView_3);

        Set<PaletteView> paletteViews = new HashSet<>();
        paletteViews.add(paletteView_1);
        paletteViews.add(paletteView_2);
        paletteViews.add(paletteView_3);

        when(paletteDaoMocked.getPaletteViewsByUserName(userName)).thenReturn(paletteViews);

        Set<TPaletteView> gotPaletteViews = handler.getPaletteViewsByUserName(userName);

        assertThat(gotPaletteViews).isEqualTo(tPaletteViews);
    }

    /** Test getPaletteViewsByUserName operation for palette.*/
    @Test
    @Rollback
    public void getPaletteViews_notExistsPaletteViews_throwsTNotFound() throws Exception {
        String username = "testUser";

        doThrow(new NotFoundException("0", "Exception")).when(paletteDaoMocked).getPaletteViewsByUserName(username);

        assertThatThrownBy(() -> handler.getPaletteViewsByUserName(username)).isInstanceOf(TNotFound.class);
    }

    private TPalette createPalette(String namePalette, String user) {
        TPalette tPalette = new TPalette();
        tPalette.setName(namePalette);
        tPalette.setUser(user);
        return tPalette;
    }

    private TPalette createPalette(String namePalette, String user, Long idPalette) {
        TPalette tPalette = new TPalette();
        tPalette.setName(namePalette);
        tPalette.setUser(user);
        tPalette.setId(idPalette);
        return tPalette;
    }

}
