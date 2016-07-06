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

/// <reference path="ColorFieldItem.ts" />
/// <reference path="../../../../vendor.d.ts"/>

interface CubicBezierItem extends ColorFieldItem {
    getPath(): RaphaelPath;
    updateStart(x: number, y: number): void;
    updateEnd(x: number, y: number): void;
    updateCP1(x: number, y: number): void;
    updateCP2(x: number, y: number): void;
}