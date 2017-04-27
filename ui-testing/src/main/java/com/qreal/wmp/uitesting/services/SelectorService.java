package com.qreal.wmp.uitesting.services;

public interface SelectorService {
    
    String get(String element, Attribute attr);
    
    String get(Attribute attr);
    
    SelectorService create(String sublevel);
    
    enum Attribute {
        ID("id"), CLASS("class"), NAME("name"), TYPE("type"), TEXT("text"), SELECTOR("selector");
        
        Attribute(String str) {
            this.str = str;
        }
        
        private String str;
        
        public String toString() {
            return str;
        }
    }
}
