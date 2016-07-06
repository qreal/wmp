<%--
  ~ Copyright Vladimir Zakharov
  ~ Copyright Anastasia Kornilova
  ~ Copyright Lidiya Chernigovskaya
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<div id="diagramContent" class="unselectable" ng-controller="RobotsDiagramEditorController">
    <ul id="diagram_menu_context_menu" class='custom-menu'>
        <li data-action="delete">Delete</li>
    </ul>
    <ul id="paper_context_menu" class='custom-menu'>
        <li data-action="delete">Delete</li>
    </ul>
    <div class="navbar navbar-inverse navbar-static-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<c:url value="/"/>">Dashboard</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li class="dropdown">
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
                        <p class="navbar-text" ng-click="openTwoDModel()">
                            <img src="images/2dmodel/2d-model.svg" style="width: 18px; height: 18px; cursor: pointer"/>
                        </p>
                    </li>
                    <li>
                        <p class="navbar-text" ng-click="undo()">
                            <span class="glyphicon glyphicon-chevron-left" style="vertical-align: middle; cursor: pointer"></span>
                        </p>
                    </li>
                    <li>
                        <p class="navbar-text" ng-click="redo()">
                            <span class="glyphicon glyphicon-chevron-right" style="vertical-align: middle; cursor: pointer"></span>
                        </p>
                    </li>
                </ul>

                <ul class="nav navbar-nav navbar-right">

                    <sec:authorize access="isAuthenticated()">

                        <li class="dropdown">
                            <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#"><i
                                    class="glyphicon glyphicon-user"></i>
                                <sec:authentication property="name"/>
                                <span class="caret"></span>
                            </a>
                            <ul id="g-account-menu" class="dropdown-menu" role="menu">
                                <li><a href="#">My Profile</a></li>
                            </ul>
                        </li>
                        <li>
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

    <div id="content-row" class="row unselectable">
        <div class="col-md-10 content-col">
            <div class="row sub-row">
                <div class="col-md-3 content-col">
                    <div id="diagram_left-menu">
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
                <div class="col-md-9 content-col">
                    <div class="paper-wrapper">
                        <div id="diagram_paper">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-2 content-col">
            <div id="diagram_right-menu">
                <legend style="height: 40px; padding: 10px">Palette</legend>
                <div id="elements_tree">
                    <ul id="palette-tabs" class="nav nav-tabs">
                        <li role="presentation" class="active"><a href="#blocks" aria-controls="blocks" role="tab"
                                                                  data-toggle="tab">Blocks</a></li>
                        <li role="presentation"><a href="#subprograms" aria-controls="subprograms" role="tab"
                                                   data-toggle="tab">Subprograms</a></li>
                    </ul>
                    <div id="palette-tab-content" class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="blocks">
                            <ul id="blocks-navigation">
                            </ul>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="subprograms">
                            <ul id="subprograms-navigation">
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="diagrams" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Diagrams</h4>
                </div>
                <div class="modal-body_nopadding">
                    <div class="folderMenu">
                    </div>
                    <div class="folderPath">
                    </div>
                    <div class="folderView">
                        <ul class="folderTable">
                        </ul>
                    </div>
                    <div class="savingMenu">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="confirmNew" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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