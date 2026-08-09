package fr.ensimag.deca.tree;


import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void verifyOperandTypes(Type leftType, Type rightType) throws ContextualError {
        if (!(leftType.isClassOrNull() && rightType.isClassOrNull())
                && !(leftType.sameType(rightType) && (leftType.isInt() || leftType.isFloat() || leftType.isBoolean()))) {
            throw new ContextualError("Types of operands are not compatible for exact comparison: "
                    + leftType + " and " + rightType, getLocation());
        }
    }
}
