package com.qreal.wmp.uitesting.services;

public interface SelectorService {
    
    String getId();
    
    String getId(String element);
    
    String getSelector();
    
    String getSelector(String element);
    
    SelectorService create(String sublevel);
}
