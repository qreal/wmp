namespace java com.qreal.robots.components.database.diagrams.thrift.gen

struct TPropertyDAO {
    1 : string propertyId,
    2 : string name,
    3 : string value,
    4 : string type,
    5 : i32 position
}

struct TDefaultDiagramNodeDAO {
    1 : string logicalId,
    2 : string graphicalId,
    3 : string jointObject,
    4 : string type,
    5 : double x,
    6 : double y,
    7 : set<TPropertyDAO> properties
}

struct TLinkDAO {
    1 : string logicalId,
    2 : string graphicalId,
    3 : string jointObjectId,
    4 : string type,
    5 : string vertices,
    6 : set<TPropertyDAO> properties
}

struct TDiagramDAO {
    1 : i64 diagramId,
    2 : string name,
    3 : string user,
    4 : set<TDefaultDiagramNodeDAO> nodes,
    5 : set<TLinkDAO> links,
    6 : i64 folderId
}

struct TFolderDAO {
    1 : i64 folderId,
    2 : string folderName,
    3 : i64 folderParentId,
    4 : set<TFolderDAO> childrenFolders,
    5 : set<TDiagramDAO> diagrams
}