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

class PaperCommandFactory {

    private paperController: PaperController;

    constructor(paperController: PaperController) {
        this.paperController = paperController;
    }

    public makeChangeCurrentElementCommand(newElement: DiagramElement, oldElement: DiagramElement): Command {
        return new ChangeCurrentElementCommand(newElement, oldElement,
            this.paperController.setCurrentElement.bind(this.paperController));
    }

    public makeCreateNodeCommand(node: DiagramNode): Command {
        return new CreateElementCommand(node, this.paperController.addNode.bind(this.paperController),
            this.paperController.removeElement.bind(this.paperController));
    }

    public makeCreateLinkCommand(link: Link): Command {
        return new CreateElementCommand(link, this.paperController.addLink.bind(this.paperController),
            this.paperController.removeElement.bind(this.paperController));
    }

    public makeRemoveNodeCommand(node: DiagramNode): Command {
        return new RemoveElementCommand(node, this.paperController.removeElement.bind(this.paperController),
            this.paperController.addNode.bind(this.paperController));
    }

    public makeRemoveLinkCommand(link: Link): Command {
        return new RemoveElementCommand(link, this.paperController.removeElement.bind(this.paperController),
            this.paperController.addLink.bind(this.paperController));
    }

    public makeMoveCommand(node: DiagramNode, oldX: number, oldY: number, newX: number, newY: number,
                           zoom: number): Command {
        return new MoveCommand(oldX, oldY, newX, newY, zoom, node.setPosition.bind(node));
    }

}