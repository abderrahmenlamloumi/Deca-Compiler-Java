package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

import static fr.ensimag.deca.codegen.DefaultInitialization.defaultInitializationForType;

public class NewArray extends AbstractExpr {

    private final AbstractType innerType;
    private final AbstractExpr size;

    public NewArray(AbstractType arrayType, AbstractExpr size) {
        this.innerType = arrayType;
        this.size = size;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (!compiler.isFeatureEnabled(FeatureFlag.ARRAY)) {
            throw new ContextualError("Arrays are not supported in this version of Deca", getLocation());
        }
        Type valueType = innerType.verifyType(compiler);
        if (valueType.isVoid()){
            throw new ContextualError("Array type can't be void", getLocation());
        }
        Type sizeType = this.size.verifyExpr(compiler, localEnv, currentClass);
        if (!sizeType.isInt()) {
            throw new ContextualError("Array size must be an integer", this.size.getLocation());
        }
        Type type = new ArrayType(valueType);
        setType(type);
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        this.innerType.decompile(s);
        s.print("[");
        this.size.decompile(s);
        s.print("]");
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.innerType.prettyPrint(s, prefix, false);
        this.size.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.innerType.iter(f);
        this.size.iter(f);
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination){
        this.size.codeGenExpr(compiler, destination);
        compiler.addInstruction(new CMP(0, destination.getRegister()));
        compiler.addInstruction(new BLT(compiler.procedures.negativeArrayIndex()));

        GPRegister arrayRegister = destination.getRegister();
        GPRegister sizeRegister = Register.R0;
        compiler.addInstruction(new LOAD(destination.getRegister(), sizeRegister));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), sizeRegister)); // Allocate one more word for the length
        compiler.addInstruction(new NEW(sizeRegister, arrayRegister));
        compiler.addInstruction(new BOV(compiler.procedures.heapOverFlow()));
        compiler.addInstruction(new SUB(new ImmediateInteger(1), sizeRegister));
        compiler.addInstruction(new STORE(sizeRegister, new RegisterOffset(0, arrayRegister)));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), sizeRegister));

        GPRegister defaultValueRegister = Register.R1;
        DVal defaultValue = defaultInitializationForType(this.innerType.getType());
        compiler.addInstruction(new LOAD(defaultValue, defaultValueRegister));

        Label whileLabel = compiler.labeller.create("while_init_array");
        Label endLabel = compiler.labeller.create("end_init_array");

        compiler.addLabel(whileLabel);
        compiler.addInstruction(new CMP(new ImmediateInteger(1), sizeRegister));
        compiler.addInstruction(new BEQ(endLabel));
        compiler.addInstruction(new SUB(new ImmediateInteger(1), sizeRegister));
        compiler.addInstruction(new STORE(defaultValueRegister, new RegisterIndex(0, arrayRegister, sizeRegister)));
        compiler.addInstruction(new BRA(whileLabel));

        compiler.addLabel(endLabel);
    }

    public Type getType(){
        return this.innerType.getType();
    }
}
