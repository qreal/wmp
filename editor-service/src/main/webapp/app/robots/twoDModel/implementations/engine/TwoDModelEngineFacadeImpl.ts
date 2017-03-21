import {DeviceInfoImpl} from "../robotModel/DeviceInfoImpl";
import {ModelImpl} from "./model/ModelImpl";
import {TrikRobotModelBase} from "../robotModel/TrikKit/TrikRobotModelBase";
import app = require("../../../../require/app");
import {TwoDRobotModel} from "../robotModel/TwoDRobotModel";
import {Model} from "../../interfaces/engine/model/Model";
import {TwoDModelEngineFacade} from "../../interfaces/engine/TwoDModelEngineFacade";
export class TwoDModelEngineFacadeImpl implements TwoDModelEngineFacade {

    protected robotModelName: string;
    protected model: Model;
    //Hack for firefox
    static $$ngIsClass: boolean;

    constructor($scope, $compile, $attrs) {
        var facade = this;

        var robotModel = new TwoDRobotModel(new TrikRobotModelBase(), "model");
        this.robotModelName = robotModel.getName();

        this.model = new ModelImpl(parseFloat($("#two-d-model-scene-area").attr("zoom")));
        this.model.addRobotModel(robotModel);

        $(document).ready(() => {
            this.initPortsConfiguration($scope, $compile, robotModel);
            this.makeUnselectable(document.getElementById("two-d-model-area"));
            $('#two-d-model-clear-confirmation-window').find('.modal-footer #confirm').on('click', function() {
                facade.model.getWorldModel().clearScene();
                $('#two-d-model-clear-confirmation-window').modal('hide');
            });
        });

        $scope.followRobot = () => { this.followRobot(); };
        $scope.closeDisplay = () => { this.closeDisplay(); };
        $scope.showDisplay = () => { this.showDisplay(); };
        $scope.setDrawLineMode = () => { this.setDrawLineMode(); };
        $scope.setDrawWallMode = () => { this.setDrawWallMode(); };
        $scope.setDrawPencilMode = () => { this.setDrawPencilMode(); };
        $scope.setDrawEllipseMode = () => { this.setDrawEllipseMode(); };
        $scope.setNoneMode = () => { this.setNoneMode(); };
        $scope.start = () => {
            var timeline = this.model.getTimeline();
            $scope.$emit("emitInterpret", timeline);
        };
        $scope.resetPosition = () => { this.resetPosition(); };
        $scope.stop = () => {
            $scope.$emit("emitStop");
        };
        $scope.openDiagramEditor = () => { this.openDiagramEditor(); };

        // wall-button is disabled while there is no collision detection
        $("#wall-button").prop('disabled', true);
    }

    public setDrawLineMode(): void {
        this.model.getWorldModel().setDrawLineMode();
    }

    public setDrawWallMode(): void {
        this.model.getWorldModel().setDrawWallMode();
    }

    public setDrawPencilMode(): void {
        this.model.getWorldModel().setDrawPencilMode();
    }

    public setDrawEllipseMode(): void {
        this.model.getWorldModel().setDrawEllipseMode();
    }

    public setNoneMode(): void {
        this.model.getWorldModel().setNoneMode();
    }

    public closeDisplay(): void {
        this.model.getRobotModels()[0].closeDisplay();
    }

    public showDisplay(): void {
        this.model.getRobotModels()[0].showDisplay();
    }

    public followRobot(): void {
        var robotModel = this.model.getRobotModels()[0];
        robotModel.follow(!$("#follow-robot-button").hasClass('active'));
    }

    public openDiagramEditor(): void {
        $("#two-d-model-area").hide();
        $("#diagram-area").show();
    }

    public resetPosition(): void {
        var robotModel = this.model.getRobotModels()[0];
        robotModel.clearCurrentPosition();
    }

    private initPortsConfiguration($scope, $compile, twoDRobotModel: TwoDRobotModel): void {
        var configurationDropdownsContent = "<p>";
        twoDRobotModel.getConfigurablePorts().forEach(function(port) {
            var portName = port.getName();
            var id = portName + "Select";
            configurationDropdownsContent += "<p>";
            configurationDropdownsContent += portName + " ";
            configurationDropdownsContent += "<select id='" + id + "' style='width: 150px'>";
            configurationDropdownsContent += "<option value='Unused'>Unused</option>";
            var devices = twoDRobotModel.getAllowedDevices(port);
            devices.forEach(function (device) {
                configurationDropdownsContent += "<option value='" + device.getName() + "'>" +
                    device.getFriendlyName(); + "</option>";
            });
            configurationDropdownsContent += "</select>";
            configurationDropdownsContent += "</p>";
        });
        configurationDropdownsContent += "</p>";
        $('#configurationDropdowns').append($compile(configurationDropdownsContent)($scope));
        this.setPortsSelectsListeners(twoDRobotModel);
    }

    private setPortsSelectsListeners(twoDRobotModel: TwoDRobotModel): void {
        var facade = this;
        var sensorsConfiguration = facade.model.getRobotModels()[0].getSensorsConfiguration();
        twoDRobotModel.getConfigurablePorts().forEach(function(port) {
            var portName: string = port.getName();
            var htmlId = "#" + portName + "Select";

            $(htmlId).change(function() {
                var newValue: string = $(htmlId).val();
                switch (newValue) {
                    case "Unused":
                        sensorsConfiguration.removeSensor(portName);
                        break;
                    default:
                        var device = DeviceInfoImpl.fromString(newValue);
                        sensorsConfiguration.addSensor(portName, device);
                }
            });
        });
    }

    private makeUnselectable(element): void {
        if (element.nodeType == 1) {
            element.setAttribute("unselectable", "on");
        }
        var child = element.firstChild;
        while (child) {
            this.makeUnselectable(child);
            child = child.nextSibling;
        }
    }

}
TwoDModelEngineFacadeImpl.$$ngIsClass = true;
app.controller("TwoDModelEngineFacadeImpl", TwoDModelEngineFacadeImpl);
console.log("Adding controller TwoDModelEngineFacadeImpl");