interface GesturesSceneController {
    getCurrentElement(): Object;
    createNodeInEventPositionFromNames(names: string[], event): void;
    createLinkBetweenCurrentAndEventTargetElements(event): void;
}

interface GesturesDiagramScene {
    getId(): string;
}