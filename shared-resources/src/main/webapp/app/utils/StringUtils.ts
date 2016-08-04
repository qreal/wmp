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

/// <reference path="../vendor.d.ts"/>

class StringUtils {

    static format(str: string, ...args: string[]): string {
        return str.replace(/{(\d+)}/g, (match, number) => {
            return typeof args[number] != 'undefined' ? args[number] : match;
        });
    }

    static getInputStringSize(input): number {
        var minWidth = 6;
        var $body = $('body');
        var $this =  $(input);
        var $text = $this.text();
        if($text=='') $text = $this.val();
        var calc = '<div style="clear:both;display:block;visibility:hidden;">' +
            '<span style="width;inherit;margin:0;font-family:' + $this.css('font-family') + ';font-size:'+
            $this.css('font-size') + ';font-weight:' + $this.css('font-weight') + '">' + $text + '</span>' +
            '</div>';
        $body.append(calc);
        var width = $('body').find('span:last').width();
        $body.find('span:last').parent().remove();
        return width + minWidth;
    }

}