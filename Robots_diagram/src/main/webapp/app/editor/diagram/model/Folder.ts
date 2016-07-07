/*
 * Copyright Vladimir Zakharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class Folder {

    private id: number;
    private name: string;
    private parent: Folder;
    private childrenFolders: Folder[];
    private diagrams: Diagram[];

    public static createFromJson(folderJson: any, parent: Folder): Folder {
        var diagrams: Diagram[] = [];
        for (var i = 0; i < folderJson.diagrams.length; i++) {
            diagrams.push(Diagram.createFromJson(folderJson.diagrams[i]));
        }
        var resutlFolder: Folder = new Folder(folderJson.folderId, folderJson.folderName, parent, diagrams);

        for (var i = 0; i < folderJson.childrenFolders.length; i++) {
            resutlFolder.addChild(Folder.createFromJson(folderJson.childrenFolders[i], resutlFolder));
        }

        return resutlFolder;
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