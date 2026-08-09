package fr.ensimag.ima.pseudocode;

/**
 *
 * @author Ensimag
 * @date 08/04/2025
 */
public class UnaryInstructionToReg extends UnaryInstruction {

    public UnaryInstructionToReg(GPRegister op) {
        super(op);
    }

    @Override
    public GPRegister getOperand() {
        return (GPRegister) super.getOperand();
    }
}
