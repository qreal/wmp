namespace java com.qreal.robots.components.editor.thrift.gen

struct TProperty {
    1 : string propertyId,
    2 : string name,
    3 : string value,
    4 : string type,
    5 : i32 position
}

struct TDefaultDiagramNode {
    1 : string logicalId,
    2 : string graphicalId,
    3 : string jointObject,
    4 : string type,
    5 : double x,
    6 : double y,
    7 : set<TProperty> properties
}

struct TLink {
    1 : string logicalId,
    2 : string graphicalId,
    3 : string jointObjectId,
    4 : string type,
    5 : string vertices,
    6 : set<TProperty> properties
}

struct TDiagram {
    1 : optional i64 diagramId,
    2 : string name,
    3 : string user,
    4 : set<TDefaultDiagramNode> nodes,
    5 : set<TLink> links,
    6 : i64 folderId
}

struct TFolder {
    1 : optional i64 folderId
    2 : string folderName,
    3 : optional i64 folderParentId,
    4 : set<TFolder> childrenFolders,
    5 : set<TDiagram> diagrams
}

service EditorServiceThrift {
    i64 saveDiagram(1: TDiagram diagram),
    TDiagram openDiagram(1: i64 diagramID),
    void deleteDiagram(1: i64 diagramId),
    void rewriteDiagram(1: TDiagram diagram),
    i64 createFolder(1: TFolder folder),
    void deleteFolder(1: i64 folderId),
    TFolder getFolderTree()
}