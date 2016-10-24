var MathUtils = (function () {
    function MathUtils() {
    }
    MathUtils.toDeg = function (radians) {
        return radians * (180 / Math.PI);
    };
    MathUtils.toRad = function (degrees) {
        return degrees * (Math.PI / 180);
    };
    MathUtils.twoPointLenght = function (x1, y1, x2, y2) {
        return Math.sqrt(this.sqr(x1 - x2) + this.sqr(y1 - y2));
    };
    MathUtils.sqr = function (x) {
        return x * x;
    };
    MathUtils.min = function (a, b) {
        return (a < b) ? a : b;
    };
    MathUtils.toRadians = function (angle) {
        return angle * Math.PI / 180.0;
    };
    MathUtils.toDegrees = function (angle) {
        return angle * 180.0 / Math.PI;
    };
    MathUtils.rotateVector = function (x, y, angle) {
        angle = MathUtils.toRadians(angle);
        var newX = x * Math.cos(angle) - y * Math.sin(angle);
        var newY = x * Math.sin(angle) + y * Math.cos(angle);
        return { x: newX, y: newY };
    };
    return MathUtils;
}());
var StringUtils = (function () {
    function StringUtils() {
    }
    StringUtils.format = function (str) {
        var args = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            args[_i - 1] = arguments[_i];
        }
        return str.replace(/{(\d+)}/g, function (match, number) {
            return typeof args[number] != 'undefined' ? args[number] : match;
        });
    };
    StringUtils.getInputStringSize = function (input) {
        var minWidth = 6;
        var $body = $('body');
        var $this = $(input);
        var $text = $this.text();
        if ($text == '')
            $text = $this.val();
        var calc = '<div style="clear:both;display:block;visibility:hidden;">' +
            '<span style="width;inherit;margin:0;font-family:' + $this.css('font-family') + ';font-size:' +
            $this.css('font-size') + ';font-weight:' + $this.css('font-weight') + '">' + $text + '</span>' +
            '</div>';
        $body.append(calc);
        var width = $('body').find('span:last').width();
        $body.find('span:last').parent().remove();
        return width + minWidth;
    };
    return StringUtils;
}());
//# sourceMappingURL=utils.js.map