package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.SGE;
import fr.ensimag.ima.pseudocode.instructions.SGT;
import fr.ensimag.ima.pseudocode.instructions.SLE;
import fr.ensimag.ima.pseudocode.instructions.SLT;
import fr.ensimag.ima.pseudocode.instructions.SNE;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WUTF8;

import java.util.ArrayList;
import java.util.List;

class Interpreter {

    private final List<Instruction> original;
    private final List<Instruction> optimized = new ArrayList<>();
    private final State state = new State();
    private int i;

    Interpreter(List<Instruction> original) {
        this.original = original;
    }

    static List<Instruction> optimize(List<Instruction> instructions) {
        Interpreter interpreter = new Interpreter(instructions);
        interpreter.optimizeInstructions(instructions);
        return interpreter.optimized;
    }

    void optimizeInstructions(List<Instruction> instructions) {
        for (this.i = 0; this.i < instructions.size(); this.i++) {
            Instruction instruction = instructions.get(this.i);
            try {
                handle(instruction);
            } catch (UnsupportedOperationException ex) {
                this.optimized.add(instruction);
            }
        }
    }

    private void handle(Instruction instruction) {
        if (instruction instanceof LOAD) {
            this.state.load((LOAD) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof ADD) {
            this.state.add((ADD) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof SUB) {
            this.state.sub((SUB) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof MUL) {
            this.state.mul((MUL) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof DIV) {
            this.state.div((DIV) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof OPP) {
            this.state.opp((OPP) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof INT) {
            this.state.convert((INT) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof FLOAT) {
            this.state.convert((FLOAT) instruction);
            this.optimized.add(instruction);
//        } else if (instruction instanceof WINT || instruction instanceof WFLOAT || instruction instanceof WFLOATX || instruction instanceof WUTF8) {
//            DVal dVal = this.state.getValue(Register.R1).getDVal();
//            if (dVal != null) {
//                this.optimized.add(new LOAD(dVal, Register.R1));
//            }
//            this.optimized.add(instruction);
        } else if (instruction instanceof STORE) {
            this.state.store((STORE) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof ADDSP) {
            this.state.addSP((ADDSP) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof NEW) {
            this.state.newObject((NEW) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof BOV) {
            this.state.bov((BOV) instruction);
            this.optimized.add(instruction);
        } else if (instruction instanceof CMP) {
            ConditionCode code = this.state.cmp((CMP) instruction);
            LOAD optimization;
            if (code != null && eliminateCondition(this.original.get(this.i + 1), code)) {
                this.i++;
            } else if (code != null && (optimization = replaceSet(this.original.get(this.i + 1), code)) != null) {
                this.optimized.add(optimization);
                this.state.load(optimization);
                this.i++;
            } else {
                this.optimized.add(instruction);
            }
        } else {
            this.optimized.add(instruction);
        }
    }

    private boolean eliminateCondition(Instruction instruction, ConditionCode code) {
        if (instruction instanceof BEQ) {
            return code.isNotEquals();
        } else if (instruction instanceof BNE) {
            return code.isEquals();
        } else if (instruction instanceof BLT) {
            return code.isGreaterThanOrEquals();
        } else if (instruction instanceof BLE) {
            return code.isGreaterThan();
        } else if (instruction instanceof BGT) {
            return code.isLessThanOrEquals();
        } else if (instruction instanceof BGE) {
            return code.isLessThan();
        }
        return false;
    }

    private LOAD replaceSet(Instruction instruction, ConditionCode code) {
        ImmediateInteger imm = null;
        if (instruction instanceof SEQ) {
            imm = new ImmediateInteger(code.isEquals() ? 1 : 0);
        } else if (instruction instanceof SNE) {
            imm = new ImmediateInteger(code.isNotEquals() ? 1 : 0);
        } else if (instruction instanceof SLT) {
            imm = new ImmediateInteger(code.isLessThan() ? 1 : 0);
        } else if (instruction instanceof SLE) {
            imm = new ImmediateInteger(code.isLessThanOrEquals() ? 1 : 0);
        } else if (instruction instanceof SGT) {
            imm = new ImmediateInteger(code.isGreaterThan() ? 1 : 0);
        } else if (instruction instanceof SGE) {
            imm = new ImmediateInteger(code.isGreaterThanOrEquals() ? 1 : 0);
        }
        if (imm == null) {
            return null;
        }
        return new LOAD(imm, ((UnaryInstructionToReg) instruction).getOperand());
    }
}
