namespace java com.qreal.robots.thrift.gen

include "../struct/Diagram.thrift"

service DiagramDbService {
    i64 saveDiagram(1: Diagram.TDiagram diagram);
    Diagram.TDiagram openDiagram(1: i64 diagramID);
    void deleteDiagram(1: i64 diagramId);
    void rewriteDiagram(1: Diagram.TDiagram diagram);
    i64 createFolder(1: Diagram.TFolder folder);
    void deleteFolder(1: i64 folderId);
    Diagram.TFolder getFolderTree(1: string username);
}
