package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class Instanceof extends AbstractExpr implements PrintableTree{
    private final AbstractExpr leftOperand;
    private final AbstractType rightOperand;

    public Instanceof(AbstractExpr leftOperand, AbstractType rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type leftOperandType = this.leftOperand.verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = this.rightOperand.verifyType(compiler);

        if (!(leftOperandType instanceof ClassType) && !leftOperandType.isNull()) {
            throw new ContextualError("Left operand of the instanceof needs to be an object", this.leftOperand.getLocation());
        }

        if (!(rightOperandType instanceof ClassType)) {
            throw new ContextualError("Right operand of the instance of needs to be a class", this.rightOperand.getLocation());
        }

        Type type = compiler.environmentType.BOOLEAN;
        setType(type);
        return type;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        this.leftOperand.codeGenExpr(compiler, destination);
        Label goUp = compiler.labeller.create("instanceof_go_up");
        Label notInstance = compiler.labeller.create("instanceof_not_instance");
        Label end = compiler.labeller.create("instanceof_end");
        DAddr expectedAddr = new RegisterOffset(getClassType().getDefinition().getOffset(), Register.GB);
        GPRegister expected = Register.R0;
        compiler.addInstruction(new LEA(expectedAddr, expected));

        GPRegister reg = destination.getRegister();
        compiler.addLabel(goUp);
        compiler.addInstruction(new CMP(new NullOperand(), reg));
        compiler.addInstruction(new BEQ(notInstance));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg)); // Load vtable header
        compiler.addInstruction(new CMP(expected, reg));
        compiler.addInstruction(new BNE(goUp));
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), reg));
        compiler.addInstruction(new BRA(end));
        compiler.addLabel(notInstance);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), reg));
        compiler.addLabel(end);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.leftOperand.decompile(s);
        s.print(" instanceof ");
        this.rightOperand.decompile(s);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.leftOperand.prettyPrint(s, prefix, false);
        this.rightOperand.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.leftOperand.iter(f);
        this.rightOperand.iter(f);
    }

    protected ClassType getClassType() {
        return (ClassType) rightOperand.getType();
    }
}
