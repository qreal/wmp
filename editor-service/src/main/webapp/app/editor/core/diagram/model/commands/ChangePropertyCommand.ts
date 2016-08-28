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

class ChangePropertyCommand implements Command {

    private key: string;
    private value: string;
    private oldValue: string;
    private executionFunction: (key: string, value: string) => void;
    private changeHtmlFunction: (value: string) => void;

    constructor(key: string, value: string, oldValue: string,
                executionFunction: (name: string, value: string) => void,
                changeHtmlFunction: (value: string) => void) {
        this.key = key;
        this.value = value;
        this.oldValue = oldValue;
        this.executionFunction = executionFunction;
        this.changeHtmlFunction = changeHtmlFunction;
    }

    public execute(): void {
        this.executionFunction(this.key, this.value);
        this.changeHtmlFunction(this.value);
    }

    public revert(): void {
        this.executionFunction(this.key, this.oldValue);
        this.changeHtmlFunction(this.oldValue);
    }

    public isRevertible() {
        return this.oldValue !== this.value;
    }

}