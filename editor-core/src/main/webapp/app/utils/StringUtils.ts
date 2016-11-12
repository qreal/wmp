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