package com.qreal.wmp.selector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {
    
    private final ConfigsMerger merger;
    
    @Autowired
    public RestController(ConfigsMerger merger) {
        this.merger = merger;
    }
    
    @GetMapping("/all")
    @ResponseBody
    public String getAllConfig() {
        return merger.generateCommonConfig().toString();
    }
    
    @GetMapping("/editor/robots")
    @ResponseBody
    public String getRobotEditor() {
        return merger.generateCommonConfig().get("robotEditor").toString();
    }
    
    @GetMapping("/editor/bpmn")
    @ResponseBody
    public String getBPMNEditor() {
        return merger.generateCommonConfig().get("bpmnEditor").toString();
    }
    
    @GetMapping("/dashboard")
    @ResponseBody
    public String getDashboard() {
        return merger.generateCommonConfig().get("dashboard").toString();
    }
    
    @GetMapping("/authform")
    @ResponseBody
    public String getAuthform() {
        return merger.generateCommonConfig().get("authform").toString();
    }
    
    @GetMapping("/auth")
    @ResponseBody
    public String getAuth() {
        return merger.generateCommonConfig().get("authService").toString();
    }
    
}
