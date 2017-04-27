package com.qreal.wmp.uitesting.pages;

import java.util.ArrayList;
import java.util.List;

/** Catches events and notify observers. */
public class EventProvider {
    
    private final List<EventListener> listeners;
    
    public EventProvider() {
        listeners = new ArrayList<>();
    }
    
    public void addListener(EventListener eventListener) {
        listeners.add(eventListener);
    }
    
    @SuppressWarnings("unused")
    public void removeListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }
    
    public void resetEvent() {
        listeners.forEach(EventListener::updateEvent);
    }
    
    public interface EventListener {
        void updateEvent();
    }
}
