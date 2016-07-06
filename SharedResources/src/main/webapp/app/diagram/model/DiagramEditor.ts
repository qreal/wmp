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

/// <reference path="DiagramPaper.ts" />
/// <reference path="../../vendor.d.ts" />

class DiagramEditor {

    private graph: joint.dia.Graph;
    private paper: DiagramPaper;

    constructor() {
        this.graph = new joint.dia.Graph;
        this.paper = new DiagramPaper("diagram_paper", this.graph);
    }

    public getGraph(): joint.dia.Graph {
        return this.graph;
    }

    public getPaper(): DiagramPaper {
        return this.paper;
    }

    public clear(): void {
        this.paper.clear();
        this.graph.clear();
    }

}