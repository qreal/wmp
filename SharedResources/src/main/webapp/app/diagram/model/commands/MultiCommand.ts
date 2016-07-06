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

class MultiCommand implements Command {

    private commands: Command[];

    constructor(commands: Command[]) {
        this.commands = commands;
    }

    public execute(): void {
        this.commands.forEach((command: Command) => command.execute());
    }

    public revert(): void {
        for (var i = this.commands.length - 1; i >= 0; i--) {
            this.commands[i].revert();
        }
    }

    public isRevertible(): boolean {
        return this.commands.reduce((previousValue: boolean, command: Command): boolean => {
            return previousValue && command.isRevertible()
        }, true);
    }

}