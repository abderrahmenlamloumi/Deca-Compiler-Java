package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import java.util.List;

public class MethodCall extends AbstractExpr {
    // Example: a.foo() => a: callee
    private final AbstractExpr callee;
    private final AbstractIdentifier name;
    private final ListExpr parameters;

    public MethodCall(AbstractExpr callee, AbstractIdentifier name, ListExpr parameters) {
        this.callee = callee;
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type calleeType = callee.verifyExpr(compiler, localEnv, currentClass);
        ExpDefinition memberDef = findMember(compiler, calleeType);
        if (memberDef == null) {
            throw new ContextualError(this.name.getName() + " does not refer to any existing method", this.name.getLocation());
        }
        if (!(memberDef instanceof MethodDefinition)) {
            throw new ContextualError("Trying to use the " + memberDef.getNature() + " `" + this.name.getName() + "` as a method", getLocation());
        }
        MethodDefinition methodDefinition = (MethodDefinition) memberDef;

        name.setDefinition(methodDefinition);

        //Verify Parameters
        Signature methodSignature = methodDefinition.getSignature();
        if (parameters.size() != methodSignature.size()) {
            throw new ContextualError("Does not match number of Method parameters of " + this.name.getName() + " " + methodSignature.size() + " expected, but found " + parameters.size(), getLocation());
        }
        List<AbstractExpr> args = this.parameters.getList();
        for (int i = 0; i < args.size(); i++) {
            AbstractExpr arg = args.get(i);
            Type methodType = methodSignature.paramNumber(i);
            this.parameters.set(i, arg.verifyRValue(compiler, localEnv, currentClass, methodType, "parameter " + (i + 1)));
        }
        setType(methodDefinition.getReturnType());
        return methodDefinition.getReturnType();
    }

    protected ExpDefinition findMember(DecacCompiler compiler, Type calleeType) throws ContextualError {
        if (!(calleeType instanceof ClassType)) {
            if (compiler.getCompilerOptions().isEnabled(FeatureFlag.STRING_OBJECT) && calleeType.isString()) {
                return ((StringType) calleeType).getMembers().get(this.name.getName());
            }
            throw new ContextualError("Unable to call a method on the " + calleeType.getName().getName() + " primitive type", this.callee.getLocation());
        }
        ClassType classType = (ClassType) calleeType;
        return classType.getDefinition().getMembers().get(this.name.getName());
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler, Destination destination) {
        compiler.addComment("Method call:");
        int size = this.parameters.size() + 1; // +1 for the implicit "this" parameter
        compiler.stack.addStackPointer(size);

        // this
        this.callee.codeGenExpr(compiler, destination);
        compiler.addInstruction(new STORE(destination.getRegister(), new RegisterOffset(0, Register.SP)));

        // Parameters
        int i = -1;
        for (AbstractExpr expr : this.parameters.getList()) {
            expr.codeGenExpr(compiler, destination);
            compiler.addInstruction(new STORE(destination.getRegister(), new RegisterOffset(i, Register.SP)));
            i--;
        }

        // Get back "this"
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), destination.getRegister()));
        if (!compiler.getCompilerOptions().doesNoCheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), destination.getRegister()));
            compiler.addInstruction(new BEQ(compiler.procedures.dereferencingNull()));
        }

        MethodDefinition def = this.name.getMethodDefinition();
        if (def.getIndex() == 0) { // Not in vtable
            compiler.stack.bsr(def.getLabel());
        } else {
            compiler.addInstruction(new LOAD(new RegisterOffset(0, destination.getRegister()), destination.getRegister()));
            compiler.stack.bsr(new RegisterOffset(this.name.getMethodDefinition().getIndex(), destination.getRegister()), "calling " + this.callee.getType() + "." + this.name.getName().getName());
        }
        compiler.addInstruction(new LOAD(Return.RETURN_REGISTER, destination.getRegister()));
        compiler.stack.subStackPointer(size);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.callee.decompile(s);
        if (!this.callee.isImplicit()) {
            s.print(".");
        }
        this.name.decompile(s);
        s.print("(");
        this.parameters.decompile(s);
        s.print(")");
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.callee.prettyPrint(s, prefix, false);
        this.name.prettyPrint(s, prefix, false);
        this.parameters.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.callee.iter(f);
        this.name.iter(f);
        this.parameters.iter(f);
    }
}
