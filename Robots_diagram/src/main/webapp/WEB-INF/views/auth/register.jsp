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

<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/error.css'/>"/>


</head>

<body>

<c:url value="/register" var="loginUrl"/>


<%@ include file="../include/navbar.jsp" %>

<div class="container-fluid">
    <section class="container">


        <form class="form-signin" action="${loginUrl}" method="post">
            <div class="container-page">
                <div class="col-md-6">
                    <h3 class="dark-grey">Registration</h3>

                    <div class="form-group col-lg-12">

                        <c:if test="${not empty error}">
                            <div class="error">${error}</div>
                        </c:if>
                        <c:if test="${not empty msg}">
                            <div class="msg">${msg}</div>
                        </c:if>
                    </div>

                    <div class="form-group col-lg-12">
                        <label>Username</label>
                        <input type="text" class="form-control" id="username" name="username" value="">
                    </div>

                    <div class="form-group col-lg-6">
                        <label>Password</label>
                        <input type="password" class="form-control" id="password" name="password" value="">
                    </div>

                    <div class="form-group col-lg-6">
                        <label>Repeat Password</label>
                        <input type="password" class="form-control" id="password2" name="password2" value="">
                    </div>


                    <div class="form-group col-lg-12">
                        <button type="submit" class="btn btn-primary pull-right">Register</button>
                    </div>


                    <input type="hidden"
                           name="${_csrf.parameterName}" value="${_csrf.token}"/>

                </div>
            </div>
        </form>
    </section>
</div>

</body>
</html>
