package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SLE;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected BranchInstruction createBranchInstruction(Label trueLabel) {
        return new BLE(trueLabel);
    }

    @Override
    protected BranchInstruction createInvertedBranchInstruction(Label elseLabel) {
        return new BGT(elseLabel);
    }

    @Override
    protected Instruction createConditionCodeToBool(GPRegister destinationRegister) {
        return new SLE(destinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

}
