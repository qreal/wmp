namespace java com.qreal.wmp.thrift.gen

include "../struct/Diagram.thrift"

include "../exception/DbExceptions.thrift"

service DiagramDbService {
    //In case of exception save will be safely aborted
    i64 saveDiagram(1: Diagram.TDiagram diagram) throws(1: DbExceptions.TIdAlreadyDefined e);
    //In case of exception undefined value will be returned
    Diagram.TDiagram openDiagram(1: i64 diagramID) throws (1: DbExceptions.TNotFound e);
    //In case of exception delete will be safely aborted
    void deleteDiagram(1: i64 id) throws (1: DbExceptions.TNotFound e);
    //In case of exception rewrite will be safely aborted
    void rewriteDiagram(1: Diagram.TDiagram diagram) throws (1: DbExceptions.TNotFound notFound,
                                                             2: DbExceptions.TIdNotDefined notDefined),
    //In case of exception create will be safely aborted
    i64 createFolder(1: Diagram.TFolder folder) throws (1: DbExceptions.TIdAlreadyDefined e);
    //In case of exception delete will be safely aborted
    void deleteFolder(1: i64 id) throws (1: DbExceptions.TNotFound e);
    //In case of exception undefined value will be returned
    Diagram.TFolder getFolderTree(1: string username) throws (1: DbExceptions.TNotFound e);
}

