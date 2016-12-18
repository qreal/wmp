<%@ include file="../../include/properties.jsp" %>

<div id="diagram-area" class="unselectable" ng-controller="BPMNDiagramEditorController">
    <!-- Context menu in File->'Open diagram' window -->
    <ul id="open-diagram-context-menu" class='custom-menu'>
        <li data-action="delete">Delete</li>
    </ul>

    <!-- Scene's elements context menu -->
    <ul id="scene-context-menu" class='custom-menu'>
        <li data-action="delete">Delete</li>
    </ul>

    <!-- Toolbars -->
    <div id="main-toolbar-area" class="navbar navbar-inverse navbar-static-top">
        <div class="container-fluid">
            <div id="go-to-dashboard-area" class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<c:url value="${dashboardService}"/>">Dashboard</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul id="tool-buttons-left-area" class="nav navbar-nav">
                    <li id="file-menu" class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">File<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="" role="menuitem" tabindex="-1" ng-click="createNewDiagram()">New</a></li>
                            <li><a href="" role="menuitem" tabindex="-1" data-toggle="modal" data-target="#diagrams"
                                   ng-click="openFolderWindow()">Open</a></li>
                            <li><a href="" role="menuitem" tabindex="-1" ng-click="saveCurrentDiagram()">Save</a></li>
                            <li><a href="" role="menuitem" tabindex="-1" data-toggle="modal" data-target="#diagrams"
                                   ng-click="saveDiagramAs()">SaveAs</a></li>
                        </ul>
                    </li>
                    <li>
                        <p id="undo-toolbar-button" class="navbar-text" ng-click="undo()">
                            <span class="glyphicon glyphicon-chevron-left" style="vertical-align: middle; cursor: pointer"></span>
                        </p>
                    </li>
                    <li>
                        <p id="redo-toolbar-button" class="navbar-text" ng-click="redo()">
                            <span class="glyphicon glyphicon-chevron-right" style="vertical-align: middle; cursor: pointer"></span>
                        </p>
                    </li>
                </ul>
                <ul id="tool-buttons-right-area" class="nav navbar-nav navbar-right">
                    <sec:authorize access="isAuthenticated()">
                        <li id="user-toolbar-menu-area" class="dropdown">
                            <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#">
                                <i class="glyphicon glyphicon-user"></i>
                                <sec:authentication property="name"/>
                                <span class="caret"></span>
                            </a>
                            <ul id="user-toolbar-menu" class="dropdown-menu" role="menu">
                                <li><a href="#">My Profile</a></li>
                            </ul>
                        </li>
                        <li id="logout-toolbar-area">
                            <c:url value="/j_spring_security_logout" var="logout"/>
                            <a href="${logout}">
                                <i class="glyphicon glyphicon-lock"></i>
                                Logout
                            </a>
                        </li>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </sec:authorize>
                </ul>

            </div>
        </div>
    </div>

    <!-- Main area -->
    <div id="main-editor-area" class="row unselectable">
        <div id="editor-and-property-editor-area" class="col-md-10 content-col">
            <div class="row sub-row">
                <div id="property-editor-area" class="col-md-3 content-col">
                    <div id="diagram-property-editor">
                        <legend style="padding: 10px">Property Editor</legend>
                        <table class="table table-condensed" id="property_table">
                            <thead>
                            <tr>
                                <th>Property</th>
                                <th>Value</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="diagram-" class="col-md-9 content-col">
                    <div class="scene-wrapper">
                        <div id="diagram-scene">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="palette-editor-area" class="col-md-2 content-col">
            <div id="editor-palette">
                <legend style="height: 40px; padding: 10px">Palette</legend>
                <div class = "search-input">
                    <b>Search</b><input id="elements-search" type="text">
                </div>
                <div id="elements-tree">
                    <ul id="palette-tabs" class="nav nav-tabs">
                        <li role="presentation" class="active">
                            <a href="#blocks" aria-controls="blocks" role="tab" data-toggle="tab">
                                Blocks
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="#flows" aria-controls="flows" role="tab" data-toggle="tab">
                                Flows
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="#subprograms" aria-controls="subprograms" role="tab" data-toggle="tab">
                                Subprograms
                            </a>
                        </li>
                    </ul>
                    <div id="palette-tab-content" class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="blocks">
                            <ul id="blocks-navigation"></ul>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="flows">
                            <ul id="flows-navigation"></ul>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="subprograms">
                            <ul id="subprograms-navigation"></ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- File->Open window -->
    <div id="diagrams" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">
                        Diagrams
                    </h4>
                </div>
                <div class="modal-body-nopadding">
                    <div class="folder-menu">
                    </div>
                    <div class="folder-path">
                    </div>
                    <div class="folder-view">
                        <ul class="folder-table">
                        </ul>
                    </div>
                    <div class="saving-menu">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Save confirmation window on creating new diagram -->
    <div id="confirm-save-diagram" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Create</h4>
                </div>
                <div class="modal-body">
                    <p>Do you want to save current diagram?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" id="saveAfterCreate" data-dismiss="modal">Yes</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" ng-click="clearAll()">No</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>

</div>