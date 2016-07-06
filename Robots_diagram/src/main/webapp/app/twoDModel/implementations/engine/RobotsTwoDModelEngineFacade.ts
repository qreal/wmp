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

class RobotsTwoDModelEngineFacade extends TwoDModelEngineFacadeImpl {

    constructor($scope, $compile, $attrs) {
        super($scope, $compile, $attrs);

        var facade = this;
        $(document).ready(() => {
            $('#confirmDelete').find('.modal-footer #confirm').on('click', function() {
                facade.model.getWorldModel().clearPaper();
                $('#confirmDelete').modal('hide');
            });
        });

        $scope.start = () => {
            var timeline = this.model.getTimeline();
            $scope.$emit("emitInterpret", timeline);
        }
        $scope.resetPosition = () => { this.resetPosition(); };
        
        $scope.stop = () => {
            $scope.$emit("emitStop");
        }

        $scope.openDiagramEditor = () => { this.openDiagramEditor(); };

        // wall-button is disabled while there is no collision detection
        $("#wall-button").prop('disabled', true);
    }

    public openDiagramEditor(): void {
        $("#twoDModelContent").hide();
        $("#diagramContent").show();
    }

    public resetPosition(): void {
        var robotModel = this.model.getRobotModels()[0];
        robotModel.clearCurrentPosition();
    }

}
