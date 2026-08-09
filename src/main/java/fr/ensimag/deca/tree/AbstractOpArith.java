package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BOV;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (leftType.isInt() && rightType.isFloat()) {
            setLeftOperand(new ConvFloat(getLeftOperand()));
            leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        if (rightType.isInt() && leftType.isFloat()) {
            setRightOperand(new ConvFloat(getRightOperand()));
            rightType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        if (!leftType.sameType(rightType)) {
            throw new ContextualError("Both sides of a binary expression must have the same type.", getLocation());
        }
        if (!leftType.isInt() && !rightType.isFloat() && !(compiler.isFeatureEnabled(FeatureFlag.STRING_OBJECT) && leftType.isString() && this instanceof Plus)) {
            throw new ContextualError("Arithmetic operations are only applicable to int and float types", getLocation());
        }
        setType(leftType);
        return leftType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        // noinspection UnnecessaryLocalVariable just a little renaming
        Destination leftDestination = destination;
        getLeftOperand().codeGenExpr(compiler, leftDestination);
        DVal rightOperand = getRightOperand().codeGenDVal(compiler);
        compiler.addInstruction(createArithOperation(leftDestination.getRegister(), rightOperand));
        if (getType().isFloat() && !compiler.getCompilerOptions().doesNoCheck()) {
            compiler.addInstruction(new BOV(compiler.procedures.floatOverflow()));
        }
    }

    protected abstract BinaryInstruction createArithOperation(GPRegister leftAndDestinationRegister, DVal rightRegister);
}
