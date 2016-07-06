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

/// <reference path="../vendor.d.ts" />

class CheckerController {

    private kit: string;
    private lastResponse;

    constructor($scope, $compile, $attrs) {
        this.kit = $attrs.kit;
        $scope.checker = this;
        $(document).ready(() => {
            $("#spinner").hide();
            $("#twoDModelContent").hide();
            $("#rerun").prop('disabled', true);
        });

        $scope.uploadFile = () => { this.uploadFile($scope) };

        $scope.showResult = () => {
            if (this.lastResponse) {
                $("#infoAlert").hide();
                $scope.$emit("emitCheckingResult", this.lastResponse);
            }
        }
    }

    uploadFile($scope) {
        this.lastResponse = null;
        $("#rerun").prop('disabled', true);
        var controller: CheckerController = this;
        var spinner = $('#spinner');
        spinner.show();
        $("#twoDModelContent").hide();
        $('#result').html('');

        $("#uploadForm").ajaxForm({
            dataType: "json",
            data: {
                'kit': controller.kit
            },
            timeout: 60000,
            success: function (response) {
                spinner.hide();
                controller.lastResponse = response;
                $("#rerun").prop('disabled', false);
                $('#result').html(controller.lastResponse.message);
                $scope.showResult();
            },
            error: function (response, status, error) {
                console.log(response);
                spinner.hide();
                if (status == "timeout") {
                    $('#result').html("Timed out – please try again");
                } else {
                    $('#result').html(response.responseText);
                }
            }
        }).submit();
    }
}