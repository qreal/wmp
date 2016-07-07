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

class EndIfBlock extends AbstractBlock {

    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[]) {
        super(node, outboundLinks);
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);
        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }

}
