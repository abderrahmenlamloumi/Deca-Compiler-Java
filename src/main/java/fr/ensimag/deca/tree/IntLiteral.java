package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl10
 * @date 08/04/2025
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type value = compiler.environmentType.INT;
        setType(value);
        return value;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        compiler.addInstruction(new LOAD(codeGenDVal(compiler), destination.getRegister()));
    }

    @Override
    protected DVal codeGenDVal(DecacCompiler compiler) {
        return new ImmediateInteger(this.value);
    }

    @Override
    public String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
