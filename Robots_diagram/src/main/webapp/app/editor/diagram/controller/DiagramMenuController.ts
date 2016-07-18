/// <reference path="exporters/DiagramThriftExporter.ts" />
/// <reference path="../model/Diagram.ts" />
/// <reference path="../model/Folder.ts" />
/// <reference path="../model/DiagramMenuElement.ts" />
/// <reference path="../../interfaces/diagramCore.d.ts" />
/// <reference path="../../interfaces/vendor.d.ts" />
/// <reference path="../../../../resources/thrift/editor/editorService_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/EditorServiceThrift.d.ts" />
/// <reference path="../../../../resources/types/thrift/Thrift.d.ts" />

class DiagramMenuController {

    private diagramEditorController: RobotsDiagramEditorController;
    private diagramThriftExporter: DiagramThriftExporter;
    private currentDiagramName: string;
    private currentDiagramFolder: Folder;
    private canBeDeleted: boolean;
    private folderTree: Folder;
    private currentFolder: Folder;
    private contextMenuId = "diagram_menu_context_menu";
    private selectedElement: DiagramMenuElement;

    constructor(diagramEditorController: RobotsDiagramEditorController) {
        this.diagramEditorController = diagramEditorController;
        this.diagramThriftExporter = new DiagramThriftExporter();
        this.currentDiagramName = "";
        this.currentDiagramFolder = null;
        this.canBeDeleted = false;

        var menuManager = this;
        try {
            var folderTree = menuManager.getClient().getFolderTree();
            menuManager.folderTree = Folder.createFromDAO(folderTree, null);
            menuManager.currentFolder = menuManager.folderTree;
        }
        catch (ouch) {
            console.log("Error: can't get folder tree");
        }

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
        try {
            var folder = new FolderDAO();
            folder.folderName = folderName;
            folder.folderParentId = menuManager.currentFolder.getId();
            var folderId = menuManager.getClient().createFolder(folder);
            menuManager.currentFolder.addChild(
                new Folder(folderId, folderName, menuManager.currentFolder));
            menuManager.showFolderMenu();
            menuManager.showFolderTable(menuManager.currentFolder);
        }
        catch (ouch) {
            console.log("Error: can't create folder");
        }
    }

    private saveDiagramInDatabase(diagramName: string): void {
        var menuManager = this;
        this.currentDiagramName = diagramName;
        this.currentDiagramFolder = this.currentFolder;
        try {
            var diagram = this.diagramThriftExporter.exportSavingDiagramState(this.diagramEditorController.getGraph(),
                this.diagramEditorController.getDiagramParts(), diagramName, this.currentFolder.getId());
            var diagramId = menuManager.getClient().saveDiagram(diagram);
            menuManager.currentFolder.addDiagram(new Diagram(diagramId, diagramName));
            menuManager.currentFolder = menuManager.folderTree;
            $('#diagrams').modal('hide');
            if (menuManager.canBeDeleted) {
                menuManager.diagramEditorController.clearAll();
            }
        }
        catch (ouch) {
            console.log("Error: can't save diagram");
        }
    }

    private updateCurrentDiagramInDatabase(): void {
        var menuManager = this;
        if (this.currentDiagramName === "") {
            $('#diagrams').modal('show');
            this.saveDiagramAs();
        } else {
            try {
                var diagram = this.diagramThriftExporter.exportUpdatingDiagramState(this.diagramEditorController.getGraph(),
                    this.diagramEditorController.getDiagramParts(), this.currentDiagramName, this.currentDiagramFolder);
                menuManager.getClient().rewriteDiagram(diagram);
                if (menuManager.canBeDeleted) {
                    menuManager.diagramEditorController.clearAll();
                }
            }
            catch (ouch) {
                console.log("Error: can't update diagram");
            }
        }
    }

    private openDiagramFromDatabase(diagramName: string): void {
        var menuManager = this;
        this.currentDiagramName = diagramName;
        this.currentDiagramFolder = this.currentFolder;
        this.currentFolder = this.folderTree;
        try {
            var diagram = menuManager.getClient().openDiagram(menuManager.currentDiagramFolder.getDiagramIdByName(diagramName));
            menuManager.diagramEditorController.clearState();
            menuManager.diagramEditorController.handleLoadedDiagramJson(diagram);
        }
        catch (ouch) {
            console.log("Error: can't open diagram");
        }
    }

    private deleteDiagramFromDatabase(diagramName: string): void {
        var menuManager = this;
        var transport = new Thrift.TXHRTransport("http://localhost:8080/Robots_diagram/editorService");
        var protocol  = new Thrift.TJSONProtocol(transport);
        var client    = new EditorServiceThriftClient(protocol);
        try {
            client.deleteDiagram(menuManager.currentFolder.getDiagramIdByName(diagramName));
            menuManager.currentFolder.deleteDiagramByName(diagramName);
            menuManager.showFolderTable(menuManager.currentFolder);

            if (diagramName === menuManager.currentDiagramName
                && menuManager.currentFolder === menuManager.currentDiagramFolder) {
                menuManager.clearState();
            }
        }
        catch (ouch) {
            console.log("Error: can't delete diagram");
        }
    }

    private deleteFolderFromDatabase(folderName: string) {
        var menuManager = this;
        try {
            menuManager.getClient().deleteFolder(menuManager.currentFolder.findChildByName(folderName).getId());
            menuManager.currentFolder.deleteChildByName(folderName);
            menuManager.showFolderTable(menuManager.currentFolder);
        }
        catch (ouch) {
            console.log("Error: can't delete folder");
        }
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

    private getClient(): EditorServiceThriftClient {
        var transport = new Thrift.TXHRTransport("http://localhost:8080/Robots_diagram/editorService");
        var protocol = new Thrift.TJSONProtocol(transport);
        return new EditorServiceThriftClient(protocol);
    }
}