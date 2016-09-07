namespace java com.qreal.wmp.thrift.gen

include "../struct/Diagram.thrift"

include "../exception/DbExceptions.thrift"

service DiagramDbService {
    /**
    * In case of exception save will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. Folder to save diagram into doesn't exist
    **/
    i64 saveDiagram(1: Diagram.TDiagram diagram) throws(1: DbExceptions.TIdAlreadyDefined alreadyDefined,
                                                        2: DbExceptions.TAborted aborted);

    //If not found diagram with specified id TNotFound will be thrown
    Diagram.TDiagram openDiagram(1: i64 diagramID) throws (1: DbExceptions.TNotFound e);


    /**
    * In case of exception delete will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. Diagram to delete doesn't exist
    **/
    void deleteDiagram(1: i64 id) throws (1: DbExceptions.TAborted e);

    /**
    * In case of exception rewriteDiagram will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. Diagram to rewrite doesn't exist
    **/
    void rewriteDiagram(1: Diagram.TDiagram diagram) throws (1: DbExceptions.TAborted aborted,
                                                             2: DbExceptions.TIdNotDefined notDefined),

    /**
    * In case of exception createFolder will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * No cases for now
    **/
    i64 createFolder(1: Diagram.TFolder folder) throws (1: DbExceptions.TIdAlreadyDefined alreadyDefined,
                                                        2: DbExceptions.TAborted aborted);

    /**
    * In case of exception deleteFolder will be safely aborted
    *
    * TAborted will be thrown in following cases:
    * 1. Folder to delete doesn't exist
    **/
    void deleteFolder(1: i64 id) throws (1: DbExceptions.TAborted e);

    //If not found folderTree with specified username of owner TNotFound will be thrown
    Diagram.TFolder getFolderTree(1: string username) throws (1: DbExceptions.TNotFound e);
}

