import {MoveCommand} from "./MoveCommand";
import {ResizeCommand} from "./ResizeCommand";
import {Command} from "./Command";
import {DiagramNode} from "../DiagramNode";
import {RemoveElementCommand} from "./RemoveElementCommand";
import {Link} from "../Link";
import {CreateElementCommand} from "./CreateElementCommand";
import {ChangeCurrentElementCommand} from "./ChangeCurrentElementCommand";
import {DiagramElement} from "../DiagramElement";
import {SceneController} from "../../controller/SceneController";
import {DiagramContainer} from "../DiagramContainer";
import {EmbedCommand} from "./EmbedCommand";
export class SceneCommandFactory {

    private sceneController: SceneController;

    constructor(sceneController: SceneController) {
        this.sceneController = sceneController;
    }

    public makeChangeCurrentElementCommand(newElement: DiagramElement, oldElement: DiagramElement): Command {
        return new ChangeCurrentElementCommand(newElement, oldElement,
            this.sceneController.setCurrentElement.bind(this.sceneController));
    }

    public makeCreateNodeCommand(node: DiagramNode): Command {
        return new CreateElementCommand(node, this.sceneController.addNode.bind(this.sceneController),
            this.sceneController.removeElement.bind(this.sceneController));
    }

    public makeCreateLinkCommand(link: Link): Command {
        return new CreateElementCommand(link, this.sceneController.addLink.bind(this.sceneController),
            this.sceneController.removeElement.bind(this.sceneController));
    }

    public makeRemoveNodeCommand(node: DiagramNode): Command {
        return new RemoveElementCommand(node, this.sceneController.removeElement.bind(this.sceneController),
            this.sceneController.addNode.bind(this.sceneController));
    }

    public makeRemoveLinkCommand(link: Link): Command {
        return new RemoveElementCommand(link, this.sceneController.removeElement.bind(this.sceneController),
            this.sceneController.addLink.bind(this.sceneController));
    }

    public makeMoveCommand(node: DiagramNode, oldX: number, oldY: number, newX: number, newY: number,
                           zoom: number, cellView : joint.dia.CellView): Command {
        return new MoveCommand(oldX, oldY, newX, newY, zoom, cellView, node.getJointObject().translate.bind(node.getJointObject()));
    }

    public makeResizeCommand(node: DiagramNode, oldWidth: number, oldHeight: number, newWidth: number, newHeight: number): Command {
        return new ResizeCommand(oldWidth, oldHeight, newWidth, newHeight, node.setSize.bind(node));
    }

    public makeEmbedCommand(child: DiagramNode, parent: DiagramContainer, oldParent: DiagramContainer): Command {
        return new EmbedCommand(child, parent, oldParent);
    }s
}