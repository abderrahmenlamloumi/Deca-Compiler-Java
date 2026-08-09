package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected BranchInstruction createBranchInstruction(Label trueLabel) {
        return new BEQ(trueLabel);
    }

    @Override
    protected BranchInstruction createInvertedBranchInstruction(Label elseLabel) {
        return new BNE(elseLabel);
    }

    @Override
    protected Instruction createConditionCodeToBool(GPRegister destinationRegister) {
        return new SEQ(destinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }    
    
}
