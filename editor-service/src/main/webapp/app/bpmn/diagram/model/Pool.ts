import {DiagramContainer} from "core/editorCore/model/DiagramContainer";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {Lane} from "./Lane";
export class Pool extends DiagramContainer {
    public isValidEmbedding(child: DiagramNode) {
        return child instanceof Lane;
    }
}