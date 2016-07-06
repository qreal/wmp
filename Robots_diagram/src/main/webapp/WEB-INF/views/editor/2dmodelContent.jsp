<%--
  ~ Copyright Vladimir Zakharov
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

<div id="twoDModelContent" class="unselectable" ng-controller="RobotsTwoDModelEngineFacade" style="display: none">
    <ul id="twoDModel_stage_context_menu" class='custom-menu'>
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
                    <li>
                        <p class="navbar-text" ng-click="openDiagramEditor()">
                            <span id="back" class="glyphicon glyphicon-arrow-left" style="vertical-align: middle; cursor: pointer"></span>
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

    <div id="container" >
        <div class="modal fade" id="confirmDelete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Clear scene</h4>
                    </div>
                    <div class="modal-body">
                        <p>Do you really want to clear scene?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" id="confirm">Yes</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="twoDModel_left-menu">
            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#pallete_tab" data-toggle="tab">Tools</a></li>
                    <li><a href="#ports_tab" data-toggle="tab">Ports</a></li>
                    <li><a href="#settings_tab" data-toggle="tab">Model settings</a></li>
                </ul>
                <div id="twoDModel_palette">
                    <div class="tab-content">
                        <div class="tab-pane active" id="pallete_tab">
                            <table class="twoDModel_table">
                                <tr>
                                    <td>
                                        <button class="twoDModel_button palette_button" ng-click="setDrawLineMode()">
                                            <img src="images/2dmodel/2d_ruler.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                    <td>
                                        <button id="wall-button" class="twoDModel_button palette_button_right"
                                                ng-click="setDrawWallMode()">
                                            <img src="images/2dmodel/2d_wall.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <button class="twoDModel_button palette_button" ng-click="setDrawPencilMode()">
                                            <img src="images/2dmodel/2d_pencil.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                    <td>
                                        <button class="twoDModel_button palette_button_right" ng-click="setDrawEllipseMode()">
                                            <img src="images/2dmodel/2d_ellipse.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <button class="twoDModel_button palette_button" data-toggle="modal" data-target="#confirmDelete">
                                            <img src="images/2dmodel/2d_clear.png"
                                                 style="width: 20px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                    <td>
                                        <button class="twoDModel_button palette_button_right" ng-click="setNoneMode()">
                                            <img src="images/2dmodel/2d_none.png"
                                                 style="width: 13px; height: 20px; vertical-align: middle"/>
                                        </button>
                                    </td>
                                </tr>
                            </table>

                            <p style="margin-top: 10px;">Pen</p>

                            <div id="pen_settings">
                                <p><b>Width</b></p>
                                <p><input id="pen_width_spinner" type="number" class="spinner twoDModel_spinner" value="6"></p>
                                <p><b>Color</b></p>
                                <p>
                                    <select id="pen_color_dropdown" class="twoDModel_dropdown">
                                        <option selected="selected" value="black">black</option>
                                        <option value="blue">blue</option>
                                        <option value="green">green</option>
                                        <option value="yellow">yellow</option>
                                        <option value="red">red</option>
                                    </select>
                                </p>
                            </div>
                        </div>
                        <div class="tab-pane" id="ports_tab">
                            <p>Ports configuration</p>
                            <p>
                                <div id="configurationDropdowns"></div>
                            </p>
                        </div>
                        <div class="tab-pane" id="settings_tab">
                            <p>Model settings</p>
                        </div>
                    </div>
                </div>
            </div>
            <div id="controll_buttons_container">
                <table class="twoDModel_table">

                    <tr>
                        <td colspan="2">
                            <button class="twoDModel_button" style="width: 204px; height: 70px;" ng-click="start()">
                                <img src="images/2dmodel/2d_run.png"
                                     style="width: 40px; height: 40px; vertical-align: middle"/>
                            </button>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <button id="follow_button" class="twoDModel_button btn btn-default" data-toggle="button"
                                    ng-click="followRobot()" aria-pressed="false" style="width: 100px; height: 30px;">
                                <img src="images/2dmodel/2d_target.png"
                                     style="width: 20px; height: 20px; vertical-align: middle"/>
                            </button>
                        </td>
                        <td>
                            <button class="twoDModel_button btn btn-default" ng-click="resetPosition()"
                                    style="width: 100px; height: 30px;">
                                <img src="images/2dmodel/2d_robot_back.png"
                                     style="width: 20px; height: 20px; vertical-align: middle"/>
                            </button>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <button class="twoDModel_button btn btn-default" data-toggle="button"
                                    ng-click="stop()" aria-pressed="false" style="width: 100px; height: 30px;">
                                <img src="images/2dmodel/2d_stop.png"
                                     style="width: 20px; height: 20px; vertical-align: middle"/>
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div id="twoDModel_stage">
            <div id="sayAlert" class="alert alert-info fade in">
            </div>
        </div>

        <div id="twoDModel_right-menu">
            <button id="menu_button" type="button" class="btn btn-default" ng-click="showDisplay()">
                <span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span>
            </button>

            <img id="controller" src="<c:url value='/images/2dmodel/trikKit/controller.png' />" />

            <span id="port_M1" class="port_name">M1</span>
            <span id="port_M2" class="port_name">M2</span>
            <span id="port_M3" class="port_name">M3</span>
            <span id="port_M4" class="port_name">M4</span>

            <span id="close_display" class="glyphicon glyphicon-remove-circle" aria-hidden="true" ng-click="closeDisplay()"></span>
            <canvas id="display" width="218" height="274"></canvas>
            <div id="led"></div>
        </div>
    </div>
</div>
