package com.qreal.wmp.uitesting.headerpanel.folderwindow;

/** Provides interface to working with folder window. */
public interface FolderArea extends AutoCloseable {
    FolderArea createFolder(String name);
    
    boolean isFolderExist(String name);
    
    FolderArea moveForward(String name);
    
    FolderArea moveBack();
    
    String getCurrentPath();
    
    FolderArea move(String path);
    
    FolderArea deleteFolder(String name);
}
