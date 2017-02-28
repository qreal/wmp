import {ServerProperties} from "../properties/ServerProperties";
export class GeneralConstants {

    static APP_ROOT_PATH: string = ServerProperties.pathEditor + "/";
    static DEFAULT_KIT: string = "2015";
    static DASHBOARD_REST_SERVLET: string = "http://localhost:" + ServerProperties.portDashboard +
        ServerProperties.pathDashboard + ServerProperties.pathDashboardService;
    static EDITOR_REST_SERVLET: string = "http://localhost:" + ServerProperties.portEditor +
        ServerProperties.pathEditor + ServerProperties.pathEditorService;
    static PALETTE_REST_SERVLET: string = "http://localhost:" + ServerProperties.portEditor +
    ServerProperties.pathEditor + ServerProperties.pathPaletteService;
    
}
