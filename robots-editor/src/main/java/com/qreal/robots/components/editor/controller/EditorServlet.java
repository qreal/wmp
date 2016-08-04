package com.qreal.robots.components.editor.controller;

import com.qreal.robots.thrift.gen.EditorServiceThrift;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Thrift service class for EditorRest controller.
 */
public class EditorServlet extends TServlet {

    public EditorServlet(AbstractApplicationContext context) {
        super(
                new EditorServiceThrift.Processor(new EditorServletHandler(context)),
                new TJSONProtocol.Factory()
        );
    }
}
