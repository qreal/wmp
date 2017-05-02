export class SelectorService {

    private selectors : string;

    constructor (selectors: string) {
        this.selectors = selectors;
    }

    public printLog() : void {
        console.log(this.selectors);
    }
}
