<%@ include file="../../include/include.jsp" %>
<html>
<head>
    <title>Robots diagram editor</title>

    <input type="hidden" id="selectorsTmp" value='${selectors}'/>
    <%JSONObject selectors = new JSONObject(request.getAttribute("selectors").toString());%>

    <jsp:include page="../../include/properties.jsp" flush="true"/>
    <jsp:include page="../../include/scripts.jsp" flush="true"/>


    <script src="<c:url value='/resources/thrift/struct/Diagram_types.js'/> "></script>
    <script src="<c:url value='/resources/thrift/editor/EditorService_types.js'/> "></script>
    <script src="<c:url value='/resources/thrift/editor/EditorServiceThrift.js'/> "></script>

    <link rel="stylesheet" href="<c:url value='/resources/css/bootstrap/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/joint/joint.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/treeview/jquery.treeview.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/jquery/jquery-ui.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/2dmodel/2dmodel.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/contex-menu/context-menu.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/base/base.css' />"/>

</head>

<body ng-controller="RootDiagramController">
<%@ include file="../../include/properties.jsp" %>

<div id="diagram-area" class="unselectable" ng-controller="RobotsDiagramEditorController">
    <%@ include file="toolbar.jsp" %>
    <%@ include file="../diagramContent.jsp" %>
</div>

<%@ include file="2dmodelContent.jsp" %>
</body>
</html>