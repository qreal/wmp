package com.qreal.wmp.uitesting.headerpanel;

import com.qreal.wmp.uitesting.pages.DashboardPage;

public interface EditorHeaderPanel {
    
    DashboardPage toDashboard();
    
    void newDiagram();
    
    FolderArea getFolderArea();
    
    void saveDiagram(String path);
    
    void openDiagram();
    
    boolean isDiagramExist(String path);
}
