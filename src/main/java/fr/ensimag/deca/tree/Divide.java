package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl10
 * @date 08/04/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        super.codeGenExpr(compiler, destination);
        if (getType().isInt() && !compiler.getCompilerOptions().doesNoCheck()) {
                compiler.addInstruction(new BOV(compiler.procedures.divisionByZero()));
        }
    }

    @Override
    protected BinaryInstruction createArithOperation(GPRegister leftAndDestinationRegister, DVal rightRegister) {
        if (getType().isInt()) {
            return new QUO(rightRegister, leftAndDestinationRegister);
        }
        return new DIV(rightRegister, leftAndDestinationRegister);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
