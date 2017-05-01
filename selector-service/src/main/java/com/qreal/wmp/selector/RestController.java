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
	String getAllConfig() {
		return merger.generateCommonConfig().toString();
	}
	
	@GetMapping("/editor/robots")
	@ResponseBody
	String getRobotEditor() {
		return merger.generateCommonConfig().get("robotEditor").toString();
	}
	
	@GetMapping("/editor/bpmn")
	@ResponseBody
	String getBPMNEditor() {
		return merger.generateCommonConfig().get("bpmnEditor").toString();
	}
	
	@GetMapping("/dashboard")
	@ResponseBody
	String getDashboard() {
		return merger.generateCommonConfig().get("dashboard").toString();
	}
	
	@GetMapping("/authform")
	@ResponseBody
	String getAuthform() {
		return merger.generateCommonConfig().get("authform").toString();
	}
	
	@GetMapping("/auth")
	@ResponseBody
	String getAuth() {
		return merger.generateCommonConfig().get("authService").toString();
	}
	
}
