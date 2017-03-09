# Db-diagram service

This service provides CRUD interface to diagrams and folders.

## Internal and external representation of the data

Internal representation of diagram service data differs from external representation which is available for the first line services (e.g. editor-service, dashboard-service etc.). Within the db-diagram service there's a “many-to-many“ relationships between folders and users (a user can own multiple folders and a folder can have multiple owners), but for the rest of the system it's “one-to-many“ (only one owner for each folder). To support this every request to the db-diagram service starts with converting internal representation into external (using RelationMapping class) or vice versa. It is done the following way:

1. All directly transferred folders are converted to internal representation, fields are filled if it is possible.

1. If a folder has been saved before, the last saved copy is loaded and parents of this copy are added to parentFolders property, if they were not directly passed as request arguments.

1. If a folder hasn't been saved before and a folderParentId is set up, a parent with this given id is loaded, and it is added to the folder's parentFolders.  

Please note that we are not considering situations when a child folder exists in internal representation, but has not been presented in external one. Such situations would mean that a folder with a larger group of owners belongs to a folder with a smaller group of owners. And it does not make sense in this domain.

Converting internal representation to external one is a little bit easier because we don't add new information, only remove some parts of it. However, it has its own special features. While converting internal representation we need to know the id of the session (user name), because only in this case we are able to build “one-to-many” folder tree right. The conversion is done the following way:

1. All the fields except of parents and children are converted directly.

1. Among current folder's parents we look for the folder that has owner id equals to session id. Its id is assigned to the folderParentId field.

1. Children are converted to external representation using the same algorithm and added to childrenFolders field of external representation. The rest of converting process is done using trivial recursive algorithms.
