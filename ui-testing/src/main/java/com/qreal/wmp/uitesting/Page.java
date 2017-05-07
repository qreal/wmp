package com.qreal.wmp.uitesting;

import com.qreal.wmp.uitesting.services.SelectorService;

/** Describes WMP pages in browser. */
@SuppressWarnings("unchecked")
public enum Page {
    
    Auth("auth") {
        @Override
        public <T> T getInstance(PageFactory pageFactory, SelectorService selectorService) {
            return (T) pageFactory.getAuthPage();
        }
    },
    Dashboard("dashboard") {
        @Override
        public <T> T getInstance(PageFactory pageFactory, SelectorService selectorService) {
            return (T) pageFactory.getDashboardPage();
        }
    },
    EditorRobots("robotsEditor") {
        @Override
        public <T> T getInstance(PageFactory pageFactory, SelectorService selectorService) {
            return (T) pageFactory.getEditorPageWithGestures(selectorService.create("robotEditor"));
        }
    },
    EditorBPMN("bpmnEditor") {
        @Override
        public <T> T getInstance(PageFactory pageFactory, SelectorService selectorService) {
            return (T) pageFactory.getEditorPage(selectorService.create("bpmnEditor"));
        }
    };
    
    private String name;
    
    Page(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract <T> T getInstance(PageFactory pageFactory, SelectorService selectorService);
}
