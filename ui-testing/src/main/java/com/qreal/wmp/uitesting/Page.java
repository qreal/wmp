package com.qreal.wmp.uitesting;

/** Describes WMP pages in browser. */
@SuppressWarnings("unchecked")
public enum Page {
    
    Auth("auth") {
        @Override
        public <T> T getInstance(PageFactory pageFactory) {
            return (T) pageFactory.getAuthPage();
        }
    },
    Dashboard("dashboard") {
        @Override
        public <T> T getInstance(PageFactory pageFactory) {
            return (T) pageFactory.getDashboardPage();
        }
    },
    EditorRobots("robotsEditor") {
        @Override
        public <T> T getInstance(PageFactory pageFactory) {
            return (T) pageFactory.getEditorPageWithGestures();
        }
    },
    EditorBPMN("bpmnEditor") {
        @Override
        public <T> T getInstance(PageFactory pageFactory) {
            return (T) pageFactory.getEditorPage();
        }
    };
    
    private String name;
    
    Page(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract <T> T getInstance(PageFactory pageFactory);
}
