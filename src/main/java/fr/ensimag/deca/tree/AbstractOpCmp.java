package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.ControlDestination;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftOperandType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (leftOperandType.isInt() && rightOperandType.isFloat()) {
            setLeftOperand(new ConvFloat(getLeftOperand()));
            leftOperandType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        if (leftOperandType.isFloat() && rightOperandType.isInt()) {
            setRightOperand(new ConvFloat(getRightOperand()));
            rightOperandType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }

        verifyOperandTypes(leftOperandType, rightOperandType);
        Type value = compiler.environmentType.BOOLEAN;
        setType(value);
        return value;
    }


    protected abstract void verifyOperandTypes(Type leftType, Type rightType) throws ContextualError;

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        getLeftOperand().codeGenExpr(compiler, destination);
        DVal rightOperand = getRightOperand().codeGenDVal(compiler);
        compiler.addInstruction(new CMP(rightOperand, destination.getRegister()));
        compiler.addInstruction(createConditionCodeToBool(destination.getRegister()));
    }

    @Override
    protected void codeGenCmp(DecacCompiler compiler, Destination destination, ControlDestination control) {
        getLeftOperand().codeGenExpr(compiler, destination);
        DVal rightOperand = getRightOperand().codeGenDVal(compiler);
        compiler.addInstruction(new CMP(rightOperand, destination.getRegister()));
        compiler.addInstruction(control.isBranchToElse()
                ? createInvertedBranchInstruction(control.getBranchLabel())
                : createBranchInstruction(control.getBranchLabel()));
    }

    protected abstract BranchInstruction createBranchInstruction(Label trueLabel);

    protected abstract BranchInstruction createInvertedBranchInstruction(Label elseLabel);

    protected abstract Instruction createConditionCodeToBool(GPRegister destinationRegister);
}
