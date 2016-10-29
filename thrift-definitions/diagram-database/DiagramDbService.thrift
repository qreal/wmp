namespace java com.qreal.wmp.thrift.gen

include "../struct/Diagram.thrift"

include "../exception/DbExceptions.thrift"

service DiagramDbService {

//CRUD DIAGRAMS

    /**
    * In case of exception save operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * 1. The folder to save the diagram into doesn't exist
    **/
    i64 saveDiagram(1: Diagram.TDiagram diagram) throws(1: DbExceptions.TIdAlreadyDefined alreadyDefined,
                                                        2: DbExceptions.TAborted aborted);

    /** TNotFound is thrown if the diagram with specified id is not found.*/
    Diagram.TDiagram getDiagram(1: i64 diagramID) throws (1: DbExceptions.TNotFound e);

    /**
    * In case of exception delete operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * 1. The diagram to delete doesn't exist
    **/
    void deleteDiagram(1: i64 id) throws (1: DbExceptions.TAborted e);

    /**
    * In case of exception updateDiagram operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * 1. The diagram to update doesn't exist
    **/
    void updateDiagram(1: Diagram.TDiagram diagram) throws (1: DbExceptions.TAborted aborted,
                                                             2: DbExceptions.TIdNotDefined notDefined);

//CRUD FOLDERS

    /**
    * In case of exception saveFolder operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * No cases for now.
    **/
    i64 saveFolder(1: Diagram.TFolder folder) throws (1: DbExceptions.TIdAlreadyDefined alreadyDefined,
                                                        2: DbExceptions.TAborted aborted);

    /**
    * Username used to find right parent.
    * TNotFound is thrown if the folder with specified id is not found.
    **/

    Diagram.TFolder getFolder(1: i64 folderId, 2: string username) throws (1: DbExceptions.TNotFound e);

    /**
    * In case of exception deleteFolder operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * 1. The folder to delete doesn't exist
    **/
    void deleteFolder(1: i64 id) throws (1: DbExceptions.TAborted e);

    /**
    * In case of exception updateFolder operation will be safely aborted.
    *
    * TAborted will be thrown in the following cases:
    * 1. The folder to update doesn't exist
    **/
    void updateFolder(1: Diagram.TFolder folder)  throws (1: DbExceptions.TAborted aborted,
                                                         2: DbExceptions.TIdNotDefined notDefined)

//SPECIFIC

    /** TNotFound is thrown if the folder tree with specified owner is not found.*/
    Diagram.TFolder getFolderTree(1: string username) throws (1: DbExceptions.TNotFound e);

    /** Create link to specified folder in Shared folder of username's root.*/
    void shareFolderTo(1: string username, 2: Diagram.TFolder folder);
}

