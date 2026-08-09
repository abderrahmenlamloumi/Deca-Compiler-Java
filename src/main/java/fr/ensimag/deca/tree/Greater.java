package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected BranchInstruction createBranchInstruction(Label trueLabel) {
        return new BGT(trueLabel);
    }

    @Override
    protected BranchInstruction createInvertedBranchInstruction(Label elseLabel) {
        return new BLE(elseLabel);
    }

    @Override
    protected Instruction createConditionCodeToBool(GPRegister destinationRegister) {
        return new SGT(destinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

}
