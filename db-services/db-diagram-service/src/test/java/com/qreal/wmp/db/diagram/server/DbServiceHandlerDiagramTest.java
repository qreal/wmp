package com.qreal.wmp.db.diagram.server;

import com.qreal.wmp.db.diagram.config.AppInit;
import com.qreal.wmp.db.diagram.dao.DiagramDao;
import com.qreal.wmp.db.diagram.exceptions.AbortedException;
import com.qreal.wmp.db.diagram.exceptions.NotFoundException;
import com.qreal.wmp.db.diagram.model.Diagram;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("testHandler")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class DbServiceHandlerDiagramTest {
    @Autowired
    private DiagramDao diagramDaoMocked;

    private DiagramDbServiceHandler handler;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new DiagramDbServiceHandler(context);
        }
    }

    @After
    public void deleteMocking() {
        reset(diagramDaoMocked);
    }

    /** Test saveDiagram operation for diagram. */
    @Test
    @Rollback
    public void saveDiagram_correctInput_diagramDaoCalled() throws Exception {
        long idFolder = 0L;
        TDiagram testDiagram = createDiagram("testDiagram", idFolder);

        handler.saveDiagram(testDiagram);

        verify(diagramDaoMocked).saveDiagram(new Diagram(testDiagram), testDiagram.getFolderId());
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

    /** Test getDiagram operation for diagram. */
    @Test
    @Rollback
    public void getDiagram_diagramExists_returnsTDiagram() throws Exception {
        long idFolder = 0L;
        long idDiagram = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder, idDiagram);
        Diagram diagram = new Diagram(tDiagram);

        when(diagramDaoMocked.getDiagram(idDiagram)).thenReturn(diagram);

        TDiagram gotDiagram = handler.getDiagram(idDiagram);
        gotDiagram.setFolderId(idFolder);

        assertThat(gotDiagram).isEqualTo(tDiagram);
    }

    /** Test getDiagram operation for diagram. */
    @Test
    @Rollback
    public void getDiagram_diagramNotExists_throwsTNotFound() throws Exception {
        long idDiagramNotCorrect = 0L;

        when(diagramDaoMocked.getDiagram(idDiagramNotCorrect)).
                thenThrow(new NotFoundException("0", "Exception"));

        assertThatThrownBy(() -> handler.getDiagram(idDiagramNotCorrect)).isInstanceOf(TNotFound.class);
    }

    /** Test deleteDiagram operation for diagram. */
    @Test
    @Rollback
    public void deleteDiagram_correctInput_diagramDaoCalled() throws Exception {
        long idDiagram = 0L;

        handler.deleteDiagram(idDiagram);

        verify(diagramDaoMocked).deleteDiagram(idDiagram);
    }

    /** Test deleteDiagram operation for diagram. */
    @Test
    @Rollback
    public void deleteDiagram_daoThrowsAborted_throwsTAborted() throws Exception {
        long idDiagram = 0L;

        doThrow(new AbortedException("0", "Exception", "Exception")).
                when(diagramDaoMocked).deleteDiagram(idDiagram);

        assertThatThrownBy(() -> handler.deleteDiagram(idDiagram)).isInstanceOf(TAborted.class);
    }

    /** Test updateDiagram operation for diagram. */
    @Test
    @Rollback
    public void updateDiagram_correctInput_diagramDaoCalled() throws Exception {
        long idFolder = 0L;
        long idDiagram = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder, idDiagram);
        Diagram diagram = new Diagram(tDiagram);

        handler.updateDiagram(tDiagram);

        verify(diagramDaoMocked).rewriteDiagram(diagram);
    }

    /** Test updateDiagram operation for diagram. */
    @Test
    @Rollback
    public void updateDiagram_idNotSet_throwsTIdNotDefined() throws Exception {
        long idFolder = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder);

        assertThatThrownBy((() -> handler.updateDiagram(tDiagram))).isInstanceOf(TIdNotDefined.class);
    }

    /** Test updateDiagram operation for diagram. */
    @Test
    @Rollback
    public void updateDiagram_daoThrowsAborted_throwsTAborted() throws Exception {
        long idFolder = 0L;
        long idDiagram = 0L;
        TDiagram tDiagram = createDiagram("testDiagram", idFolder, idDiagram);
        Diagram diagram = new Diagram(tDiagram);

        doThrow(new AbortedException("0", "Exception", "Exception")).
                when(diagramDaoMocked).rewriteDiagram(diagram);

        assertThatThrownBy(() -> handler.updateDiagram(tDiagram)).isInstanceOf(TAborted.class);
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
