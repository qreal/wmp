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

abstract class MotorsDirectionBlock extends MotorsBlock {

    private interpreter: Interpreter;
    
    constructor(node: DiagramNode, outboundLinks: Link[], robotModels: RobotModel[], interpreter: Interpreter) {
        super(node, outboundLinks, robotModels);
        this.interpreter = interpreter;
    }
    
    protected getPowerProperty(): number {
        var properties = this.node.getChangeableProperties();
        var parser = new Parser();
        var power = parser.parseExpression(properties["Power"].value, this.interpreter);
        if (power < -100 || power > 100) {
            throw new Error("Error: incorrect power value in " + this.node.getName() +
                " (must be between -100 and 100)");
        }
        return power;
    }

}