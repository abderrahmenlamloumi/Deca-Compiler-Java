package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;

public class ConstantLabel implements Value {

    private final Label label;

    public ConstantLabel(Label label) {
        this.label = label;
    }

    @Override
    public DVal getDVal() {
        return new LabelOperand(this.label);
    }
}
