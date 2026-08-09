package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;

import java.io.PrintStream;

public class Return extends AbstractInst {

    public static final GPRegister RETURN_REGISTER = Register.R0;

    private AbstractExpr expr;

    public Return(AbstractExpr expr) {
        this.expr = expr;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        this.expr = this.expr.verifyRValue(compiler, localEnv, currentClass, returnType, "return");
        if (returnType.isVoid()) {
            throw new ContextualError("Cannot return a void value", this.expr.getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label restoreParametersLabel) {
        DVal val = this.expr.codeGenDVal(compiler);
        compiler.addInstruction(new LOAD(val, RETURN_REGISTER));
        compiler.addInstruction(new BRA(restoreParametersLabel));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        this.expr.decompile(s);
        s.print(";");
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.expr.iter(f);
    }
}
