<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Editor</title>

    <script src="<c:url value='/resources/js/jquery-1.11.3.min.js' />"></script>
    <script src="<c:url value='/resources/js/joint.js' />"></script>
    <script src="<c:url value='/resources/js/jquery.line.js' />"></script>
    <script src="<c:url value='/resources/js/jquery-ui.min.js' />"></script>
    <script src="<c:url value='/resources/js/angular.js' />"></script>
    <script src="<c:url value='/resources/js/context-menu.js' />"></script>
    <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js' />"></script>
    <script src="<c:url value='/resources/js/transform-to-element-fix.js' />"></script>

    <script src="<c:url value='/resources/js/utils.js' />"></script>
    <script src="<c:url value='/resources/js/diagram-core.js' />"></script>
    <script src="<c:url value='/resources/js/compiled/standalone-example.js' />"></script>

    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/joint.css' />"/>

    <link rel="stylesheet" href="<c:url value='/resources/treeview/jquery.treeview.css' />"/>
    <script type="text/javascript" src="<c:url value='/resources/treeview/jquery.treeview.js' />"></script>

    <link rel="stylesheet" href="<c:url value='/resources/css/jquery-ui.css' />"/>

    <link rel="stylesheet" href="<c:url value='/resources/css/context-menu.css' />" />
    <link rel="stylesheet" href="<c:url value='/resources/css/base.css' />"/>
</head>
<body ng-app>

<div id="diagramContent" class="unselectable" ng-controller="SimpleDiagramEditorController">
    <ul id="paper_context_menu" class='custom-menu'>
        <li data-action="delete">Delete</li>
    </ul>

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

</div>

</body>
</html>
