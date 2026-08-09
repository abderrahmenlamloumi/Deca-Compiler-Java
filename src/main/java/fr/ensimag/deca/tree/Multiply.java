package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl10
 * @date 08/04/2025
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected BinaryInstruction createArithOperation(GPRegister leftAndDestinationRegister, DVal rightRegister) {
        return new MUL(rightRegister, leftAndDestinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

}
