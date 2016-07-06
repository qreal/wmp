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

/// <reference path="../../../two-d-model-core.d.ts" />
/// <reference path="../../../vendor.d.ts" />

class StepicTwoDModelEngineFacade extends TwoDModelEngineFacadeImpl {

    constructor($scope, $compile, $attrs) {
        super($scope, $compile, $attrs);

        $scope.$on("displayCheckingResult", (event, result) => {
            this.displayResult(result);
        });

        $scope.$on("2dModelLoad", (event, fieldXML) => {
            this.model.deserialize($.parseXML(fieldXML));
        });

        $("#infoAlert").hide();
        $(".close").click(function () {
            $(this).parent().hide();
        })

        $scope.stopPlay = () => { this.stopPlay(); };
    }

    private displayResult(result): void {
        this.model.getWorldModel().clearPaper();
        this.model.deserialize($.parseXML(result.fieldXML));
        $("#twoDModelContent").show();
        var robotModel = this.model.getRobotModels()[0];
        robotModel.showCheckResult(result);
    }

    public stopPlay(): void {
        var robotModel = this.model.getRobotModels()[0];
        $("#infoAlert").hide();
        robotModel.stopPlay();
    }

}
