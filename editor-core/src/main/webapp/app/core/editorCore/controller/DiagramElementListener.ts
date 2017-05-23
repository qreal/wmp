import {Link} from "../model/Link";
import {NodeType} from "../model/NodeType";
import {Property} from "../model/Property";
export class DiagramElementListener {

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

            var nodeType: NodeType = DiagramElementListener.getNodeType(this.paper.getCurrentLinkTypeName());
            var typeProperties: Map<string, Property> = nodeType.getPropertiesMap();

            var nodeProperties: Map<string, Property> = new Map<string, Property>();
            for (var [propertyName, property] of typeProperties) {
                nodeProperties.set(propertyName, new Property(property.name, property.type, property.value));
            }
            DiagramElementListener.makeAndExecuteCreateLinkCommand(link, nodeType.getShownName(), nodeType.getName(), nodeProperties);

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

    static makeAndExecuteCreateLinkCommand: (jointObject: joint.dia.Link, name: string, type: string,
                                             properties: Map<string, Property>) => void = function(): void {
        console.error("DiagramElementListener makeAndExecuteCreateLinkCommand method is empty")
    }
}