package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;

/**
 * @author Ensimag
 * @date 08/04/2025
 */
public class STORE extends BinaryInstruction {
    public STORE(Register op1, DAddr op2) {
        super(op1, op2);
    }

    @Override
    public Register getOperand1() {
        return (Register) super.getOperand1();
    }

    @Override
    public DAddr getOperand2() {
        return (DAddr) super.getOperand2();
    }
}
