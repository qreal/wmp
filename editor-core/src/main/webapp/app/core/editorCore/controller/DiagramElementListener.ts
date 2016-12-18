/// <reference path="../model/Map.ts" />
/// <reference path="../model/Link.ts" />
/// <reference path="../model/Property.ts" />
/// <reference path="../../../vendor.d.ts" />

class DiagramElementListener {

    static pointerdown: (evt, x , y) => void = function (evt, x, y) {
        if (
            evt.target.getAttribute('magnet') &&
            this.paper.options.validateMagnet.call(this.paper, this, evt.target)
        ) {
            this.model.trigger('batch:start');

            var link = this.paper.getCurrentLinkType();
            if (evt.target.tagName === "circle") {
                link.set({
                    source: {
                        id: this.model.id
                    },
                    target: { x: x, y: y }
                });
            } else {
                link.set({
                    source: {
                        id: this.model.id,
                        selector: this.getSelector(evt.target),
                        port: $(evt.target).attr('port')
                    },
                    target: { x: x, y: y }
                });
            }

            var nodeType = DiagramElementListener.getNodeType(this.paper.getCurrentLinkTypeName());
            var typeProperties = nodeType.getPropertiesMap;

            var nodeProperties: Map<Property> = {};
            for (var property in typeProperties) {
                nodeProperties[property] = new Property(typeProperties[property].name,
                    typeProperties[property].type, typeProperties[property].value);
            }

            var linkObject: Link = new Link(link, nodeType);
            DiagramElementListener.makeAndExecuteCreateLinkCommand(linkObject);

            this.paper.model.addCell(link);

            this._linkView = this.paper.findViewByModel(link);
            this._linkView.startArrowheadMove('target');

        } else {

            this._dx = x;
            this._dy = y;

            joint.dia.CellView.prototype.pointerdown.apply(this, arguments);
        }
    };

    static getNodeType: (type: string) => NodeType = function(type: string): NodeType {
        console.error("DiagramElementListener getNodeType method is empty");
        return null;
    };

    static makeAndExecuteCreateLinkCommand: (linkObject: Link) => void = function(linkObject: Link): void {
        console.error("DiagramElementListener makeAndExecuteCreateLinkCommand method is empty")
    }
}