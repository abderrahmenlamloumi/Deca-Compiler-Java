package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class Index extends AbstractLValue {

    private final AbstractExpr array;
    private final AbstractExpr index;
    private Type innerType;

    public Index(AbstractExpr array, AbstractExpr index) {
        this.array = array;
        this.index = index;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (!compiler.isFeatureEnabled(FeatureFlag.ARRAY)) {
            throw new ContextualError("Arrays are not supported in this version of Deca", getLocation());
        }
        Type type = array.verifyExpr(compiler, localEnv, currentClass);
        if (!array.getType().isArray()){
            throw new ContextualError("Cannot index non-array type", getLocation());
        }
        Type indexType = index.verifyExpr(compiler, localEnv, currentClass);
        if (!indexType.isInt()){
            throw new ContextualError("Index value type must be int", getLocation());
        }

        Type innerType = ((ArrayType)type).getInnerType();
        setType(innerType);
        this.innerType = innerType;
        return innerType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.array.decompile(s);
        s.print("[");
        this.index.decompile(s);
        s.print("]");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.array.iter(f);
        this.index.iter(f);
    }

    @Override
    public void codeGenExpr(DecacCompiler compiler, Destination destination){
        this.array.codeGenExpr(compiler, destination);
        GPRegister indexRegister = compiler.stack.scoped(dest -> {
            this.index.codeGenExpr(compiler, dest);
            compiler.addInstruction(new ADD(new ImmediateInteger(1), dest.getRegister()));
        });
        compiler.addInstruction(new CMP(1, indexRegister));
        compiler.addInstruction(new BLT(compiler.procedures.negativeArrayIndex()));
        compiler.addInstruction(new LOAD(new RegisterIndex(0, destination.getRegister(), indexRegister), destination.getRegister()));
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.array.prettyPrint(s, prefix, false);
        this.index.prettyPrint(s, prefix, true);
    }

    public AbstractExpr getArray() {
        return this.array;
    }

    public AbstractExpr getIndex() {
        return this.index;
    }

    public Type getType() {
        return this.innerType;
    }
}
