import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Pool} from "./Pool";
import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
export class Lane extends DiagramContainer {
    public isValidEmbedding(child: DiagramNode) {
        return !(child instanceof Pool) && !(child instanceof Lane);
    }
}