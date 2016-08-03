/*
 * Copyright Vladimir Zakharov 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/// <reference path="../CommonRobotModelImpl.ts" />
/// <reference path="../PortInfoImpl.ts" />
/// <reference path="../DeviceInfoImpl.ts" />
/// <reference path="../robotParts/Display.ts" />
/// <reference path="../robotParts/Speaker.ts" />
/// <reference path="../robotParts/Button.ts" />
/// <reference path="../robotParts/Motor.ts" />
/// <reference path="../robotParts/EncoderSensor.ts" />
/// <reference path="../robotParts/LightSensor.ts" />
/// <reference path="../robotParts/GyroscopeSensor.ts" />
/// <reference path="../robotParts/AccelerometerSensor.ts" />
/// <reference path="../TrikKit/parts/TrikInfraredSensor.ts" />
/// <reference path="../TrikKit/parts/TrikSonarSensor.ts" />
/// <reference path="../TrikKit/parts/TrikMotionSensor.ts" />
/// <reference path="../TrikKit/parts/TrikLed.ts" />
/// <reference path="../TrikKit/parts/TrikLineSensor.ts" />
/// <reference path="../TrikKit/parts/TrikColorSensor.ts" />
/// <reference path="../TrikKit/parts/TrikObjectSensor.ts" />
/// <reference path="../TrikKit/parts/TrikShell.ts" />
/// <reference path="../TrikKit/parts/TrikGamepadButton.ts" />
/// <reference path="../TrikKit/parts/TrikGamepadPad.ts" />
/// <reference path="../TrikKit/parts/TrikGamepadPadPressSensor.ts" />
/// <reference path="../TrikKit/parts/TrikGamepadWheel.ts" />
/// <reference path="../TrikKit/parts/TrikGamepadConnectionIndicator.ts" />
/// <reference path="../../../interfaces/robotModel/PortInfo.ts" />
/// <reference path="../../../interfaces/robotModel/DeviceInfo.ts" />
/// <reference path="../../../types/Direction.ts" />

class TrikRobotModelBase extends CommonRobotModelImpl {
    private digitalPorts: PortInfo[];
    
    constructor() {
        super();
        var analogPortConnections: DeviceInfo[] = [this.lightSensorInfo(), this.infraredSensorInfo()];

        this.addAllowedConnection(new PortInfoImpl("DisplayPort", Direction.output),[this.displayInfo()]);
        this.addAllowedConnection(new PortInfoImpl("SpeakerPort", Direction.output), [this.speakerInfo()]);

        this.addAllowedConnection(new PortInfoImpl("Left", Direction.input, [], "buttonLeft"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Right", Direction.input, [], "buttonRight"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Up", Direction.input, [], "buttonUp"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Down", Direction.input, [], "buttonDown"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Enter", Direction.input, [], "buttonEnter"), [this.buttonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("Esc", Direction.input, [], "buttonEsc"), [this.buttonInfo()]);

        this.addAllowedConnection(new PortInfoImpl("C1", Direction.output, ["JC1"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("C2", Direction.output, ["JC2" ]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("C3", Direction.output, ["JC3"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E1", Direction.output, ["JE1"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E2", Direction.output, ["JE2"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E3", Direction.output, ["JE3"]), [this.servoMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("E4", Direction.output, ["JE4"]), [this.servoMotorInfo()]);

        this.addAllowedConnection(new PortInfoImpl("M1", Direction.output, ["JM1", "A", "1"]), [this.powerMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("M2", Direction.output, ["JM2", "B", "2"]), [this.powerMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("M3", Direction.output, ["JM3", "C", "3"]), [this.powerMotorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("M4", Direction.output, ["JM4", "D", "4"]), [this.powerMotorInfo()]);

        this.addAllowedConnection(new PortInfoImpl("B1", Direction.input, ["JB1", "M1", "JM1", "A", "1"], "encoder1"), [this.encoderInfo()]);
        this.addAllowedConnection(new PortInfoImpl("B2", Direction.input, ["JB2", "M2", "JM2", "B", "2"], "encoder2"), [this.encoderInfo()]);
        this.addAllowedConnection(new PortInfoImpl("B3", Direction.input, ["JB3", "M3", "JM3", "C", "3"], "encoder3"), [this.encoderInfo()]);
        this.addAllowedConnection(new PortInfoImpl("B4", Direction.input, ["JB4", "M4", "JM4", "D", "4"], "encoder4"), [this.encoderInfo()]);

        this.addAllowedConnection(new PortInfoImpl("A1", Direction.input, ["JA1"], "sensorA1"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A2", Direction.input, ["JA2"], "sensorA2"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A3", Direction.input, ["JA3"], "sensorA3"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A4", Direction.input, ["JA4"], "sensorA4"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A5", Direction.input, ["JA5"], "sensorA5"), analogPortConnections);
        this.addAllowedConnection(new PortInfoImpl("A6", Direction.input, ["JA6"], "sensorA6"), analogPortConnections);

        this.digitalPorts = [
            new PortInfoImpl("D1", Direction.input, ["JD1"], "sensorD1"),
            new PortInfoImpl("D2", Direction.input, ["JD2"], "sensorD2"),
            new PortInfoImpl("F1", Direction.input, ["JF1"], "sensorF1")
        ];
        this.addAllowedConnection(this.digitalPorts[0], [this.sonarSensorInfo()]);
        this.addAllowedConnection(this.digitalPorts[1], [this.sonarSensorInfo()]);
        this.addAllowedConnection(this.digitalPorts[2], [this.motionSensorInfo()]);

        this.addAllowedConnection(new PortInfoImpl("GyroscopePortX", Direction.input, [], "gyroscopeX"), [this.gyroscopeInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GyroscopePortY", Direction.input, [], "gyroscopeY"), [this.gyroscopeInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GyroscopePortZ", Direction.input, [], "gyroscopeZ"), [this.gyroscopeInfo()]);

        this.addAllowedConnection(new PortInfoImpl("AccelerometerPortX", Direction.input, [], "accelerometerX"), [this.accelerometerInfo()]);
        this.addAllowedConnection(new PortInfoImpl("AccelerometerPortY", Direction.input, [], "accelerometerY"), [this.accelerometerInfo()]);
        this.addAllowedConnection(new PortInfoImpl("AccelerometerPortZ", Direction.input, [], "accelerometerZ"), [this.accelerometerInfo()]);

        this.addAllowedConnection(new PortInfoImpl("LedPort", Direction.output), [this.ledInfo()]);

        this.addAllowedConnection(new PortInfoImpl("LineSensorXPort", Direction.input, [], "lineSensorX"), [this.lineSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("LineSensorSizePort", Direction.input, [], "lineSensorSize"), [this.lineSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("LineSensorCrossroadsPort", Direction.input, [], "lineSensorCross"), [this.lineSensorInfo()]);

        this.addAllowedConnection(new PortInfoImpl("ObjectSensorXPort", Direction.input, [], "objectSensorX"), [this.objectSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ObjectSensorYPort", Direction.input, [], "objectSensorY"), [this.objectSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ObjectSensorSizePort", Direction.input, [], "objectSensorSize"), [this.objectSensorInfo()]);

        this.addAllowedConnection(new PortInfoImpl("ColorSensorRPort", Direction.input, [], "colorSensorR"), [this.colorSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ColorSensorGPort", Direction.input, [], "colorSensorG"), [this.colorSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("ColorSensorBPort", Direction.input, [], "colorSensorB"), [this.colorSensorInfo()]);

        this.addAllowedConnection(new PortInfoImpl("ShellPort", Direction.output), [this.shellInfo()]);

        this.addAllowedConnection(new PortInfoImpl("GamepadPad1PosPort", Direction.input, [], "gamepadPad1"
            , ReservedVariableType.vector), [this.gamepadPadInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadPad2PosPort", Direction.input, [], "gamepadPad2"
            , ReservedVariableType.vector), [this.gamepadPadInfo()]);

        this.addAllowedConnection(new PortInfoImpl("GamepadPad1PressedPort", Direction.input, [], "gamepadPad1Pressed")
            , [this.gamepadPadPressSensorInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadPad2PressedPort", Direction.input, [], "gamepadPad2Pressed")
            , [this.gamepadPadPressSensorInfo()]);

        this.addAllowedConnection(new PortInfoImpl("GamepadWheelPort", Direction.input, [], "gamepadWheel"), [this.gamepadWheelInfo()]);

        this.addAllowedConnection(new PortInfoImpl("GamepadButton1Port", Direction.input, [], "gamepadButton1"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton2Port", Direction.input, [], "gamepadButton2"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton3Port", Direction.input, [], "gamepadButton3"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton4Port", Direction.input, [], "gamepadButton4"), [this.gamepadButtonInfo()]);
        this.addAllowedConnection(new PortInfoImpl("GamepadButton5Port", Direction.input, [], "gamepadButton5"), [this.gamepadButtonInfo()]);

        this.addAllowedConnection(new PortInfoImpl("GamepadConnectionIndicatorPort", Direction.input, [], "gamepadConnected")
            , [this.gamepadConnectionIndicatorInfo()]);
    }

    getConfigurablePorts(): PortInfo[] {
        return super.getConfigurablePorts().concat(this.digitalPorts);
    }

    displayInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(Display);
    }
    speakerInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(Speaker);
    }
    buttonInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(Button);
    }

    powerMotorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(Motor);
    }
    servoMotorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(Motor);
    }
    encoderInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(EncoderSensor);
    }

    lightSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(LightSensor);
    }

    infraredSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikInfraredSensor);
    }

    sonarSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikSonarSensor);
    }

    motionSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikMotionSensor);
    }

    gyroscopeInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(GyroscopeSensor);
    }
    accelerometerInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(AccelerometerSensor);
    }

    ledInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikLed);
    }
    lineSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikLineSensor);
    }
    colorSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikColorSensor);
    }
    objectSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikObjectSensor);
    }
    shellInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikShell);
    }

    gamepadButtonInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikGamepadButton);
    }
    gamepadPadInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikGamepadPad);
    }
    gamepadPadPressSensorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikGamepadPadPressSensor);
    }
    gamepadWheelInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikGamepadWheel);
    }
    gamepadConnectionIndicatorInfo(): DeviceInfo {
        return DeviceInfoImpl.getInstance(TrikGamepadConnectionIndicator);
    }
}