package fr.ensimag.deca.codegen;

import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;

public class VirtualTable {
    private List<Label> methodLabels = new ArrayList<>();

    public List<Label> getMethodLabels() {
        return methodLabels;
    }

    public void copy(VirtualTable vtable) {
        this.methodLabels = new ArrayList<>(vtable.getMethodLabels());
    }

    public void addLabel(int index, Label label) {
        if (index - 1 < this.methodLabels.size()) {
            this.methodLabels.set(index - 1, label);
        } else {
            this.methodLabels.add(label);
        }
    }

    public Label getLabel(int index) {
        return this.methodLabels.get(index);
    }

}
