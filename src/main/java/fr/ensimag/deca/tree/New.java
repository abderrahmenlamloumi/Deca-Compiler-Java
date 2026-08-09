package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr {

    private final AbstractIdentifier constructorIdent;

    public New(AbstractIdentifier constructorIdent) {
        this.constructorIdent = constructorIdent;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Definition def = compiler.environmentType.defOfType(constructorIdent.getName());
        if (def == null) {
            throw new ContextualError(constructorIdent.getName() + " does not refer to a value.", this.constructorIdent.getLocation());
        }
        if(!def.isClass()){
            throw new ContextualError("Incorrect use of new: " + constructorIdent.getName() + " is not a class.", this.constructorIdent.getLocation());
        } else {
            this.constructorIdent.setDefinition(def);
            setType(def.getType());
            return def.getType();
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        this.constructorIdent.decompile(s);
        s.print("()");
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.constructorIdent.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.constructorIdent.iter(f);
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination){
        int numberOfFields = this.constructorIdent.getClassDefinition().getNumberOfFields();
        compiler.addInstruction(new NEW(numberOfFields + 1, destination.getRegister()));

        if(!compiler.getCompilerOptions().doesNoCheck()){
            compiler.addInstruction(new BOV(compiler.procedures.heapOverFlow()));
        }
        compiler.addInstruction(new LEA(new RegisterOffset(constructorIdent.getClassDefinition().getOffset(), Register.GB), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, destination.getRegister())));
        compiler.stack.push(destination.getRegister());
        Label initLabel = compiler.labeller.create(this.constructorIdent.getClassDefinition());
        compiler.stack.bsr(initLabel);
        compiler.stack.pop(destination.getRegister());
    }

}
