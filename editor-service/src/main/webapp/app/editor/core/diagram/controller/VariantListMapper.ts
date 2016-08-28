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

/// <reference path="../model/Variant.ts" />

class VariantListMapper {

    private static variantsMap = {};

    static addVariantList(typeName: string, propertyKey: string, variants: Variant[]) {
        if (!this.variantsMap[typeName]) {
            this.variantsMap[typeName] = {};
        }
        this.variantsMap[typeName][propertyKey] = variants;
    }

    static getVariantList(typeName: string, propertyKey: string): Variant[] {
        return this.variantsMap[typeName][propertyKey];
    }
}