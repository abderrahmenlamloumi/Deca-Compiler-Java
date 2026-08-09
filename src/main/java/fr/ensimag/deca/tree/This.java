package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

public class This extends AbstractExpr{

    private boolean implicit;
    static public RegisterOffset THIS_REGISTER = new RegisterOffset(-2, Register.LB);

    public This(boolean implicit) {
        this.implicit = implicit;
    }

    @Override
    public boolean isImplicit() {
        return implicit;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (currentClass != null) {
            Type thisType = currentClass.getType();
            setType(thisType);
            return thisType;
        } else {
            throw new ContextualError("Cannot use 'this' outside a class", getLocation());
        }
    }

    public void codeGenExpr(DecacCompiler compiler, Destination destination) {
        compiler.addInstruction(new LOAD(THIS_REGISTER, destination.getRegister()));
    }

    public void codeGenInst(DecacCompiler compiler, Destination destination) {
        //nothing to do when {this;} is met
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (!this.isImplicit()){
            s.print("this");
        }
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
