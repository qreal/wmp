import {AbstractBlock} from "./AbstractBlock";
import {Link} from "core/editorCore/model/Link";
export abstract class ConditionBlock extends AbstractBlock {

    protected getGuard(link : Link): string {
        return link.getChangeableProperties().get("Guard").value;
    }
    
}