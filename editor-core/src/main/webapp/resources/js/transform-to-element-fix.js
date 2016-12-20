SVGElement.prototype.getTransformToElement = SVGElement.prototype.getTransformToElement || function(elem) {
        return elem.getScreenCTM().inverse().multiply(this.getScreenCTM());
    };