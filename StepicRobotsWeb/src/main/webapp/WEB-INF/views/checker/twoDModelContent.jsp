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

<div id="twoDModelContent" class="row unselectable" ng-controller="StepicTwoDModelEngineFacade">
    <div id="twoDModel_stage" interactive="false" task="${taskId}" zoom="${2 / 3}">
    </div>
    <div id="twoDModelSpinner" class="centerSpinner">
    </div>
    <div id="infoAlert" class="alert fade in">
        <a href="" class="close" aria-label="close">&times;</a>
    </div>
    <div id="sayAlert" class="alert alert-info fade in">
    </div>

    <button id="menu_button" type="button" class="btn btn-default" ng-click="showDisplay()">
        <span class="glyphicon glyphicon-modal-window" aria-hidden="true"></span>
    </button>

    <div id="scroll_buttons">
        <button id="follow_button" class="btn btn-default scroll_button" data-toggle="button" aria-pressed="false" ng-click="followRobot()">
            <img class="small_image" src="<c:url value='/images/2dmodel/2d_target.png' />"/>
        </button>
    </div>

    <img id="controller" src="<c:url value='/images/2dmodel/trikKit/controller.png' />" />

    <span id="port_M1" class="port_name">M1</span>
    <span id="port_M2" class="port_name">M2</span>
    <span id="port_M3" class="port_name">M3</span>
    <span id="port_M4" class="port_name">M4</span>

    <span id="close_display" class="glyphicon glyphicon-remove-circle" aria-hidden="true" ng-click="closeDisplay()"></span>
    <canvas id="display" width="218" height="274"></canvas>
    <div id="led"></div>

    <button id="stop_button" class="btn btn-danger btn-lg" type="button" ng-click="stopPlay()">
        <spring:message code="label.stop"/>
    </button>
</div>
