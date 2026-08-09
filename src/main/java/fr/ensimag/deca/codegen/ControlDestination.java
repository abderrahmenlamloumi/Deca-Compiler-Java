package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;


/**
 * Give code generators the information that they can branch immediately.
 */
public class ControlDestination {

    private final boolean branchToElse;
    private final Label branchLabel;

    /**
     * Create a control destination for the else {@link Label}.
     *
     * @param elseLabel Label of else, usually built using the {@link BlockLabeller}
     */
    public ControlDestination(Label elseLabel) {
        this(true, elseLabel);
    }

    /**
     * Internal constructor.
     * <p>
     * Since this class is immutable, all methods clone the object.
     *
     * @param branchToElse The branch meaning (is a branch to the true block or the else block)
     * @param branchLabel The branch label
     */
    private ControlDestination(boolean branchToElse, Label branchLabel) {
        this.branchToElse = branchToElse;
        this.branchLabel = branchLabel;
    }

    /**
     * Check if the destination is a branch to else
     *
     * @return {@code true} if this destination branches to an else block
     */
    public boolean isBranchToElse() {
        return this.branchToElse;
    }

    /**
     * Get the branch label.
     *
     * @return the label
     */
    public Label getBranchLabel() {
        return this.branchLabel;
    }

    /**
     * Negate the destination
     *
     * @return Return a new object with the same label but branchToElse is negated
     */
    public ControlDestination negate() {
        return new ControlDestination(!this.branchToElse, this.branchLabel);
    }
}
