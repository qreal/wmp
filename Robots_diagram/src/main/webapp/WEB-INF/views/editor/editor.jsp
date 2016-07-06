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

<head>
    <title>Robots Diagram</title>

    <jsp:include page="../include/scripts.jsp" flush="true"/>

    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/joint.css' />"/>

    <link rel="stylesheet" href="<c:url value='/resources/treeview/jquery.treeview.css' />"/>
    <script type="text/javascript" src="<c:url value='/resources/treeview/jquery.treeview.js' />"></script>

    <link rel="stylesheet" href="<c:url value='/resources/css/jquery-ui.css' />"/>

    <link rel="stylesheet" href="<c:url value='/resources/css/2dmodel.css' />" />
    <link rel="stylesheet" href="<c:url value='/resources/css/context-menu.css' />" />
    <link rel="stylesheet" href="<c:url value='/resources/css/base.css' />"/>
</head>

<body ng-app ng-controller="RootDiagramController">
    <%@ include file="diagramContent.jsp" %>
    <%@ include file="2dmodelContent.jsp" %>
</body>
</html>