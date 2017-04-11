package com.qreal.wmp.uitesting.services;

public interface SelectorService {
    
    String getId(String element);
    
    String getText(String element);
    
    String getSelector(String element);
    
    String getName(String element);
    
    String getType(String element);
    
    SelectorService create(String sublevel);
}
