export class InterpreterUtils {

    public static getOperatorFunctionByString(operatorString: string) {
        switch (operatorString) {
            case "+":
                return (x, y) => x + y;
            case "-":
                return (x, y) => x - y;
            case "*":
                return (x, y) => x * y;
            case "/":
                return (x, y) => x / y;
            case "^":
                return (x, y) => x ^ y;
            case ">":
                return (x, y) => x > y;
            case "<":
                return (x, y) => x < y;
            case ">=":
                return (x, y) => x >= y;
            case "<=":
                return (x, y) => x <= y;
            case "==":
                return (x, y) => x == y;
            case "or":
            case "||":
                return (x, y) => x || y;
            case "and":
            case "&&":
                return (x, y) => x && y;
        }
    }

}