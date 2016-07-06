<%@ include file="include.jsp" %>

<%--
  ~ Copyright Vladimir Zakharov
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

<link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>

<c:url value="/j_spring_security_logout" var="logout"/>
<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value="/"/>">Dashboard</a>
        </div>
        <div class="navbar-collapse collapse">

            <ul class="nav navbar-nav">

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Editor<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value="/editor"/>">Editor</a></li>
                    </ul>
                </li>

            </ul>

            <ul class="nav navbar-nav navbar-right">

                <sec:authorize access="isAuthenticated()">

                    <li class="dropdown">
                        <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#"><i
                                class="glyphicon glyphicon-user"></i>
                            <sec:authentication property="name"/>
                            <span class="caret"></span>
                        </a>
                        <ul id="g-account-menu" class="dropdown-menu" role="menu">
                            <li><a href="#">My Profile</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="${logout}">
                            <i class="glyphicon glyphicon-lock"></i>
                            Logout
                        </a>
                    </li>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </sec:authorize>
            </ul>

        </div>
    </div>
    <!-- /container -->
</div>
<!-- /Header -->