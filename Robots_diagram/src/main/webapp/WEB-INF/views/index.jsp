<%@ include file="include/include.jsp" %>

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

<html lang="en">
<head>
    <title>Robots Diagram</title>

    <jsp:include page="include/scripts.jsp"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/error.css'/>"/>
    <script src="<c:url value='/resources/js/map.js' />"></script>
    <script src="<c:url value='/resources/js/robot.js' />"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#target").click(function () {
                var robots = JSON.parse('${robots}');
                robots.forEach(function (robot) {
                    addLabel(robot);
                });
            });

            $("#registerRobot").click(function () {
                var name = $('#robotName').val();
                var code = $('#ssid').val();
                var data = "robotName=" + name + "&ssid=" + code;
                $.ajax({
                    type: 'POST',
                    url: 'registerRobot',
                    data: data,
                    success: function (response) {
                        var result = JSON.parse(response);
                        if (result.status == "OK") {
                            location.reload();
                        } else {
                            $('#registerError').text(result.message);
                            $('#registerError').show();
                        }
                    },
                    error: function (response, status, error) {
                        console.log("error");
                    }
                });
            });

            $('[name="deleteRobot"]').click(function (event) {
                var robotName = event.target.id.substring(7);
                var data = "robotName=" + robotName;
                $.ajax({
                    type: 'POST',
                    url: 'deleteRobot',
                    data: data,
                    success: function (data) {
                        location.reload();
                    },
                    error: function (response, status, error) {
                        console.log("error");
                    }
                });
            });

            $('[name="sendDiagram"]').click(function (event) {
                var robotName = event.target.id.substring(13);
                var data = "robotName=" + robotName + "&program=" + "MUR MUR MUR";
                $.ajax({
                    type: 'POST',
                    url: 'sendDiagram',
                    data: data,
                    success: function (data) {
                        $('#sendDiagramModal-' + robotName).modal('hide');
                        alert("Successfully sent ");
                    },
                    error: function (response, status, error) {
                        console.log("error");
                    }
                });
            });


            $('[name="saveModelConfig"]').click(function (event) {
                var robotName = event.target.id.substring(18);
                var name = robotName + "-port";
                var o = [];
                $('[name="' + name + '"]').each(function (index, value) {
                    var obj = {};
                    var key = $(value).text();
                    var valueElementName = '[name="' + robotName + '-' + key + '"]';
                    var attr = $(valueElementName).attr("data-content");

                    if (typeof attr !== typeof undefined && attr !== false) {
                        obj[key] = $(valueElementName).attr("data-content");
                    } else {
                        obj[key] = $(valueElementName).text();
                    }
                    o.push(obj); // push in the "o" object created
                });

                var typeProperties = [];
                $('[name="propertyForm-' + robotName + '"]').each(function (index, value) {
                    var obj = {};
                    obj["type"] = $(value).attr("id").substring(5);
                    $(value).find(':input').each(function (index2, value2) {
                        obj[$(value2).attr("name")] = $(value2).val();
                    });
                    typeProperties.push(obj);
                });

                var data = "robotName=" + robotName + "&modelConfigJson=" + JSON.stringify(o) +
                        "&typeProperties=" + JSON.stringify(typeProperties);


                $.ajax({
                    type: 'POST',
                    url: 'saveModelConfig',
                    data: data,
                    success: function (response) {
                        var result = JSON.parse(response);
                        if (result.status == "OK") {
                            $('#configureRobotModal-' + robotName).modal('hide');
                            $('#validationError-' + robotName).hide();
                            alert("Successfully saved ");
                        } else {
                            $('#validationError-' + robotName).show();
                            $('#validationError-' + robotName).text(result.errors);
                        }
                    },
                    error: function (response, status, error) {
                        alert(JSON.parse(response).message);

                    }
                });
            });


            $("#configureMenu a").click(function (event) {
                event.preventDefault(); //prevent synchronous loading
                var id = event.target.id.substring(2);
                var option = $(this).text();
                if (option.length > 9) {
                    var diff = option.length - 9;
                    option = option.substring(0, option.length - diff);
                    $("#" + id).attr("data-content", $(this).text());
                }
                $("#" + id).html(option);
            });

            $("#configureDeviceMenu a").click(function (event) {
                event.preventDefault(); //prevent synchronous loading
                var robotName = event.target.id.substring(14);
                var option = $(this).text();
                $("#deviceType-" + robotName).attr("data-content", option);
                $("#deviceType-" + robotName).html(option);
                $('[name="propertyType-' + robotName + '"]').hide();
                $("#property-" + robotName + '-' + option).show();
            });

            $('[data-toggle="tooltip"]').tooltip({
                'placement': 'top'
            });
            $('[data-toggle="popover"]').popover({
                trigger: 'hover',
                'placement': 'top'
            });


            $(window).load(function () {
                $('[data-toggle="popover"]').each(function (index, value) {
                    var option = $(value).text();

                    if (option.length > 9) {
                        var diff = option.length - 9;
                        option = option.substring(0, option.length - diff);
                        $(value).attr("data-content", $(value).text());
                        $(value).text(option);
                    }
                });
            });


        });
    </script>
    <style>
        .dropdown-submenu {
            position: relative;
        }

        .dropdown-submenu > .dropdown-menu {
            top: 0;
            left: 100%;
            margin-top: -6px;
            margin-left: -1px;
            -webkit-border-radius: 0 6px 6px 6px;
            -moz-border-radius: 0 6px 6px;
            border-radius: 0 6px 6px 6px;
        }

        .dropdown-submenu:hover > .dropdown-menu {
            display: block;
        }

        .dropdown-submenu > a:after {
            display: block;
            content: " ";
            float: right;
            width: 0;
            height: 0;
            border-color: transparent;
            border-style: solid;
            border-width: 5px 0 5px 5px;
            border-left-color: #ccc;
            margin-top: 5px;
            margin-right: -10px;
        }

        .dropdown-submenu:hover > a:after {
            border-left-color: #fff;
        }

        .dropdown-submenu.pull-left {
            float: none;
        }

        .dropdown-submenu.pull-left > .dropdown-menu {
            left: -100%;
            margin-left: 10px;
            -webkit-border-radius: 6px 0 6px 6px;
            -moz-border-radius: 6px 0 6px 6px;
            border-radius: 6px 0 6px 6px;
        }
    </style>
    <link rel="stylesheet" href="<c:url value='/resources/bootstrap/css/bootstrap.min.css' />"/>

</head>

<body>

<%@ include file="include/navbar.jsp" %>


<!-- Main -->
<div class="container-fluid">


    <!-- upper section -->
    <div class="row">
        <div class="col-sm-3">
            <!-- left -->
            <h3><i class="glyphicon glyphicon-briefcase"></i> Toolbox</h3>
            <hr>

            <ul class="nav nav-stacked ">
                <li class="active"><a href="#robots" data-toggle="tab"><i class="glyphicon glyphicon-flash"></i> My
                    robots</a></li>
                <li><a href="#diagrams" data-toggle="tab"><i class="glyphicon glyphicon-link"></i> My
                    Diagrams</a></li>
                <li><a href="#map" data-toggle="tab"><i class="glyphicon glyphicon-list-alt"></i> Map</a>
                </li>
                <li><a href="#settings" data-toggle="tab"><i class="glyphicon glyphicon-book"></i>
                    Settings</a></li>
            </ul>

            <hr>

        </div>


        <div class="col-sm-9">


            <h3><i class="glyphicon glyphicon-dashboard"></i> Dashboard</h3>


            <hr>

            <div id="myTabContent" class="tab-content">
                <div class="tab-pane active in" id="robots">


                    <div class="row">
                        <!-- center left-->

                        <c:forEach var="robotWrapper" items="${robotsWrapper}">
                            <c:set var="robot" value="${robotWrapper.robot}"/>

                            <div class="modal fade" id="sendDiagramModal-${robot.name}" tabindex="-1" role="dialog"
                                 aria-labelledby="myModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close"
                                                    data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title" id="sendDiagramLabel">Send diagram</h4>
                                        </div>
                                        <div class="modal-body">

                                            <h4>Select diagram</h4>
                                            <select class="form-control">
                                                <c:forEach var="diagram" items="${user.diagrams}">
                                                    <option>${diagram.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" name="sendDiagram"
                                                    id="send-diagram-${robot.name}"
                                                    class="btn btn-primary">Send
                                            </button>
                                            <button type="button" class="btn btn-default"
                                                    data-dismiss="modal">Close
                                            </button>

                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal fade" id="configureRobotModal-${robot.name}" tabindex="-1" role="dialog"
                                 aria-labelledby="myModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close"
                                                    data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title" id="configureModalLabel">Configure robot</h4>

                                            <div id="validationError-${robot.name}" class="error" hidden></div>

                                        </div>
                                        <div class="modal-body">

                                            <ul class="nav nav-tabs">
                                                <li class="active"><a href="#portsConfig-${robot.name}"
                                                                      data-toggle="tab">Ports</a>
                                                </li>
                                                <li><a href="#devicesConfig-${robot.name}" data-toggle="tab">Devices</a>
                                                </li>
                                            </ul>
                                            <div id="myTabContent33" class="tab-content">
                                                <c:set var="systemConfig"
                                                       value="${robotWrapper.robotInfo.systemConfigObject}"/>
                                                <div class="tab-pane active in" id="portsConfig-${robot.name}">
                                                    <div class="row">
                                                        <c:set var="modelConfig"
                                                               value="${robotWrapper.robotInfo.modelConfigObject}"/>
                                                        <c:forEach var="port" items="${systemConfig.ports}">
                                                            <div class="col-md-4">
                                                                <div class="form-group">
                                                                    <div class="input-group">
                                                                <span class="input-group-addon"
                                                                      name="${robot.name}-port">${port.name}</span>
                                                            <span class="input-group-addon">
                                                                    <a role="button"
                                                                       data-toggle="dropdown"
                                                                       class="btn btn-default" data-target="#">
                                                                        <div id="${robot.name}-${port.name}"
                                                                             name="${robot.name}-${port.name}"
                                                                             data-toggle="popover"
                                                                             name="popover">${modelConfig.getDeviceName(port.name)}<span
                                                                                class="caret"></span></div>
                                                                    </a>
                                                                    <ul id="configureMenu"
                                                                        class="dropdown-menu multi-level" role="menu"
                                                                        aria-labelledby="dropdownMenu">
                                                                        <c:forEach var="device" items="${port.devices}">
                                                                            <c:if test="${device.types.size() > 0}">
                                                                                <li class="dropdown-submenu">
                                                                                    <a id="s-${robot.name}-${port.name}"
                                                                                       tabindex="-1"
                                                                                       href="#">${device.name}</a>
                                                                                    <ul class="dropdown-menu">
                                                                                        <c:forEach var="type"
                                                                                                   items="${device.types}">
                                                                                            <li><a href="#"
                                                                                                   id="s-${robot.name}-${port.name}">${type.name}</a>
                                                                                            </li>
                                                                                        </c:forEach>

                                                                                    </ul>

                                                                                </li>
                                                                            </c:if>
                                                                            <c:if test="${device.types.size() == 0}">
                                                                                <li class="dropdown">
                                                                                    <a tabindex="-1"
                                                                                       href="#"
                                                                                       id="s-${port.name}">${device.name}</a>
                                                                                </li>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </ul>
                                                            </span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>


                                                <div class="tab-pane fade" id="devicesConfig-${robot.name}">

                                                    <div class="row" style="margin-top:20px;" id="configureDeviceMenu">

                                                        <!-- Form Name -->


                                                        <div class="btn-group show-on-hover">
                                                            <button type="button"
                                                                    class="btn btn-default dropdown-toggle"
                                                                    data-toggle="dropdown"
                                                                    id="deviceType-${robot.name}">
                                                                Device <span
                                                                    class="caret"></span>
                                                            </button>
                                                            <ul class="dropdown-menu" role="menu">
                                                                <c:forEach var="device"
                                                                           items="${systemConfig.devices}">
                                                                    <c:forEach var="type"
                                                                               items="${device.types}">
                                                                        <li><a id="configureMenu-${robot.name}"
                                                                               href="#">${type.name}</a>
                                                                        </li>
                                                                    </c:forEach>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>

                                                        <c:forEach var="device"
                                                                   items="${systemConfig.devices}">
                                                            <c:forEach var="type"
                                                                       items="${device.types}">

                                                                <div class="panel" hidden
                                                                     name="propertyType-${robot.name}"
                                                                     id="property-${robot.name}-${type.name}">
                                                                    <div class="well">
                                                                        <div
                                                                                class="panel-body form-horizontal payment-form"
                                                                                id="form-${type.name}"
                                                                                name="propertyForm-${robot.name}"
                                                                                id="input-${type.name}">
                                                                            <c:forEach var="entry"
                                                                                       items="${type.properties}">
                                                                                <div class="form-group">
                                                                                    <label
                                                                                            name="label-${type.name}"
                                                                                            class="col-sm-3 control-label">${entry.key}</label>

                                                                                    <div class="col-sm-9">
                                                                                        <input id="input-${type.name}"
                                                                                               type="text"
                                                                                               class="form-control"
                                                                                               value="${entry.value}"
                                                                                               name="${entry.key}">
                                                                                    </div>
                                                                                </div>
                                                                            </c:forEach>


                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <!-- / panel preview -->
                                                            </c:forEach>
                                                        </c:forEach>


                                                    </div>
                                                    <!-- /.row -->


                                                </div>
                                            </div>


                                            <div class="modal-footer">
                                                <button type="button" name="saveModelConfig"
                                                        id="save-model-config-${robot.name}"
                                                        class="btn btn-primary">Save
                                                </button>
                                                <button type="button" class="btn btn-default"
                                                        data-dismiss="modal">Close
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <div class="col-md-6">

                                <div class="container-fluid well">
                                    <div class="row-fluid">
                                        <div class="col-md-4">
                                            <c:if test="${robotWrapper.status == 'Online'}">
                                            <img src="images/trik_smile_normal.png"
                                                 height="65" width="65" class="img-circle">
                                            </c:if>
                                            <c:if test="${robotWrapper.status != 'Online'}">
                                                <img src="images/trik_smile_sad.png"
                                                     height="65" width="65" class="img-circle">
                                            </c:if>
                                        </div>

                                        <div class="col-md-4">
                                            <h4>${robot.name}</h4>
                                            <h6>Status: ${robotWrapper.status}</h6>

                                        </div>

                                        <div class="col-md-4">

                                            <div class="btn-group pull-right">
                                                <a class="btn dropdown-toggle btn-info" data-toggle="dropdown" href="#">
                                                    <li class="glyphicon glyphicon-cog"></li>
                                                    Action
                                                    <span class="caret"></span>

                                                </a>
                                                <ul class="dropdown-menu">
                                                    <c:if test="${robotWrapper.status == 'Online'}">
                                                        <li><a href="#" data-toggle="modal"
                                                               data-target="#sendDiagramModal-${robot.name}"><span
                                                                class="icon-wrench"></span> Send diagram</a>
                                                        </li>
                                                        <li><a href="#" data-toggle="modal"
                                                               data-target="#configureRobotModal-${robot.name}"><span
                                                                class="icon-wrench"></span> Configure</a>
                                                        </li>


                                                    </c:if>

                                                    <li><a href='#' name="deleteRobot"
                                                           id="delete-${robot.name}">
                                                        <span class="icon-trash"></span>
                                                        Delete
                                                    </a>
                                                    </li>
                                                </ul>

                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <!--/col-span-6-->
                        </c:forEach>


                        <div class="col-md-6 ">
                            <div style="padding-top:2.8%;">


                                <a href="#" class="btn btn-info" data-toggle="modal" data-target="#myModal">
                                    <i class="glyphicon glyphicon-plus"></i><br/>
                                    Register robot
                                </a>

                                <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
                                     aria-labelledby="myModalLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal"
                                                        aria-label="Close"><span aria-hidden="true">&times;</span>
                                                </button>
                                                <h4 class="modal-title" id="myModalLabel">Register new robot</h4>
                                            </div>
                                            <div class="modal-body">
                                                <div id="registerError" class="error" hidden></div>
                                                <form class="form form-vertical">
                                                    <div class="control-group">
                                                        <label>Name</label>

                                                        <div class="controls">
                                                            <input id="robotName" type="text" class="form-control"
                                                                   placeholder="Enter Name">
                                                        </div>
                                                    </div>
                                                    <br/>

                                                    <div class="control-group">
                                                        <label>Robot ssid</label>

                                                        <div class="controls">
                                                            <input id="ssid" type="password" class="form-control"
                                                                   placeholder="ssid">

                                                        </div>
                                                    </div>
                                                    <br/>

                                                </form>

                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-default" data-dismiss="modal">
                                                    Close
                                                </button>
                                                <button type="button" id="registerRobot" class="btn btn-primary">
                                                    Register
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </div>

                </div>

                <div class="tab-pane" id="diagrams">
                    <h1>HERE WILL BE DIAGRAMS MANAGEMENT</h1>
                </div>


                <div class="tab-pane" id="map">

                    <div id="yamap" style="width: 100%; height: 600px"></div>
                    <button id="target">ADD LABEL</button>

                </div>
                <div class="tab-pane" id="settings">
                    <div class="col-md-8">
                        <div class="container-fluid">
                            <div class="row text-center">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--/row-->
        </div>
    </div>


</div>


</body>

</html>
