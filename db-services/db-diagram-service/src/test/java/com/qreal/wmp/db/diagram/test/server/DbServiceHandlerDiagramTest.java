package com.qreal.wmp.db.diagram.test.server;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.Aborted;
import com.qreal.wmp.db.diagram.exceptions.NotFound;
import com.qreal.wmp.db.diagram.model.Diagram;
import com.qreal.wmp.db.diagram.server.DiagramDbServiceHandler;
import com.qreal.wmp.thrift.gen.*;
import org.junit.After;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerDiagramTest {

    public DiagramDbServiceHandler handler;

    @Autowired
    public ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new DiagramDbServiceHandler(context);
        }
        handler.setDiagramDao(mock(DiagramDao.class));
    }

    @After
    public void deleteMocking() {
        handler.rewindDiagramDao();
    }

    /** Test saveDiagram operation for diagram. */
    @Test
    @Rollback
    public void saveDiagram_correctInput_diagramDaoCalled() throws TAborted, TIdAlreadyDefined, Aborted {
        long idFolder = 0L;
        TDiagram testDiagram = createDiagram("testDiagram", idFolder);

        handler.saveDiagram(testDiagram);

        verify(handler.getDiagramDao()).saveDiagram(new Diagram(testDiagram), testDiagram.getFolderId());
    }

    /** Test saveDiagram operation for diagram. */
    @Test
    @Rollback
    public void saveDiagram_idSet_throwsTIdAlreadyDefined() {
        long idFolder = 0L;
        long idDiagram = 0L;
        TDiagram testDiagram = createDiagram("testDiagram", idFolder, idDiagram);

        assertThatThrownBy(() -> handler.saveDiagram(testDiagram)).isInstanceOf(TIdAlreadyDefined.class);
    }

    /** Test openDiagram operation for diagram. */
    @Test
    @Rollback
    public void openDiagram_diagramExists_returnsTDiagram() throws TNotFound, NotFound {
        long idFolder = 0L;
        long idDiagram = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder, idDiagram);
        Diagram diagram = new Diagram(tDiagram);

        when(handler.getDiagramDao().getDiagram(idDiagram)).thenReturn(diagram);

        TDiagram gotDiagram = handler.openDiagram(idDiagram);
        gotDiagram.setFolderId(idFolder);

        assertThat(gotDiagram).isEqualTo(tDiagram);
    }

    /** Test openDiagram operation for diagram. */
    @Test
    @Rollback
    public void openDiagram_diagramNotExists_throwsTNotFound() throws NotFound {
        long idDiagramNotCorrect = 0L;

        when(handler.getDiagramDao().getDiagram(idDiagramNotCorrect)).thenThrow(new NotFound("0", "Exception"));

        assertThatThrownBy(() -> handler.openDiagram(idDiagramNotCorrect)).isInstanceOf(TNotFound.class);
    }

    /** Test deleteDiagram operation for diagram. */
    @Test
    @Rollback
    public void deleteDiagram_correctInput_diagramDaoCalled() throws TAborted, TIdAlreadyDefined,
            TNotFound, NotFound, Aborted {
        long idDiagram = 0L;

        handler.deleteDiagram(idDiagram);

        verify(handler.getDiagramDao()).deleteDiagram(idDiagram);
    }

    /** Test deleteDiagram operation for diagram. */
    @Test
    @Rollback
    public void deleteDiagram_daoThrowsAborted_throwsTAborted() throws Aborted {
        long idDiagram = 0L;

        doThrow(new Aborted("0", "Exception", "Exception")).when(handler.getDiagramDao()).deleteDiagram(idDiagram);

        assertThatThrownBy(() -> handler.deleteDiagram(idDiagram)).isInstanceOf(TAborted.class);
    }

    /** Test rewriteDiagram operation for diagram. */
    @Test
    @Rollback
    public void rewriteDiagram_correctInput_diagramDaoCalled() throws TIdNotDefined, TAborted, Aborted {
        long idFolder = 0L;
        long idDiagram = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder, idDiagram);
        Diagram diagram = new Diagram(tDiagram);

        handler.rewriteDiagram(tDiagram);

        verify(handler.getDiagramDao()).rewriteDiagram(diagram);
    }

    /** Test rewriteDiagram operation for diagram. */
    @Test
    @Rollback
    public void rewriteDiagram_idNotSet_throwsTIdNotDefined() throws TIdNotDefined, TAborted, Aborted {
        long idFolder = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder);

        assertThatThrownBy((() -> handler.rewriteDiagram(tDiagram))).isInstanceOf(TIdNotDefined.class);
    }

    /** Test rewriteDiagram operation for diagram. */
    @Test
    @Rollback
    public void rewriteDiagram_daoThrowsAborted_throwsTAborted() throws TIdNotDefined, TAborted, Aborted {
        long idFolder = 0L;
        long idDiagram = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder, idDiagram);
        Diagram diagram = new Diagram(tDiagram);

        doThrow(new Aborted("0", "Exception", "Exception")).when(handler.getDiagramDao()).rewriteDiagram(diagram);

        assertThatThrownBy(() -> handler.rewriteDiagram(tDiagram)).isInstanceOf(TAborted.class);
    }

    private TDiagram createDiagram(String nameDiagram) {
        TDiagram tDiagram = new TDiagram();
        tDiagram.setName(nameDiagram);
        return tDiagram;
    }

    private TDiagram createDiagram(String nameDiagram, Long idFolder) {
        TDiagram tDiagram = new TDiagram();
        tDiagram.setName(nameDiagram);
        tDiagram.setFolderId(idFolder);
        return tDiagram;
    }

    private TDiagram createDiagram(String nameDiagram, Long idFolder, Long idDiagram) {
        TDiagram tDiagram = new TDiagram();
        tDiagram.setName(nameDiagram);
        tDiagram.setFolderId(idFolder);
        tDiagram.setId(idDiagram);
        return tDiagram;
    }

}
