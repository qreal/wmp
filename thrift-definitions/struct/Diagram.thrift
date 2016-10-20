namespace java com.qreal.wmp.thrift.gen

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
    1 : optional i64 id,
    2 : string name,
    3 : string user,
    4 : set<TDefaultDiagramNode> nodes,
    5 : set<TLink> links,
    6 : optional i64 folderId
}

struct TFolder {
    1 : optional i64 id,
    2 : string folderName,
    3 : optional set<string> owners,
    4 : optional i64 folderParentId,
    5 : set<TFolder> childrenFolders,
    6 : set<TDiagram> diagrams
}
