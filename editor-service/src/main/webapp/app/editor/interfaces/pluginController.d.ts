declare class PluginController {
    public static create(className: string, ...args: any[]);
    public static exec(object: any, method: string, ...args: any[]);
}
