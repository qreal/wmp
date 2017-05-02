export class SelectorService {

    private selectors : string;

    constructor (selectors: string) {
        this.selectors = selectors;
    }

    public getSelectors() : any {
        return JSON.parse(this.selectors);
    }
}
