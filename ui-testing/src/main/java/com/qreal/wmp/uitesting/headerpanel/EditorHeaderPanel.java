package com.qreal.wmp.uitesting.headerpanel;

import com.qreal.wmp.uitesting.headerpanel.folderwindow.FolderArea;
import com.qreal.wmp.uitesting.pages.DashboardPage;

public interface EditorHeaderPanel {
    
    DashboardPage toDashboard();
    
    void newDiagram();
    
    FolderArea getFolderArea();
    
    void saveDiagram(String path);
    
    void saveDiagram();
    
    void openDiagram(String path);
    
    boolean isDiagramExist(String path);
    
    boolean equalsDiagrams(String path);
}
