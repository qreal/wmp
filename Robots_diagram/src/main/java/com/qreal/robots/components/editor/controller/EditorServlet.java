/*package com.qreal.robots.components.editor.controller;

        import com.fasterxml.jackson.databind.JsonNode;
        import com.qreal.robots.common.loaders.TypesLoader;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.ResponseBody;
        import org.springframework.web.servlet.ModelAndView;

        import java.io.IOException;

@Controller
public class EditorController {

    private TypesLoader typesLoader;

    public EditorController() {
        this.typesLoader = new TypesLoader();
    }

    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("editor/editor");
    }

    @ResponseBody
    @RequestMapping(value = "getTypes/", method = RequestMethod.POST)
    public JsonNode getTypes(@RequestParam(value="kit") String kit) throws IOException {
        return typesLoader.getTypesJson();
    }
}*/

package com.qreal.robots.components.editor.controller;

import com.qreal.robots.components.editor.thrift.gen.EditorServiceThrift;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.context.support.AbstractApplicationContext;

public class EditorServlet extends TServlet {

    public EditorServlet(AbstractApplicationContext context) {
        super(
                new EditorServiceThrift.Processor(new EditorServletHandler(context)),
                new TJSONProtocol.Factory()
        );
    }
}
