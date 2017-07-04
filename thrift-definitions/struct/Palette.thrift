namespace java com.qreal.wmp.thrift.gen

struct TNode {
    1 : string nodeId,
    2 : string name,
    3 : string image,
    4 : set<TNodeProperty> properties
}

struct TPalette {
    1 : optional i64 id,
    2 : string name,
    3 : string user,
    4 : set<TNode> nodes
}

struct TNodeProperty {
    1 : string propertyId,
    2 : string name,
    3 : string value,
    4 : string type
}

struct TPaletteView {
    1 : i64 id,
    2 : string name
}

struct TModel {
    1 : string name,
    2 : string user,
    3 : set<TNode> nodes,
    4 : string metamodelName
}