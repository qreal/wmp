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
    <title>Tasks</title>

    <script src="<c:url value='/resources/js/jquery-1.11.3.min.js' />"></script>
    <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js' />"></script>

    <script type="text/javascript">
        $('body').on('click', 'a.disabled', function(event) {
            event.preventDefault();
        });
        $('body').on('click', 'li.disabled', function(event) {
            event.preventDefault();
        });
    </script>

    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/tasks.css' />"/>
</head>
<body>
    <div class="container">
        <div class="col-md-9 col-centered">
            <h2><spring:message code="label.tasks"/></h2>
            <br>

            <div style="text-align: left;"><spring:message code="label.language"/>:
                <a href="tasks?kit=${kit}&locale=ru"><img class="language" src="<c:url value='/resources/css/images/Russia.png' />"/></a>
                <a href="tasks?kit=${kit}&locale=en"><img class="language" src="<c:url value='/resources/css/images/United-Kingdom.png' />"/></a>
            </div>
            <c:if test="${not empty tasks}">
                <div id="taskList" class="list-group">
                    <c:forEach var="task" items="${tasks}">
                        <c:set var="taskName" value="${taskNames.get(task.getId())}"/>
                        <div class="list-group-item clearfix">
                            <span class="pull-left">
                                ${task.getTitle()}. ${taskName}
                            </span>
                            <span class="pull-right">
                                <c:if test="${task.isOnline()}">
                                    <a class="btn listButton"
                                        href="online/${task.getId()}?kit=${kit}&name=${taskName}&title=${task.getTitle()}">
                                        <spring:message code="label.online"/></a>
                                </c:if>
                                <a class="btn listButton"
                                    href="offline/${task.getId()}?kit=${kit}&name=${taskName}&title=${task.getTitle()}">
                                    <spring:message code="label.offline"/></a>
                            </span>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
