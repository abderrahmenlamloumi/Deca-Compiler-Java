package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

/**
 * @author gl10
 * @date 08/04/2025
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        if (getType().isString()) {
            compiler.addInstruction(new ADDSP(2));
            getLeftOperand().codeGenExpr(compiler, destination);
            compiler.addInstruction(new STORE(destination.getRegister(), new RegisterOffset(0, Register.SP)));
            getRightOperand().codeGenExpr(compiler, destination);
            compiler.addInstruction(new STORE(destination.getRegister(), new RegisterOffset(-1, Register.SP)));
            compiler.addInstruction(new BSR(compiler.procedures.stringConcat()));
            compiler.addInstruction(new SUBSP(2));
            compiler.addInstruction(new LOAD(Return.RETURN_REGISTER, destination.getRegister()));
        } else {
            super.codeGenExpr(compiler, destination);
        }
    }

    @Override
    protected BinaryInstruction createArithOperation(GPRegister leftAndDestinationRegister, DVal rightRegister) {
        return new ADD(rightRegister, leftAndDestinationRegister);
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }
}
