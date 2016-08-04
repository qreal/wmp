<%@ include file="../include/properties.jsp" %>

<div id="two-d-model-area" class="unselectable" ng-controller="RobotsTwoDModelEngineFacade" style="display: none">
    <ul id="two-d-model-scene-context-menu" class='custom-menu'>
        <li data-action="delete">Delete</li>
    </ul>

    <!-- Toolbar -->
    <div id="two-d-model-toolbar-area" class="navbar navbar-inverse navbar-static-top">
        <div class="container-fluid">

            <!-- Go to dashboard button -->
            <div id="two-d-model-toolbar-dashboard-button" class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"></button>
                <a class="navbar-brand" href="<c:url value="${dashboardService}"/>">Dashboard</a>
            </div>
            <div class="navbar-collapse collapse">

                <!-- Go to editor button -->
                <ul id="two-d-model-toolbar-back-button" class="nav navbar-nav">
                    <li>
                        <p class="navbar-text" ng-click="openDiagramEditor()">
                            <span id="back" class="glyphicon glyphicon-arrow-left" style="vertical-align: middle; cursor: pointer"></span>
                        </p>
                    </li>
                </ul>

                <!-- Profile and logout buttons -->
                <ul class="nav navbar-nav navbar-right">
                    <sec:authorize access="isAuthenticated()">
                        <li id="two-d-model-toolbar-profile-button" class="dropdown">
                            <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#">
                                <i class="glyphicon glyphicon-user"></i>
                                <sec:authentication property="name"/>
                                <span class="caret"></span>
                            </a>
                            <ul id="g-account-menu" class="dropdown-menu" role="menu">
                                <li><a href="#">My Profile</a></li>
                            </ul>
                        </li>
                        <li id="two-d-model-toolbar-logout button">
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
    <div id="two-d-model-main-area" >

        <!-- Clear scene confirmation window -->
        <div id="two-d-model-clear-confirmation-window" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Clear scene</h4>
                    </div>
                    <div class="modal-body">
                        <p>Do you really want to clear the scene?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" id="confirm">Yes</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Left tool window -->
        <div id="two-d-model-left-window">

            <!-- Tabs area -->
            <div id="two-d-model-left-tabs-area" class="tabbable">
                <ul id="two-d-model-left-tabs-headers" class="nav nav-tabs">
                    <li class="active"><a href="#two-d-model-left-tools-tab" data-toggle="tab">Tools</a></li>
                    <li><a href="#two-d-model-left-ports-tab" data-toggle="tab">Ports</a></li>
                    <li><a href="#two-d-model-left-settings-tab" data-toggle="tab">Model settings</a></li>
                </ul>
                <div id="two-d-model-left-tabs-content">
                    <div class="tab-content">

                        <!-- Tools tab -->
                        <div id="two-d-model-left-tools-tab" class="tab-pane active">
                            <!-- Buttons -->
                            <table class="two-d-model-table">
                                <tr>
                                    <td>
                                        <button class="two-d-model-button palette-left-button" ng-click="setDrawLineMode()">
                                            <img src="images/2dmodel/2d_ruler.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                    <td>
                                        <button id="wall-button" class="two-d-model-button palette-right-button"
                                                ng-click="setDrawWallMode()">
                                            <img src="images/2dmodel/2d_wall.png" style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <button class="two-d-model-button palette-left-button" ng-click="setDrawPencilMode()">
                                            <img src="images/2dmodel/2d_pencil.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                    <td>
                                        <button class="two-d-model-button palette-right-button" ng-click="setDrawEllipseMode()">
                                            <img src="images/2dmodel/2d_ellipse.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <button class="two-d-model-button palette-left-button" data-toggle="modal" data-target="#two-d-model-clear-confirmation-window">
                                            <img src="images/2dmodel/2d_clear.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                    <td>
                                        <button class="two-d-model-button palette-right-button" ng-click="setNoneMode()">
                                            <img src="images/2dmodel/2d_none.png"
                                                 style="width: 13px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                </tr>
                            </table>

                            <p style="margin-top: 10px;">Pen</p>

                            <!-- Pen settings -->
                            <div id="pen-settings">
                                <p><b>Width</b></p>
                                <p><input id="pen-width-spinner" type="number" class="spinner two-d-model-spinner" value="6"></p>
                                <p><b>Color</b></p>
                                <p>
                                    <select id="pen-color-dropdown" class="two-d-model-dropdown">
                                        <option selected="selected" value="black">black</option>
                                        <option value="blue">blue</option>
                                        <option value="green">green</option>
                                        <option value="yellow">yellow</option>
                                        <option value="red">red</option>
                                    </select>
                                </p>
                            </div>
                        </div>

                        <!-- Ports tab -->
                        <div class="tab-pane" id="two-d-model-left-ports-tab">
                            <p>Ports configuration</p>
                            <p><div id="configurationDropdowns"></div></p>
                        </div>

                        <!-- Model settings tab -->
                        <div class="tab-pane" id="two-d-model-left-settings-tab">
                            <p>Model settings</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Control buttons area -->
            <div id="two-d-mpde-left-control-buttons-area">
                <table class="two-d-model-table">
                    <tr>
                        <td colspan="2">
                            <button class="two-d-model-button" style="width: 204px; height: 70px;" ng-click="start()">
                                <img src="images/2dmodel/2d_run.png"
                                     style="width: 40px; height: 40px; vertical-align: middle"/>
                            </button>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <button id="follow-robot-button" class="two-d-model-button btn btn-default" data-toggle="button"
                                    ng-click="followRobot()" aria-pressed="false" style="width: 100px; height: 30px;">
                                <img src="images/2dmodel/2d_target.png"
                                     style="width: 20px; height: 20px; vertical-align: middle"/>
                            </button>
                        </td>
                        <td>
                            <button class="two-d-model-button btn btn-default" ng-click="resetPosition()"
                                    style="width: 100px; height: 30px;">
                                <img src="images/2dmodel/2d_robot_back.png"
                                     style="width: 20px; height: 20px; vertical-align: middle"/>
                            </button>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <button class="two-d-model-button btn btn-default" data-toggle="button"
                                    ng-click="stop()" aria-pressed="false" style="width: 100px; height: 30px;">
                                <img src="images/2dmodel/2d_stop.png"
                                     style="width: 20px; height: 20px; vertical-align: middle"/>
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <!-- Main scene area -->
        <div id="two-d-model-scene-area">
            <div id="sayAlert" class="alert alert-info fade in">
            </div>
        </div>

        <!-- Left controller model window -->
        <div id="two-d-model-right-window">
            <button id="hide-controller-model-button" type="button" class="btn btn-default" ng-click="showDisplay()">
                <span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span>
            </button>
            <span id="hide-controller-model-area" class="glyphicon glyphicon-remove-circle" aria-hidden="true" ng-click="closeDisplay()"></span>

            <img id="controller" src="<c:url value='/images/2dmodel/trikKit/controller.png' />" />

            <span id="port-M1" class="port-name">M1</span>
            <span id="port-M2" class="port-name">M2</span>
            <span id="port-M3" class="port-name">M3</span>
            <span id="port-M4" class="port-name">M4</span>

            <canvas id="display" width="218" height="274"></canvas>
            <div id="led"></div>
        </div>
    </div>
</div>
