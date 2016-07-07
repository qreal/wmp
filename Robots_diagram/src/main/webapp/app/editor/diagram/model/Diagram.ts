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

class Diagram {

    private id: number;
    private name: string;

    public static createFromJson(diagramJson: any) {
        return new Diagram(diagramJson.diagramId, diagramJson.name)
    }

    constructor(id: number, name: string) {
        this.id = id;
        this.name = name;
    }

    public setId(id: number): void {
        this.id = id;
    }

    public getId(): number {
        return this.id;
    }

    public setName(name: string): void {
        this.name = name;
    }

    public getName(): string {
        return this.name;
    }

}