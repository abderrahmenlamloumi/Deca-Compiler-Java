package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;

public class MethodBodyAsm extends AbstractMethodBody {
    private final StringLiteral stringLiteral;


    public MethodBodyAsm(StringLiteral stringLiteral) {
        this.stringLiteral = stringLiteral;
    }

    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        this.stringLiteral.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    public void codeGenMethod(DecacCompiler compiler, MethodDefinition methodDefinition) {
        compiler.add(new InlinePortion(this.stringLiteral.getValue()));
        if (!compiler.getCompilerOptions().doesNoCheck()) {
            compiler.addInstruction(new BRA(compiler.procedures.missingReturn()));
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm");
        s.print("(");
        this.stringLiteral.decompile(s);
        s.print(")");
        s.println(";");
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.stringLiteral.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.stringLiteral.iterChildren(f);
    }


}
