module GesturesUtils {
    export class Pair {
        first: number;
        second: number;

        constructor(first : number, second : number) {
            this.first = first;
            this.second = second;
        }
    }

    export class PairString {
        first: string;
        second: string;

        constructor(curString: string) {
            var index = curString.indexOf(" ");
            this.first = curString.substr(0, index);
            this.second = curString.substr(index, curString.length - index);
        }

        public getString(): string {
            return this.first + " - " + this.second;
        }
    }

}