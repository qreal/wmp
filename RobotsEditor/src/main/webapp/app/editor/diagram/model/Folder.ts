class Folder {

    private id: number;
    private name: string;
    private parent: Folder;
    private childrenFolders: Folder[];
    private diagrams: Diagram[];

    public static createFromDAO(folder: TFolder, parent: Folder): Folder {
        var diagrams: Diagram[] = [];
        for (var i = 0; i < folder.diagrams.length; i++) {
            diagrams.push(Diagram.createFromDAO(folder.diagrams[i]));
        }
        var resultFolder: Folder = new Folder(folder.id, folder.folderName, parent, diagrams);

        for (var i = 0; i < folder.childrenFolders.length; i++) {
            resultFolder.addChild(Folder.createFromDAO(folder.childrenFolders[i], resultFolder));
        }

        return resultFolder;
    }

    constructor(id: number, name: string, parent: Folder, diagrams: Diagram[]= [], childrenFolders: Folder[] = []) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.diagrams = diagrams;
        this.childrenFolders = childrenFolders;
    }

    public getChildrenNames(): string[] {
        var folderNames: string[] = [];
        for (var i = 0; i < this.childrenFolders.length; i++) {
            folderNames.push(this.childrenFolders[i].getName());
        }

        return folderNames;
    }

    public getDiagramNames(): string[] {
        var diagramNames: string[] = [];
        for (var i = 0; i < this.diagrams.length; i++) {
            diagramNames.push(this.diagrams[i].getName());
        }

        return diagramNames;
    }

    public findChildByName(childName: string): Folder {
        for (var i = 0; i < this.childrenFolders.length; i++) {
            if (this.childrenFolders[i].getName() === childName) {
                return this.childrenFolders[i];
            }
        }
        return null;
    }

    public getDiagramIdByName(diagramName: string): number {
        for (var i = 0; i < this.diagrams.length; i++) {
            if ( this.diagrams[i].getName() === diagramName) {
                return  this.diagrams[i].getId();
            }
        }

        return -1;
    }

    public isDiagramExists(diagramName: string): boolean {
        for (var i = 0; i < this.diagrams.length; i++) {
            if (this.diagrams[i].getName() === diagramName) {
                return true;
            }
        }

        return false;
    }

    public isChildExists(folderName: string): boolean {
        for (var i = 0; i < this.childrenFolders.length; i++) {
            if (this.childrenFolders[i].getName() === folderName) {
                return true;
            }
        }

        return false;
    }

    public deleteChildByName(folderName: string): void {
        this.childrenFolders = this.childrenFolders.filter((folder) => !(folder.getName() === folderName));
    }

    public deleteDiagramByName(diagramName: string): void {
        this.diagrams = this.diagrams.filter((diagram) => !(diagram.getName() === diagramName));
    }

    public setId(id: number): void {
        this.id = id;
    }

    public getId(): number {
        return this.id;
    }

    public setName(name: string): void {
        this.name = name;
    }

    public getName(): string {
        return this.name;
    }

    public getParent(): Folder {
        return this.parent;
    }

    public addChild(folder: Folder): void {
        this.childrenFolders.push(folder);
    }

    public getChildren(): Folder[] {
        return this.childrenFolders;
    }

    public addDiagram(diagram: Diagram): void {
        this.diagrams.push(diagram);
    }

    public getDiagrams(): Diagram[] {
        return this.diagrams;
    }

}