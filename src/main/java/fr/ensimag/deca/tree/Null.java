package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class Null extends AbstractExpr {
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type nullType = compiler.environmentType.NULL;
        setType(nullType);
        return nullType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        compiler.addInstruction(new LOAD(new NullOperand(), destination.getRegister()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }
}
