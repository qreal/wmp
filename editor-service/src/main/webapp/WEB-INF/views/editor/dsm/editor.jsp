<%@ include file="../../include/include.jsp" %>

<head>
  <title>DSM platform</title>

  <%@ include file="../../include/properties.jsp" %>

  <jsp:include page="../../include/scripts.jsp" flush="true"/>
  <jsp:include page="../../include/properties.jsp" flush="true"/>

  <script src="<c:url value='/resources/thrift/struct/Diagram_types.js'/> "></script>
  <script src="<c:url value='/resources/thrift/struct/Palette_types.js'/> "></script>
  <script src="<c:url value='/resources/thrift/editor/EditorService_types.js'/> "></script>
  <script src="<c:url value='/resources/thrift/editor/EditorServiceThrift.js'/> "></script>
  <script src="<c:url value='/resources/thrift/editor/PaletteService_types.js'/> "></script>
  <script src="<c:url value='/resources/thrift/editor/PaletteServiceThrift.js'/> "></script>
  <script src="<c:url value='/resources/js/libs/jquery/treeview/jquery.treeview.js' />"></script>

  <link rel="stylesheet" href="<c:url value='/resources/css/bootstrap/bootstrap.min.css' />"/>
  <link rel="stylesheet" href="<c:url value='/resources/css/joint/joint.css' />"/>
  <link rel="stylesheet" href="<c:url value='/resources/css/treeview/jquery.treeview.css' />"/>
  <link rel="stylesheet" href="<c:url value='/resources/css/jquery/jquery-ui.css' />"/>
  <link rel="stylesheet" href="<c:url value='/resources/css/contex-menu/context-menu.css' />" />
  <link rel="stylesheet" href="<c:url value='/resources/css/base/base.css' />"/>
</head>

<body ng-app>

<div id="diagram-area" class="unselectable" ng-controller="PaletteDiagramEditorController">
  <%@ include file="toolbar.jsp" %>
  <%@ include file="../diagramContent.jsp" %>

  <div id="palettesMenu" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 class="modal-title">
            Palettes
          </h4>
        </div>
        <div class="modal-body-nopadding">
          <div class="palette-view">
            <ul class="palette-table">
            </ul>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
