declare class ContextMenu {

    isVisible(): boolean;
    addItem(item): void;
    addSubMenu(item): void;
    removeItemByName(name): void;
    setItems(items): void;
    showMenu(event, parent, items): void;
    initMenu(parent): void;

}