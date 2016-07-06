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

abstract class AbstractBlock {

    protected node: DiagramNode;
    protected outboundLinks: Link[];
    
    constructor(node: DiagramNode, outboundLinks: Link[]) {
        this.node = node;
        this.outboundLinks = outboundLinks;
    }
    
    public abstract run(): void;
    
    public abstract getNextNodeId(): string;

    public checkExpectedNumberOfOutboundLinks(expectedNumber: number): void {
        if (this.outboundLinks.length !== expectedNumber) {
            throw new Error("There must be exact " + expectedNumber + " links from " + this.node.getName());
        }
    }
    
    public checkRangeNumberOfOutboundLinks(minNumber: number, maxNumber: number): void {
        if (this.outboundLinks.length < minNumber) {
            throw new Error("There must be at least " + minNumber + " links from " + this.node.getName());
        }
        if (this.outboundLinks.length > maxNumber) {
            throw new Error("There more than " + maxNumber + " links from " + this.node.getName());
        }
    }
    
}
