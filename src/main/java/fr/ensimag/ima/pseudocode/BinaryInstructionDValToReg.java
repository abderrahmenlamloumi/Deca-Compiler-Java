package fr.ensimag.ima.pseudocode;

/**
 * Base class for instructions with 2 operands, the first being a
 * DVal, and the second a Register.
 *
 * @author Ensimag
 * @date 08/04/2025
 */
public class BinaryInstructionDValToReg extends BinaryInstruction {
    public BinaryInstructionDValToReg(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    @Override
    public DVal getOperand1() {
        return (DVal) super.getOperand1();
    }

    @Override
    public GPRegister getOperand2() {
        return (GPRegister) super.getOperand2();
    }
}
