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

/// <reference path="../../../interfaces/engine/model/WorldModel.ts" />
/// <reference path="../../../../constants/GeneralConstants.d.ts" />
/// <reference path="../../../../vendor.d.ts" />

class StartPositionItem {
    private width = 15;
    private height = 15;
    private image: RaphaelElement;

    constructor(worldModel: WorldModel, x: number, y: number, direction: number) {
        this.image = worldModel.getPaper().image(GeneralConstants.APP_ROOT_PATH + "images/2dmodel/cross.png",
            x - this.width / 2, y - this.height / 2,
            this.width, this.height);
        this.image.transform("R" + direction);
        this.image.toBack();
    }

    remove(): void {
        this.image.remove();
    }
    
}