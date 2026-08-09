package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.ControlDestination;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type booleanType = compiler.environmentType.BOOLEAN;
        setType(booleanType);
        return booleanType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        compiler.addInstruction(new LOAD(codeGenDVal(compiler), destination.getRegister()));
    }

    @Override
    protected DVal codeGenDVal(DecacCompiler compiler) {
        return new ImmediateInteger(this.value ? 1 : 0);
    }

    @Override
    protected void codeGenCmp(DecacCompiler compiler, Destination destination, ControlDestination control) {
        if (control.isBranchToElse() != this.value) {
            compiler.addInstruction(new BRA(control.getBranchLabel()));
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

}
