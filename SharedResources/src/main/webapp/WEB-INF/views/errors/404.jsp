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
    <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js' />"></script>
    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/error_page.css' />" />
    <title>404 Not Found</title>
</head>
<body>
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <div class="error-template">
          <h1>Oops!</h1>
          <h2>404 Not Found</h2>
          <div class="error-details">
            Sorry, an error has occured, Requested page not found!
          </div>
          <div class="error-actions">
            <a href="<c:url value="/"/>" class="btn btn-primary btn-lg">
              <span class="glyphicon glyphicon-home"></span>Take Me Home
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
