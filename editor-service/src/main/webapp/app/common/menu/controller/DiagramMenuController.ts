/// <reference path="../exporters/DiagramThriftExporter.ts" />
/// <reference path="../model/Folder.ts" />
/// <reference path="../model/DiagramMenuElement.ts" />
/// <reference path="../parsers/DiagramThriftParser.ts" />
/// <reference path="../../interfaces/editorCore.d.ts" />
/// <reference path="../../../vendor.d.ts" />
/// <reference path="../../../../resources/thrift/editor/EditorService_types.d.ts" />
/// <reference path="../../../../resources/thrift/struct/Diagram_types.d.ts" />
/// <reference path="../../../../resources/thrift/editor/EditorServiceThrift.d.ts" />
/// <reference path="../../../../resources/types/thrift/Thrift.d.ts" />
/// <reference path="../../../common/constants/GeneralConstants.ts" />
/// <reference path="../../../common/constants/MouseButton.ts" />

class DiagramMenuController {

    private diagramEditorController: DiagramEditorController;
    private diagramThriftExporter: DiagramThriftExporter;
    private diagramThriftParser: DiagramThriftParser;
    private currentDiagramName: string;
    private currentDiagramFolder: Folder;
    private canBeDeleted: boolean;
    private folderTree: Folder;
    private currentFolder: Folder;
    private contextMenuId = "open-diagram-context-menu";
    private selectedElement: DiagramMenuElement;

    constructor(diagramEditorController: DiagramEditorController) {
        this.diagramEditorController = diagramEditorController;
        this.diagramThriftExporter = new DiagramThriftExporter();
        this.diagramThriftParser = new DiagramThriftParser();
        this.currentDiagramName = "";
        this.currentDiagramFolder = null;
        this.canBeDeleted = false;

        var menuManager = this;
        var folderTree;
        try {
            folderTree = menuManager.getClient().getFolderTree();
            menuManager.folderTree = Folder.createFromDAO(folderTree, null);
            menuManager.currentFolder = menuManager.folderTree;
        }
        catch (e) {
            console.log("Error: can't get folder tree", e);
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
        $('#confirm-save-diagram').modal('show');
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
        this.showFolderMenu();
        this.showFolderTable(this.currentFolder);
        this.showSavingMenu();
    }

    public showCreatingMenu() {
        var menuManager = this;
        this.clearFolderMenu();
        $('.folder-menu').append(
            "<input type='text'>" +
            "<i id='creating'><span class='glyphicon glyphicon-ok' aria-hidden='true'></span></i>" +
            "<i id='cancel-creating'><span class='glyphicon glyphicon-remove'></span></i>");

        $('.folder-menu #creating').click(function() {
            menuManager.clearWarning('.folder-menu p');
            var folderName: string = $('.folder-menu input:text').val();
            if (folderName === "") {
                menuManager.writeWarning("Empty name", '.folder-menu');
            }  else if (menuManager.currentFolder.isChildExists(folderName)) {
                menuManager.writeWarning("The folder with this name already exists", '.folder-menu');
            } else {
                menuManager.createFolderInDatabase(folderName);
            }
        });

        $('.folder-menu #cancel-creating').click(function() {
            menuManager.showFolderMenu();
        });
    }

    public showSavingMenu(): void {
        var menuManager = this;
        this.clearSavingMenu();

        $('.saving-menu').append("<b>Input diagram name</b><input type:text>");
        $('#diagrams .modal-footer').prepend("<button id='saving' type='button' class='btn btn-success'>Save</button>");

        $('#saving').click(function() {
            menuManager.clearWarning('.saving-menu p');
            var diagramName: string = $('.saving-menu input:text').val();
            if (diagramName === "") {
                menuManager.writeWarning("Empty name", '.saving-menu');
            } else if (menuManager.currentFolder.isDiagramExists(diagramName)) {
                menuManager.writeWarning("The diagram with this name already exists", '.saving-menu');
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
        $('.folder-menu').append("<i id='level-up'><span class='glyphicon glyphicon-arrow-left'></span></i>");
        $('.folder-menu #level-up').click(function() {
            menuManager.levelUpFolder();
        });

        $('.folder-menu').append("<i id='creating-menu'><span class='glyphicon glyphicon-plus'></span></i>");
        $('.folder-menu #creating-menu').click(function() {
            menuManager.showCreatingMenu();
        });
    }

    private createFolderInDatabase(folderName: string): void {
        var menuManager = this;
        try {
            var folder = new TFolder();
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
                menuManager.diagramEditorController.clearState();
                menuManager.clearState();
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
                    menuManager.diagramEditorController.clearState();
                    menuManager.clearState();
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
            var diagramParts: DiagramParts = this.diagramThriftParser.parse(diagram, menuManager.diagramEditorController.getNodeTypes(),
                menuManager.diagramEditorController.getLinkPatterns());
            menuManager.diagramEditorController.addFromMap(diagramParts);
        }
        catch (ouch) {
            console.log("Error: can't open diagram");
        }
    }

    private deleteDiagramFromDatabase(diagramName: string): void {
        var menuManager = this;
        var transport = new Thrift.TXHRTransport(GeneralConstants.EDITOR_REST_SERVLET);
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
        $(place).append("<p class='warning-message'>" + message + "</p>");
    }

    private clearSavingMenu(): void {
        $('.saving-menu').empty();
        $('.modal-footer #saving').remove();
    }

    private clearFolderMenu(): void {
        $('.folder-menu').empty();
    }

    private clearFolderTable(): void {
        $('.folder-table li').remove();
    }

    private clearWarning(place: string): void {
        $(place).remove();
    }

    private showPathToFolder(): void {
        var path: string = "";

        $('.folder-path p').remove();

        var folder: Folder = this.currentFolder;
        while (folder.getParent()) {
            path = folder.getName() + "/" + path;
            folder = folder.getParent();
        }

        $('.folder-path').prepend("<p>" + path + "</p>");
    }

    private showFolderTable(openingFolder): void {
        var menuManager = this;
        this.clearFolderTable();
        this.currentFolder = openingFolder;
        var folderNames: string[] = this.currentFolder.getChildrenNames();
        var diagramNames: string[] = this.currentFolder.getDiagramNames();
        this.showPathToFolder();
        $.each(folderNames, function (i) {
            $('.folder-view ul').prepend("<li class='folders'>" +
                "<span class='glyphicon glyphicon-folder-open' aria-hidden='true'></span>" +
                "<span class='glyphicon-class'>" + folderNames[i] + "</span></li>");
        });
        $.each(diagramNames, function (i) {
            $('.folder-view ul').append("<li class='diagrams'>" +
                "<span class='glyphicon glyphicon-file' aria-hidden='true'></span>" +
                "<span class='glyphicon-class'>" + diagramNames[i] + "</span></li>");
        });

        $('.folder-table .folders').click(function () {
            menuManager.showFolderTable(menuManager.currentFolder.findChildByName($(this).text()));
        });
        $('.folder-table .diagrams').click(function () {
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
            if (event.button == MouseButton.right) {
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
                case "share":
                    if (controller.selectedElement.getType() === 'folders') {
                        console.log("Shared clicked")
                        controller.shareFolder(controller.selectedElement.getName());
                    }
                    break;
            }

            $("#" + controller.contextMenuId).hide(100);
        });
    }

    private getClient(): EditorServiceThriftClient {
        var transport = new Thrift.TXHRTransport(GeneralConstants.EDITOR_REST_SERVLET);
        var protocol = new Thrift.TJSONProtocol(transport);
        return new EditorServiceThriftClient(protocol);
    }

    public shareFolder(folderName: string) {
        console.log("Shared entered")
        var menuManager = this;
        $('#user-name-to-share-folder').empty();
        $('#enter-name-share-folder').modal('show');
        $('#name-share-folder-entered').click(function () {
            var name = $('.share-path input:text').val();
            var id = menuManager.currentFolder.findChildByName(folderName).getId();
            menuManager.getClient().addUserToOwners(id, name)
            $('#enter-name-share-folder').modal('hide');
        });
    }
}