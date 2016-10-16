/// <reference path="../../interfaces/robotModel/DeviceInfo.ts" />

class DevicesConfigurationProvider {
    private currentConfiguration: {string?: {string?: DeviceInfo}} = {};

    deviceConfigurationChanged(robotModelName: string, portName: string, device: DeviceInfo): void {
        if (!this.currentConfiguration[robotModelName]) {
            this.currentConfiguration[robotModelName] = {};
        }
        if (device == null) {
            if (this.currentConfiguration[robotModelName][portName]) {
                delete this.currentConfiguration[robotModelName][portName];
            }
        } else {
            this.currentConfiguration[robotModelName][portName] = device;
        }
    }

    getCurrentConfiguration(robotModelName: string, portName: string): DeviceInfo {
        if (!this.currentConfiguration[robotModelName] || !this.currentConfiguration[robotModelName][portName]) {
            return null;
        }
        return this.currentConfiguration[robotModelName][portName];
    }
}