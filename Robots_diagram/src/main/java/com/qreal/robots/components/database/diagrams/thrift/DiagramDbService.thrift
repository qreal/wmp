namespace java com.qreal.robots.components.database.diagrams.thrift.gen

include "Diagram.thrift"

service DiagramDbService {
    i64 saveDiagram(1: Diagram.TDiagramDAO diagram);
    Diagram.TDiagramDAO openDiagram(1: i64 diagramID);
    void deleteDiagram(1: i64 diagramId);
    void rewriteDiagram(1: Diagram.TDiagramDAO diagram);
    i64 createFolder(1: Diagram.TFolderDAO folder);
    void deleteFolder(1: i64 folderId);
    Diagram.TFolderDAO getFolderTree();
}