<%@ page import="org.codehaus.jettison.json.JSONObject" %>

<!-- Main area -->
<div id="main-editor-area" class="row unselectable">
    <div id="editor-and-property-editor-area" class="col-md-10 content-col">
        <div class="row sub-row">
            <div id="property-editor-area" class="col-md-3 content-col">
                <div id="diagram-property-editor">
                    <legend style="padding: 10px">Property Editor</legend>
                    <table class="table table-condensed" name="property_table"
                           id="<%=selectors.getJSONObject("propertyEditor").getString("id")%>">
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
            <div class="search-input">
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
<% JSONObject saveConfirmSelector = selectors
        .getJSONObject("editorHeaderPanel")
        .getJSONObject("saveDiagramConfirmWindow"); %>

<div id="<%=saveConfirmSelector.getString("id")%>"
     class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
                <button id="<%=saveConfirmSelector.getJSONObject("confirm").getString("id")%>"
                        type="button" class="btn btn-default" id="saveAfterCreate" data-dismiss="modal">Yes</button>
                <button id="<%=saveConfirmSelector.getJSONObject("cancel").getString("id")%>"
                        type="button" class="btn btn-default" data-dismiss="modal" ng-click="clearAll()">No</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>

<!-- Window for sharing -->
<div id="enter-name-share-folder" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">
                    Share
                </h4>
            </div>
            <div class="modal-body-nopadding">
                <div class="share-path">
                    <b id="user-name-to-share-folder">Input name</b><input type:text>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" id="name-share-folder-entered">Yes
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>
