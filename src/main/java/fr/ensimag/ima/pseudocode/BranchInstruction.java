package fr.ensimag.ima.pseudocode;

/**
 *
 * @author Ensimag
 * @date 08/04/2025
 */
public class BranchInstruction extends UnaryInstruction {

    public BranchInstruction(Label op) {
        super(op);
    }

    @Override
    public Label getOperand() {
        return (Label) super.getOperand();
    }
}
