package com.qreal.wmp.uitesting;

/** Opening some pages must initialize included components. */
public interface InitializedComponent {
    void init();
    
    boolean name(String name);
}
