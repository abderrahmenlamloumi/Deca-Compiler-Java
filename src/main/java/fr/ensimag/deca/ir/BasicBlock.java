package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicBlock {

    private final Set<Label> labels = new HashSet<>();
    private final List<AbstractLine> instructions = new ArrayList<>();
    private final List<Integer> successors = new ArrayList<>();

    public void addLabel(Label label) {
        this.labels.add(label);
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(new Line(instruction));
    }

    public void addInstructions(InlinePortion inline) {
        this.instructions.add(inline);
    }

    public void addSuccessor(int successor) {
        this.successors.add(successor);
    }

    public Set<Label> getLabels() {
        return labels;
    }

    public List<AbstractLine> getInstructions() {
        return instructions;
    }

    public boolean isOptimizable() {
        return instructions.stream().noneMatch(m -> m instanceof InlinePortion);
    }

    public List<Instruction> instructionIterator() {
        Stream<Instruction> stream = instructions.stream().filter(m -> m instanceof Line && ((Line) m).getInstruction() != null).map(m -> ((Line) m).getInstruction());
        return stream.collect(Collectors.toList());
    }
}
