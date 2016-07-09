/// <reference path="InterpreterUtils.ts" />

class Parser {

    public parseExpression(expression: string, interpreter: Interpreter): any {
        // Here we kinda adapt to normal lua grammar
        // In lua x = y + 2 will be accepted, while y = 2 will not
        var abstractSyntaxTree = JSON.parse(JSON.stringify(luaparse.parse("x = " + expression)));
        if (abstractSyntaxTree.hasOwnProperty("error")) {
            throw new Error(abstractSyntaxTree.error + abstractSyntaxTree.message);
        }
        // And we ignore our fictive assignment when walking ast
        var root = abstractSyntaxTree.body[0].init[0];
        return this.calc(root, interpreter);
    }

    public parseFunction(functionStr: string, interpreter: Interpreter): Map<string> {
        //And here we do not need to adapt
        var abstractSyntaxTree = JSON.parse(JSON.stringify(luaparse.parse(functionStr)));
        if (abstractSyntaxTree.hasOwnProperty("error")) {
            throw new Error(abstractSyntaxTree.error + abstractSyntaxTree.message);
        }

        var variablesMap: Map<string> = {};

        for (var i = 0; i < abstractSyntaxTree.body.length; i++) {
            if (abstractSyntaxTree.body[i].type === "AssignmentStatement") {
                for (var j = 0; j < abstractSyntaxTree.body[i].variables.length; j++) {
                    variablesMap[abstractSyntaxTree.body[i].variables[j].name] =
                        this.calc(abstractSyntaxTree.body[i].init[0], interpreter);
                }
            } else {
                throw new Error("Unresolved input");
            }
        }
        
        return variablesMap;
    }

    private calc(node, interpreter: Interpreter): any {
        if (node.type === "BinaryExpression") {
            if (node.left != null && node.right != null) {
                return InterpreterUtils.getOperatorFunctionByString(node.operator)(
                    this.calc(node.left, interpreter), this.calc(node.right, interpreter));
            }
        }

        if (node.type === "Identifier") {
            return interpreter.getUserVariable(node.name);
        }

        if (node.type === "NumericLiteral") {
            return node.value;
        }

        throw new Error("Unresolved input");
    }
    
}