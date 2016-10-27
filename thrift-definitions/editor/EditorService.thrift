namespace java com.qreal.wmp.thrift.gen

include "../struct/Diagram.thrift"

service EditorServiceThrift {
//CRUD Diagrams
    i64 saveDiagram(1: Diagram.TDiagram diagram),
    Diagram.TDiagram openDiagram(1: i64 diagramID),
    void rewriteDiagram(1: Diagram.TDiagram diagram),
    void deleteDiagram(1: i64 id),

//CRUD Folders
    i64 createFolder(1: Diagram.TFolder folder),
    Diagram.TFolder getFolder(1: i64 folderId),
    void updateFolder(1: Diagram.TFolder folder),
    void deleteFolder(1: i64 id),

//Specific
    Diagram.TFolder getFolderTree(),
    void addUserToOwners(1: i64 folderId, 2: string username)
}
