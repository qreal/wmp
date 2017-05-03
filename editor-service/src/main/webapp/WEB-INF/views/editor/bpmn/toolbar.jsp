<%JSONObject headerPanelSelector = selectors.getJSONObject("editorHeaderPanel"); %>

<!-- Context menu in File->'Open diagram' window -->
<ul id="open-diagram-context-menu" class='custom-menu'>
    <li data-action="delete">Delete</li>
    <li data-action="share" >Share</li>
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
            <a id="<%=headerPanelSelector.getJSONObject("dashboardItem").getString("id")%>"
               class="navbar-brand" href="<c:url value="${dashboardService}"/>">Dashboard</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul id="tool-buttons-left-area" class="nav navbar-nav">
                <% JSONObject fileItemSelector = headerPanelSelector.getJSONObject("fileItem"); %>
                <li id="<%=fileItemSelector.getString("id")%>" class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">File<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a id="<%=fileItemSelector.getJSONObject("newDiagramItem").getString("id")%>"
                               href="" role="menuitem" tabindex="-1" ng-click="createNewDiagram()">New</a></li>
                        <li><a id="<%=fileItemSelector.getJSONObject("openItem").getString("id")%>"
                               href="" role="menuitem" tabindex="-1" ng-click="openFolderWindow()">Open</a></li>
                        <li><a id="<%=fileItemSelector.getJSONObject("saveItem").getString("id")%>"
                               href="" role="menuitem" tabindex="-1" ng-click="saveCurrentDiagram()">Save</a></li>
                        <li><a id="<%=fileItemSelector.getJSONObject("saveAsItem").getString("id")%>"
                               href="" role="menuitem" tabindex="-1" ng-click="saveDiagramAs()">SaveAs</a></li>
                    </ul>
                </li>
                <li>
                    <p id="undo-toolbar-button" class="navbar-text" ng-click="undo()">
                        <span class="glyphicon glyphicon-chevron-left"
                              style="vertical-align: middle; cursor: pointer"></span>
                    </p>
                </li>
                <li>
                    <p id="redo-toolbar-button" class="navbar-text" ng-click="redo()">
                        <span class="glyphicon glyphicon-chevron-right"
                              style="vertical-align: middle; cursor: pointer"></span>
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