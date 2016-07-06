<%@ include file="../include/include.jsp" %>
<%--
  ~ Copyright Denis Ageev
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

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Robots online</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script src="<c:url value='/resources/js/jquery-1.11.0.min.js' />"></script>
    <jsp:include page="../include/scripts.jsp"/>
    <script src="http://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
    <script src="<c:url value='/resources/js/map.js' />"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $("#target").click(function () {
                var robots = JSON.parse('${robots}');
                robots.forEach(function (robot) {
                    addLabel(robot);
                });
            });
        });
    </script>

</head>

<body>

<%@ include file="../include/navbar.jsp" %>

<div id="map" style="width: 100%; height: 600px"></div>
<button id="target">ADD LABEL</button>
</body>

</html>