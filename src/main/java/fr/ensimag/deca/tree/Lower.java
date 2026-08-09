package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected BranchInstruction createBranchInstruction(Label trueLabel) {
        return new BLT(trueLabel);
    }

    @Override
    protected BranchInstruction createInvertedBranchInstruction(Label elseLabel) {
        return new BGE(elseLabel);
    }

    @Override
    protected Instruction createConditionCodeToBool(GPRegister destinationRegister) {
        return new SLT(destinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

}
