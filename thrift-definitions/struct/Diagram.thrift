namespace java com.qreal.wmp.thrift.gen

struct TProperty {
    1 : string propertyId,
    2 : string name,
    3 : string value,
    4 : string type,
    5 : i32 position
}

struct TDefaultDiagramNode {
    1 : string id,
    2 : string logicalId,
    3 : string graphicalId,
    4 : string jointObject,
    5 : string type,
    6 : double x,
    7 : double y,
    8 : set<TProperty> properties
}

struct TLink {
    1: string id,
    2 : string logicalId,
    3 : string graphicalId,
    4 : string jointObjectId,
    5 : string type,
    6 : string vertices,
    7 : set<TProperty> properties
}

struct TDiagram {
    1 : optional i64 id,
    2 : string name,
    3 : string user,
    4 : set<TDefaultDiagramNode> nodes,
    5 : set<TLink> links,
    6 : optional i64 folderId
}

//parent folders should be set only for root
//all others will be reconstructed from children folders
struct TFolder {
    1 : optional i64 id,
    2 : string folderName,
    3 : optional set<string> owners,
    4 : optional set<i64> parentFolders,
    5 : set<TFolder> childrenFolders,
    6 : set<TDiagram> diagrams,
    7 : optional i64 folderParentId
}
