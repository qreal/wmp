class Variant {

    private key: string;
    private value: string;

    constructor(key: string, value: string) {
        this.key = key;
        this.value = value;
    }

    public getKey(): string {
        return this.key;
    }

    public getValue(): string {
        return this.value;
    }

}