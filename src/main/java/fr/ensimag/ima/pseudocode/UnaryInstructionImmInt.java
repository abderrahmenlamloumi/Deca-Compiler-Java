package fr.ensimag.ima.pseudocode;

/**
 *
 * @author Ensimag
 * @date 08/04/2025
 */
public abstract class UnaryInstructionImmInt extends UnaryInstruction {

    protected UnaryInstructionImmInt(ImmediateInteger operand) {
        super(operand);
    }

    protected UnaryInstructionImmInt(int i) {
        super(new ImmediateInteger(i));
    }

    @Override
    public ImmediateInteger getOperand() {
        return (ImmediateInteger) super.getOperand();
    }
}
