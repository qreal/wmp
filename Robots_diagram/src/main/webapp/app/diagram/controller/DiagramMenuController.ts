/*
 * Copyright Anastasia Kornilova
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

/// <reference path="exporters/RobotsDiagramExporter.ts" />
/// <reference path="../model/Diagram.ts" />
/// <reference path="../model/Folder.ts" />
/// <reference path="../model/DiagramMenuElement.ts" />
/// <reference path="../../diagramCore.d.ts" />
/// <reference path="../../vendor.d.ts" />

class DiagramMenuController {

    private diagramEditorController: RobotsDiagramEditorController;
    private diagramExporter: RobotsDiagramExporter;
    private currentDiagramName: string;
    private currentDiagramFolder: Folder;
    private canBeDeleted: boolean;
    private folderTree: Folder;
    private currentFolder: Folder;
    private contextMenuId = "diagram_menu_context_menu";
    private selectedElement: DiagramMenuElement;

    constructor(diagramEditorController: RobotsDiagramEditorController) {
        this.diagramEditorController = diagramEditorController;
        this.diagramExporter = new RobotsDiagramExporter();
        this.currentDiagramName = "";
        this.currentDiagramFolder = null;
        this.canBeDeleted = false;

        var menuManager = this;

        $.ajax({
            type: 'GET',
            url: 'getFolderTree',
            dataType: 'json',
            success: function (response, status, jqXHR) {
                menuManager.folderTree = Folder.createFromJson(response, null);
                menuManager.currentFolder = menuManager.folderTree;
            },
            error: function (response, status, error) {
                console.log("error: " + status + " " + error);
            }
        });

        $(document).ready(function() {
            $('.modal-footer button').click(function() {
                menuManager.currentFolder = menuManager.folderTree;
            });
            $('#saveAfterCreate').click(function () {
                menuManager.canBeDeleted = true;
                menuManager.updateCurrentDiagramInDatabase();
            });
        });
    }

    public createNewDiagram(): void {
        $('#confirmNew').modal('show');
    }

    public openFolderWindow(): void {
        this.showFolderMenu();
        this.showFolderTable(this.currentFolder);
        this.clearSavingMenu();
    }

    public saveCurrentDiagram(): void {
        if (this.currentDiagramName === "") {
            this.saveDiagramAs();
        } else {
            this.updateCurrentDiagramInDatabase();
        }
    }

    public saveDiagramAs(): void {
        $('#diagrams').modal('show');
        this.showFolderMenu();
        this.showFolderTable(this.currentFolder);
        this.showSavingMenu();
    }

    public showCreatingMenu() {
        var menuManager = this;
        this.clearFolderMenu();
        $('.folderMenu').append(
            "<input type='text'>" +
            "<i id='creating'><span class='glyphicon glyphicon-ok' aria-hidden='true'></span></i>" +
            "<i id='cancelCreating'><span class='glyphicon glyphicon-remove'></span></i>");

        $('.folderMenu #creating').click(function() {
            menuManager.clearWarning('.folderMenu p');
            var folderName: string = $('.folderMenu input:text').val();
            if (folderName === "") {
                menuManager.writeWarning("Empty name", '.folderMenu');
            }  else if (menuManager.currentFolder.isChildExists(folderName)) {
                menuManager.writeWarning("The folder with this name already exists", '.folderMenu');
            } else {
                menuManager.createFolderInDatabase(folderName);
            }
        });

        $('.folderMenu #cancelCreating').click(function() {
            menuManager.showFolderMenu();
        });
    }

    public showSavingMenu(): void {
        var menuManager = this;
        this.clearSavingMenu();

        $('.savingMenu').append("<b>Input diagram name</b><input type:text>");
        $('#diagrams .modal-footer').prepend("<button id='saving' type='button' class='btn btn-success'>Save</button>");

        $('#saving').click(function() {
            menuManager.clearWarning('.savingMenu p');
            var diagramName: string = $('.savingMenu input:text').val();
            if (diagramName === "") {
                menuManager.writeWarning("Empty name", '.savingMenu');
            } else if (menuManager.currentFolder.isDiagramExists(diagramName)) {
                menuManager.writeWarning("The diagram with this name already exists", '.savingMenu');
            } else {
                menuManager.saveDiagramInDatabase(diagramName);
            }
        });
    }

    public clearState(): void {
        this.canBeDeleted = false;
        this.currentDiagramName = "";
        this.currentDiagramFolder = null;
        this.selectedElement = null;
    }

    private showFolderMenu(): void {
        var menuManager = this;
        this.clearFolderMenu();
        $('.folderMenu').append("<i id='levelUp'><span class='glyphicon glyphicon-arrow-left'></span></i>");
        $('.folderMenu #levelUp').click(function() {
            menuManager.levelUpFolder();
        });

        $('.folderMenu').append("<i id='creatingMenu'><span class='glyphicon glyphicon-plus'></span></i>");
        $('.folderMenu #creatingMenu').click(function() {
            menuManager.showCreatingMenu();
        });
    }

    private createFolderInDatabase(folderName: string): void {
        var menuManager = this;
        $.ajax({
            type: 'POST',
            url: 'createFolder',
            dataType: 'text',
            contentType: 'application/json',
            data: JSON.stringify({
                'folderName': folderName,
                'folderParentId': menuManager.currentFolder.getId()
            }),
            success: function (createdFolderId, status, jqXHR): any {
                menuManager.currentFolder.addChild(
                    new Folder(createdFolderId, folderName, menuManager.currentFolder));
                menuManager.showFolderMenu();
                menuManager.showFolderTable(menuManager.currentFolder);
            },
            error: function (response, status, error): any {
                console.log("error: " + status + " " + error);
            }
        });
    }

    private saveDiagramInDatabase(diagramName: string): void {
        var menuManager = this;
        this.currentDiagramName = diagramName;
        this.currentDiagramFolder = this.currentFolder;
        $.ajax({
            type: 'POST',
            url: 'saveDiagram',
            dataType: 'text',
            contentType: 'application/json',
            data: JSON.stringify(
                menuManager.diagramExporter.exportSavingDiagramStateToJSON(this.diagramEditorController.getGraph(),
                this.diagramEditorController.getDiagramParts(), diagramName, this.currentFolder.getId())),
            success: function (diagramId, status, jqXHR): any {
                menuManager.currentFolder.addDiagram(new Diagram(diagramId, diagramName));
                menuManager.currentFolder = menuManager.folderTree;
                $('#diagrams').modal('hide');

                if (menuManager.canBeDeleted) {
                    menuManager.diagramEditorController.clearAll();
                }
            },
            error: function (response, status, error): any {
                console.log("error: " + status + " " + error);
            }
        });
    }

    private updateCurrentDiagramInDatabase(): void {
        var menuManager = this;
        if (this.currentDiagramName === "") {
            $('#diagrams').modal('show');
            this.saveDiagramAs();
        } else {
            $.ajax({
                type: 'POST',
                url: 'updateDiagram',
                dataType: 'text',
                contentType: 'application/json',
                data: JSON.stringify(
                    menuManager.diagramExporter.exportUpdatingDiagramStateToJSON(this.diagramEditorController.getGraph(),
                    this.diagramEditorController.getDiagramParts(), this.currentDiagramName, this.currentDiagramFolder)),
                success: function (response, status, jqXHR): any {
                    if (menuManager.canBeDeleted) {
                        menuManager.diagramEditorController.clearAll();
                    }
                },
                error: function (response, status, error): any {
                    console.log("error: " + status + " " + error);
                }
            });
        }
    }

    private openDiagramFromDatabase(diagramName: string): void {
        var menuManager = this;
        this.currentDiagramName = diagramName;
        this.currentDiagramFolder = this.currentFolder;
        this.currentFolder = this.folderTree;
        $.ajax({
            type: 'POST',
            url: 'openDiagram',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({id: menuManager.currentDiagramFolder.getDiagramIdByName(diagramName)}),
            success: function (response, status, jqXHR): any {
                menuManager.diagramEditorController.clearState();
                menuManager.diagramEditorController.handleLoadedDiagramJson(response);
            },
            error: function (response, status, error): any {
                console.log("error: " + status + " " + error);
            }
        });
    }

    private deleteDiagramFromDatabase(diagramName: string): void {
        var menuManager = this;
        $.ajax({
            type: 'POST',
            url: 'deleteDiagram',
            contentType: 'application/json',
            data: (JSON.stringify({id: menuManager.currentFolder.getDiagramIdByName(diagramName)})),
            success: function () {
                menuManager.currentFolder.deleteDiagramByName(diagramName);
                menuManager.showFolderTable(menuManager.currentFolder);

                if (diagramName === menuManager.currentDiagramName
                    && menuManager.currentFolder === menuManager.currentDiagramFolder) {
                    menuManager.clearState();
                }
            },
            error: function (response, status, error) {
                console.log("error: " + status + " " + error);
            }
        });
    }

    private  deleteFolderFromDatabase(folderName: string) {
        var menuManager = this;
        $.ajax({
            type: 'POST',
            url: 'deleteFolder',
            contentType: 'application/json',
            data: (JSON.stringify({id: menuManager.currentFolder.findChildByName(folderName).getId()})),
            success: function () {
                menuManager.currentFolder.deleteChildByName(folderName);
                menuManager.showFolderTable(menuManager.currentFolder);
            },
            error: function (response, status, error) {
                console.log("error: " + status + " " + error);
            }
        });
    }

    private writeWarning(message: string, place: string): void {
        $(place).append("<p class='warningMessage'>" + message + "</p>");
    }

    private clearSavingMenu(): void {
        $('.savingMenu').empty();
        $('.modal-footer #saving').remove();
    }

    private clearFolderMenu(): void {
        $('.folderMenu').empty();
    }

    private clearFolderTable(): void {
        $('.folderTable li').remove();
    }

    private clearWarning(place: string): void {
        $(place).remove();
    }

    private showPathToFolder(): void {
        var path: string = "";

        $('.folderPath p').remove();

        var folder: Folder = this.currentFolder;
        while (folder.getParent()) {
            path = folder.getName() + "/" + path;
            folder = folder.getParent();
        }

        $('.folderPath').prepend("<p>" + path + "</p>");
    }

    private showFolderTable(openingFolder): void {
        var menuManager = this;

        this.clearFolderTable();
        this.currentFolder = openingFolder;
        var folderNames: string[] = this.currentFolder.getChildrenNames();
        var diagramNames: string[] = this.currentFolder.getDiagramNames();
        this.showPathToFolder();
        $.each(folderNames, function (i) {
            $('.folderView ul').prepend("<li class='folders'>" +
                "<span class='glyphicon glyphicon-folder-open' aria-hidden='true'></span>" +
                "<span class='glyphicon-class'>" + folderNames[i] + "</span></li>");
        });
        $.each(diagramNames, function (i) {
            $('.folderView ul').append("<li class='diagrams'>" +
                "<span class='glyphicon glyphicon-file' aria-hidden='true'></span>" +
                "<span class='glyphicon-class'>" + diagramNames[i] + "</span></li>");
        });

        $('.folderTable .folders').click(function () {
            menuManager.showFolderTable(menuManager.currentFolder.findChildByName($(this).text()));
        });
        $('.folderTable .diagrams').click(function () {
            menuManager.openDiagramFromDatabase($(this).text());
            $('#diagrams').modal('hide');
        });

        this.initContextMenu();
    }

    private levelUpFolder(): void {
        if (this.currentFolder.getParent()) {
            this.showFolderTable(this.currentFolder.getParent());
        }
    }

    private initContextMenu(): void {
        var controller = this;

        $('#diagrams li').mouseup(function (event) {
            if (event.button == 2) {
                $("#" + controller.contextMenuId).finish().toggle(100)
                    .css({
                        top: event.pageY + "px",
                        left: event.pageX + "px"
                    });
                controller.selectedElement = new DiagramMenuElement($(this).text(), $(this).attr("class"));
            }
        });

        $("#" + controller.contextMenuId + " li").click(function () {
            switch($(this).attr("data-action")) {
                case "delete":
                    if (controller.selectedElement.getType() === 'diagrams') {
                        controller.deleteDiagramFromDatabase(controller.selectedElement.getName());
                    } else if (controller.selectedElement.getType() === 'folders') {
                        controller.deleteFolderFromDatabase(controller.selectedElement.getName());
                    }
                    break;
            }

            $("#" + controller.contextMenuId).hide(100);
        });
    }
    
}