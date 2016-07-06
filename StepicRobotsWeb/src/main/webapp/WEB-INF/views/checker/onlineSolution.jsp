<%@ include file="../include/include.jsp" %>

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

<html>
<head>
    <title>${name}</title>

    <jsp:include page="../include/scripts.jsp" flush="true"/>

    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/joint.css' />"/>

    <link rel="stylesheet" href="<c:url value='/resources/treeview/jquery.treeview.css' />"/>
    <script type="text/javascript" src="<c:url value='/resources/treeview/jquery.treeview.js' />"></script>

    <link rel="stylesheet" href="<c:url value='/resources/css/jquery-ui.css' />"/>

    <link rel="stylesheet" href="<c:url value='/resources/css/context-menu.css' />" />

    <link rel="stylesheet" href="<c:url value='/resources/css/onlineSolution.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/twoDModel.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/description.css' />"/>
</head>
<body>
<div ng-app ng-controller="RootDiagramController">
    <div class="container">
        <div id="titleContent" class="col-md-12">
            <%@ include file="description.jsp" %>
            <br>
        </div>
        <div class="col-md-9">
            <div id="diagramContent" class="row unselectable" ng-controller="StepicDiagramEditorController" task="${id}" kit="${kit}">
                <ul id="paper_context_menu" class='custom-menu'>
                    <li data-action="delete">Delete</li>
                </ul>
                <div class="paper-wrapper">
                    <div id="diagram_paper" zoom="0.8">
                        <div id="diagramSpinner" class="centerSpinner">
                        </div>
                    </div>
                </div>
                <button id="submit_button" class="btn btn-success btn-lg" type="button" ng-click="submit($scope)">
                    <spring:message code="label.submit"/>
                </button>
            </div>
            <%@ include file="twoDModelContent.jsp" %>
        </div>
        <div id="paletteContent" class="row unselectable">
            <div class="col-md-3">
                <div id="palette">
                    <div id="properties">
                        <legend style="padding: 10px"><spring:message code="label.propertyEditor"/></legend>
                        <table class="table table-condensed" id="property_table">
                            <thead>
                            <tr>
                                <th><spring:message code="label.property"/></th>
                                <th><spring:message code="label.value"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div id="elements">
                        <legend style="margin-bottom: 5px"><spring:message code="label.palette"/></legend>
                        <div id="elements_tree">
                            <ul id="palette-tabs" class="nav nav-tabs">
                                <li role="presentation" class="active"><a href="#blocks" aria-controls="blocks" role="tab"
                                                                          data-toggle="tab"><spring:message code="label.blocks"/></a></li>
                                <li role="presentation"><a href="#subprograms" aria-controls="subprograms" role="tab"
                                                           data-toggle="tab"><spring:message code="label.subprograms"/></a></li>
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
        </div>
    </div>
</div>
</body>
</html>
