interface DeviceInfo {
    getName(): string;
    getFriendlyName(): string;
    getType();
    isA(type): boolean;
}