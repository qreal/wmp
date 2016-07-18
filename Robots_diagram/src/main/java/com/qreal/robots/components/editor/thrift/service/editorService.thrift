namespace java com.qreal.robots.components.editor.thrift.gen

struct PropertyDAO {
    1 : string propertyId,
    2 : string name,
    3 : string value,
    4 : string type,
    5 : i32 position
}

struct DefaultDiagramNodeDAO {
    1 : string logicalId,
    2 : string graphicalId,
    3 : string jointObject,
    4 : string type,
    5 : double x,
    6 : double y,
    7 : set<PropertyDAO> properties
}

struct LinkDAO {
    1 : string logicalId,
    2 : string graphicalId,
    3 : string jointObjectId,
    4 : string type,
    5 : string vertices,
    6 : set<PropertyDAO> properties
}

struct DiagramDAO {
    1 : i64 diagramId,
    2 : string name,
    3 : string user,
    4 : set<DefaultDiagramNodeDAO> nodes,
    5 : set<LinkDAO> links,
    6 : i64 folderId
}

struct FolderDAO {
    1 : i64 folderId,
    2 : string folderName,
    3 : i64 folderParentId,
    4 : set<FolderDAO> childrenFolders,
    5 : set<DiagramDAO> diagrams
}

service EditorServiceThrift {
    i64 saveDiagram(1: DiagramDAO diagram);
    DiagramDAO openDiagram(1: i64 diagramID);
    void deleteDiagram(1: i64 diagramId);
    void rewriteDiagram(1: DiagramDAO diagram);
    i64 createFolder(1: FolderDAO folder);
    void deleteFolder(1: i64 folderId);
    FolderDAO getFolderTree();
}