package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 * 
 * @author gl10
 * @date 08/04/2025
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected BranchInstruction createBranchInstruction(Label trueLabel) {
        return new BGE(trueLabel);
    }

    @Override
    protected BranchInstruction createInvertedBranchInstruction(Label elseLabel) {
        return new BLT(elseLabel);
    }

    @Override
    protected Instruction createConditionCodeToBool(GPRegister destinationRegister) {
        return new SGE(destinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
