package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlFlowGraph {

    public IMAProgram buildCfg(IMAProgram program) {
        // Merge labels
        Map<Label, Integer> labelToIndex = new HashMap<>();
        List<AbstractLine> lines = program.getLines();
        for (int i = 0; i < lines.size(); i++) {
            AbstractLine abstractLine = lines.get(i);
            if (!(abstractLine instanceof Line)) {
                continue;
            }
            Label label = ((Line) abstractLine).getLabel();
            if (label != null) {
                int start = firstInstructionIndex(lines, i);
                if (start != 1) {
                    labelToIndex.put(label, start);
                }
            }
        }

        // Find labels that are effectively used
        Map<Integer, BasicBlock> leaders = new HashMap<>();
        leaders.put(0, new BasicBlock());
        for (int i = 0; i < lines.size(); i++) {
            AbstractLine line = lines.get(i);
            if (!(line instanceof Line)) {
                continue;
            }
            Line currentLine = (Line) line;
            Instruction instruction = currentLine.getInstruction();
            if (instruction == null) {
                continue;
            }
            if (instruction instanceof BranchInstruction) {
                if (instruction instanceof BOV) {
                    Label label = ((BOV) instruction).getOperand();
                    int index = labelToIndex.getOrDefault(label, -1);
                    if (index == -1) {
                        throw new IllegalStateException("Label " + label + " not found in program");
                    }
                    leaders.computeIfAbsent(index, block -> new BasicBlock()).addLabel(label);
                    continue; // BOV is only used for errors
                }
                Label label = ((BranchInstruction) instruction).getOperand();
                int index = labelToIndex.getOrDefault(label, -1);
                if (index == -1) {
                    throw new IllegalStateException("Label " + label + " not found in program");
                }
                leaders.computeIfAbsent(index, block -> new BasicBlock()).addLabel(label);
                if (!(instruction instanceof BRA)) {
                    int start = firstInstructionIndex(lines, i + 1);
                    if (start == -1) {
                        throw new IllegalStateException("No code to fallthrough");
                    }
                    leaders.computeIfAbsent(start, block -> new BasicBlock());
                }
            } else if (instruction instanceof BinaryInstruction) {
                BinaryInstruction binary = (BinaryInstruction) instruction;
                Operand operand = binary.getOperand1();
                if (operand instanceof LabelOperand) {
                    Label label = ((LabelOperand) operand).getLabel();
                    int index = labelToIndex.getOrDefault(label, -1);
                    if (index == -1) {
                        throw new IllegalStateException("Label " + label + " not found in program");
                    }
                    leaders.computeIfAbsent(index, block -> new BasicBlock()).addLabel(label);
                }
                operand = binary.getOperand2();
                if (operand instanceof LabelOperand) {
                    Label label = ((LabelOperand) operand).getLabel();
                    int index = labelToIndex.getOrDefault(label, -1);
                    if (index == -1) {
                        throw new IllegalStateException("Label " + label + " not found in program");
                    }
                    leaders.computeIfAbsent(index, block -> new BasicBlock()).addLabel(label);
                }
            } else if (instruction instanceof UnaryInstruction) {
                UnaryInstruction unary = (UnaryInstruction) instruction;
                Operand operand = unary.getOperand();
                if (operand instanceof LabelOperand) {
                    Label label = ((LabelOperand) operand).getLabel();
                    int index = labelToIndex.getOrDefault(label, -1);
                    if (index == -1) {
                        throw new IllegalStateException("Label " + label + " not found in program");
                    }
                    leaders.computeIfAbsent(index, block -> new BasicBlock()).addLabel(label);
                }
            }
        }

        BasicBlock current = leaders.get(0);
        for (int i = 0; i < lines.size(); i++) {
            BasicBlock leader = leaders.get(i);
            if (leader != null) {
                current = leader;
            }
            if (current == null) {
                continue;
            }
            AbstractLine line = lines.get(i);
            if (line instanceof InlinePortion) {
                current.addInstructions((InlinePortion) line);
                continue;
            }
            Instruction instruction = ((Line) line).getInstruction();
            if (instruction != null) {
                current.addInstruction(instruction);
            }
            if (instruction instanceof BranchInstruction && !(instruction instanceof BOV)) {
                Label label = ((BranchInstruction) instruction).getOperand();
                int index = labelToIndex.getOrDefault(label, -1);
                if (index == -1) {
                    throw new IllegalStateException("Label " + label + " not found in program");
                }
                current.addSuccessor(index);
                if (!(instruction instanceof BRA)) {
                    int start = firstInstructionIndex(lines, i + 1);
                    if (start == -1) {
                        throw new IllegalStateException("No code to fallthrough");
                    }
                    current.addSuccessor(start);
                }
                current = null;
            }
        }

        IMAProgram reduced = new IMAProgram();
        for (int i = 0; i < lines.size(); i++) {
            BasicBlock leader = leaders.get(i);
            if (leader != null) {
                for (Label label : leader.getLabels()) {
                    reduced.addLabel(label);
                }
                if (leader.isOptimizable()) {
                    for (Instruction instruction : Interpreter.optimize(leader.instructionIterator())) {
                        reduced.addInstruction(instruction);
                    }
                } else {
                    reduced.addAll(leader.getInstructions());
                }
            }
        }
        return reduced;
    }

    public static int firstInstructionIndex(List<AbstractLine> lines, int starting) {
        while (starting < lines.size()) {
            AbstractLine abstractLine = lines.get(starting);
            if (!(abstractLine instanceof Line)) {
                return starting;
            }
            Line line = (Line) abstractLine;
            if (line.getInstruction() != null) {
                return starting;
            }
            ++starting;
        }
        return -1;
    }
}
