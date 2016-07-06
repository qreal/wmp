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

class ChangeCurrentElementCommand implements Command {

    private element: DiagramElement;
    private oldElement: DiagramElement;
    private executionFunction: (element: DiagramElement) => void;

    constructor(element: DiagramElement, oldElement: DiagramElement,
                executionFunction: (element: DiagramElement) => void) {
        this.element = element;
        this.oldElement = oldElement;
        this.executionFunction = executionFunction;
    }

    public execute(): void {
        this.executionFunction(this.element);
    }

    public revert(): void {
        this.executionFunction(this.oldElement);
    }

    public isRevertible() : boolean{
        return (this.oldElement !== this.element);
    }

}