package fr.ensimag.deca.tree;


import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;

/**
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void verifyOperandTypes(Type leftType, Type rightType) throws ContextualError {
        if (!leftType.sameType(rightType) || !leftType.isFloat() && !leftType.isInt()) {
            throw new ContextualError("Compared operands must be of type float or int", getLocation());
        }
    }
}
