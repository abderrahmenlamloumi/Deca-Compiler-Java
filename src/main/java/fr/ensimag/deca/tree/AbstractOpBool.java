package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftOperandType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!leftOperandType.sameType(rightOperandType) || !rightOperandType.isBoolean()) {
            throw new ContextualError("All Operands should be boolean", getLocation());
        }
        Type value = compiler.environmentType.BOOLEAN;
        setType(value);
        return value;
    }


}
