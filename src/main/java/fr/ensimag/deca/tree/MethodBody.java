package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.PrintStream;

import static java.util.Objects.requireNonNull;

public class MethodBody extends AbstractMethodBody{

    private final ListDeclVar variables;
    private final ListInst instructions;
    private Type returnType = null;
    public MethodBody(ListDeclVar variables, ListInst instructions) {
        this.variables = variables;
        this.instructions = instructions;
    }

    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError{
        this.returnType = returnType;
        variables.verifyListDeclVariable(compiler, localEnv, currentClass);
        instructions.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void codeGenMethod(DecacCompiler compiler, MethodDefinition methodDefinition) {
        TSTO tsto = null;
        if (!compiler.getCompilerOptions().doesNoCheck()) {
            tsto = new TSTO(0);
            compiler.addInstruction(tsto);
            compiler.addInstruction(new BOV(compiler.procedures.stackOverFlow()));
        }
        if (!this.variables.isEmpty()) {
            compiler.stack.addStackPointer(this.variables.size());
        }
        int markerIndex = compiler.stack.initSaving(compiler);

        Label restoreParametersLabel = new Label("restore." + methodDefinition.getLabel());
        int offset = 0;
        for (AbstractDeclVar decl : this.variables.getList()) {
            VariableDefinition definition = requireNonNull(decl.getDefinition(), "the variable definition should exist");
            definition.setOperand(new RegisterOffset(++offset, Register.LB));
            decl.codeGenDecl(compiler);
        }

        for (AbstractInst instruction : instructions.getList()) {
            instruction.codeGenInst(compiler, restoreParametersLabel);
        }

        if (!this.returnType.isVoid() && !compiler.getCompilerOptions().doesNoCheck()) {
            compiler.addInstruction(new BRA(compiler.procedures.missingReturn()));
        }

        compiler.addLabel(restoreParametersLabel);
        compiler.stack.applyRegisterSavingAndRestore(compiler, markerIndex);
        if (tsto != null) {
            tsto.setOperand(new ImmediateInteger(compiler.stack.getMaxTemporariesAllocations() + this.variables.size()));
        }
        compiler.addInstruction(new RTS());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        this.variables.decompile(s);
        this.instructions.decompile(s);
        s.println("}");
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.variables.prettyPrint(s, prefix, false);
        this.instructions.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.variables.iter(f);
        this.instructions.iter(f);
    }
}
