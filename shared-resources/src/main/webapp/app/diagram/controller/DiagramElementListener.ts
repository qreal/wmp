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

/// <reference path="../model/Map.ts" />
/// <reference path="../model/Link.ts" />
/// <reference path="../model/Property.ts" />
/// <reference path="../../vendor.d.ts" />

class DiagramElementListener {

    static pointerdown: (evt, x , y) => void = function (evt, x, y) {
        if (
            evt.target.getAttribute('magnet') &&
            this.paper.options.validateMagnet.call(this.paper, this, evt.target)
        ) {
            this.model.trigger('batch:start');

            var link = this.paper.getDefaultLink(this, evt.target);
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

            var typeProperties = DiagramElementListener.getNodeProperties("ControlFlow");

            var nodeProperties: Map<Property> = {};
            for (var property in typeProperties) {
                nodeProperties[property] = new Property(typeProperties[property].name,
                    typeProperties[property].type, typeProperties[property].value);
            }

            var linkObject: Link = new Link(link, nodeProperties);
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

    static getNodeProperties: (type: string) => Map<Property> = function(type: string): Map<Property> {
        console.error("DiagramElementListener getNodeProperties method is empty")
        return null;
    };

    static makeAndExecuteCreateLinkCommand: (linkObject: Link) => void = function(linkObject: Link): void {
        console.error("DiagramElementListener makeAndExecuteCreateLinkCommand method is empty")
    }
}