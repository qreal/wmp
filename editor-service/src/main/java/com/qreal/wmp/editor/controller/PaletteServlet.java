package com.qreal.wmp.editor.controller;

import com.qreal.wmp.thrift.gen.PaletteServiceThrift;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.context.ApplicationContext;

public class PaletteServlet extends TServlet {
    public PaletteServlet(ApplicationContext context) {
        super(
                new PaletteServiceThrift.Processor(new PaletteServletHandler(context)),
                new TJSONProtocol.Factory()
        );
    }

}
