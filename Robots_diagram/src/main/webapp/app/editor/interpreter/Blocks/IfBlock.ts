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

class IfBlock extends ConditionBlock {

    private interpreter: Interpreter;
    private parsedCondition: any;
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 2;

    constructor(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter) {
        super(node, outboundLinks);
        this.interpreter = interpreter;
    }
    
    public run(): void {
        var output = this.node.getName() + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);
        var condition = this.getCondition(this.node);
        var parser = new Parser();
        this.parsedCondition = parser.parseExpression(condition, this.interpreter);
        output += "Condition: " + this.parsedCondition + "\n";
        console.log(output);
    }
    
    public getNextNodeId(): string {
        var firstLink: Link = this.outboundLinks[0];
        var secondLink: Link = this.outboundLinks[1];
        var firstLinkGuard = this.getGuard(firstLink);
        var secondLinkGuard = this.getGuard(secondLink);
        var firstLinkTargetId: string = firstLink.getJointObject().get('target').id;
        var secondLinkTargetId: string = secondLink.getJointObject().get('target').id;
        if (firstLinkGuard === "true" && secondLinkGuard === "false" 
            || firstLinkGuard === "true" && secondLinkGuard === ""
            || firstLinkGuard === "" && secondLinkGuard === "false") {
            return (this.parsedCondition) ? firstLinkTargetId : secondLinkTargetId;
        } else if (firstLinkGuard === "false" && secondLinkGuard === "true" 
            || firstLinkGuard === "false" && secondLinkGuard === ""
            || firstLinkGuard === "" && secondLinkGuard === "true") {
            return (this.parsedCondition) ? secondLinkTargetId : firstLinkTargetId;
        } 
        throw new Error("Error: wrong link Guard options connected to " + this.node.getName());
    }

    protected getCondition(node: DiagramNode): string {
        return node.getChangeableProperties()["Condition"].value;
    }
    
}
