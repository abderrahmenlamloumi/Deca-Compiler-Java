package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;

import java.util.List;

public class LazyBlock {

    private final Label label;
    private final List<Instruction> instructions;
    private boolean used;

    public LazyBlock(Label label, List<Instruction> instructions) {
        this.label = label;
        this.instructions = instructions;
    }

    public Label useBlock() {
        this.used = true;
        return this.label;
    }

    protected boolean isUsed() {
        return this.used;
    }

    public void codeGen(DecacCompiler compiler) {
        if (!this.used) {
            return;
        }
        compiler.addLabel(this.label);
        for (Instruction instruction : this.instructions) {
            compiler.addInstruction(instruction);
        }
    }
}
