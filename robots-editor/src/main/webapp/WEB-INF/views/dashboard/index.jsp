<%@ include file="../include/include.jsp" %>

<html lang="en">
<head>
    <title>WMP Dashboard</title>

    <jsp:include page="../include/scripts.jsp"/>
    <script src="<c:url value='/resources/js/map.js' />"></script>
    <script src="<c:url value='/resources/js/robot.js' />"></script>
    <script src="<c:url value='/resources/thrift/dashboard/robotService_types.js'/> "></script>
    <script src="<c:url value='/resources/thrift/dashboard/RobotServiceThrift.js'/> "></script>

    <link rel="stylesheet" href="<c:url value='/resources/css/error.css'/>"/>

    <script type="text/javascript">
        $(document).ready(function () {
            $("#add-label-button").click(function () {
                var robots = JSON.parse('${robots}');
                robots.forEach(function (robot) {
                    addLabel(robot);
                });
            });

            $("#register-robot-button").click(function () {

                var name = $('#robotName').val();
                var code = $('#ssid').val();

                var transport = new Thrift.TXHRTransport("http://localhost:8080/robots-editor/RobotRest"); //FIXME
                var protocol = new Thrift.TJSONProtocol(transport);
                var client = new RobotServiceThriftClient(protocol);
                try {
                    var result = client.registerRobot(name, code);
                    location.reload();
                }
                catch (ouch) {
                }
            });

            $('[name="delete-robot"]').click(function (event) {
                var robotId = event.target.id.substring(7);
                var transport = new Thrift.TXHRTransport("http://localhost:8080/robots-editor/RobotRest"); //FIXME
                var protocol  = new Thrift.TJSONProtocol(transport);
                var client = new RobotServiceThriftClient(protocol);
                try {
                    var result = client.deleteRobot(robotId);
                    location.reload();
                }
                catch (ouch) {
                }
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

<%@ include file="../include/navbar.jsp" %>

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
                        <c:forEach var="robot" items="${robots}">
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
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" name="sendDiagram"
                                                    id="send-diagram-${robot.id}"
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

                                                    <li><a href='#' name="delete-robot" id="delete-${robot.id}">
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
                                                <button id="register-robot-button" type="button" class="btn btn-primary">
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
                    <button id="add-label-button">ADD LABEL</button>

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
