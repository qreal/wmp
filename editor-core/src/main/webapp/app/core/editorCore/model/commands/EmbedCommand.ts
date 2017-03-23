import {Command} from "./Command";
import {DiagramContainer} from "../DiagramContainer";
import {DiagramNode} from "../DiagramNode";
export class EmbedCommand implements Command {

    private child: DiagramNode;
    private parent: DiagramContainer;
    private oldParent: DiagramContainer;

    constructor(child: DiagramNode, parent: DiagramNode, oldParent: DiagramContainer) {
        this.child = child;
        this.parent = <DiagramContainer> parent;
        this.oldParent = <DiagramContainer> oldParent;
    }

    public execute(): void {
        this.child.setParentNode(this.parent);
    }

    public revert(): void {
        this.child.setParentNode(this.oldParent);
    }

    public isRevertible() : boolean {
        return true;
    }

}