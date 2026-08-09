package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class State {

    private final List<Value> global = new ArrayList<>();
    private final Value[] registers = new Value[16];
    private Value[] locals;

    private GPRegister flagsDueToRegister;

    State() {
        Arrays.fill(this.registers, UnknownValue.INSTANCE);
    }

    public void load(LOAD load) {
        Value operand1 = extractValue(load.getOperand1());
        this.registers[load.getOperand2().getNumber()] = operand1;
    }

    public void add(ADD add) {
        Value rightOperand = extractValue(add.getOperand1());
        Value leftOperand = extractValue(add.getOperand2());
        GPRegister destination = add.getOperand2();
        if (leftOperand instanceof ConstantInt && rightOperand instanceof ConstantInt) {
            this.registers[destination.getNumber()] = new ConstantInt(((ConstantInt) leftOperand).getValue() + ((ConstantInt) rightOperand).getValue());
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void sub(SUB sub) {
        Value rightOperand = extractValue(sub.getOperand1());
        Value leftOperand = extractValue(sub.getOperand2());
        GPRegister destination = sub.getOperand2();
        if (leftOperand instanceof ConstantInt && rightOperand instanceof ConstantInt) {
            this.registers[destination.getNumber()] = new ConstantInt(((ConstantInt) leftOperand).getValue() - ((ConstantInt) rightOperand).getValue());
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void mul(MUL sub) {
        Value rightOperand = extractValue(sub.getOperand1());
        Value leftOperand = extractValue(sub.getOperand2());
        GPRegister destination = sub.getOperand2();
        if (leftOperand instanceof ConstantInt && rightOperand instanceof ConstantInt) {
            this.registers[destination.getNumber()] = new ConstantInt(((ConstantInt) leftOperand).getValue() * ((ConstantInt) rightOperand).getValue());
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void div(DIV div) {
        Value rightOperand = extractValue(div.getOperand1());
        Value leftOperand = extractValue(div.getOperand2());
        GPRegister destination = div.getOperand2();
        if (leftOperand instanceof ConstantInt && rightOperand instanceof ConstantInt) {
            int divisor = ((ConstantInt) rightOperand).getValue();
            if (divisor == 0) {
                throw new UnsupportedOperationException("not implemented yet");
            }
            this.registers[destination.getNumber()] = new ConstantInt(((ConstantInt) leftOperand).getValue() / divisor);
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void opp(OPP opp) {
        Value source = extractValue(opp.getOperand1());
        GPRegister destination = opp.getOperand2();
        if (source instanceof ConstantInt) {
            this.registers[destination.getNumber()] = new ConstantInt(-((ConstantInt) source).getValue());
        } else if (source instanceof ConstantFloat) {
            this.registers[destination.getNumber()] = new ConstantFloat(-((ConstantFloat) source).getValue());
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void convert(INT insn) {
        Value source = extractValue(insn.getOperand1());
        GPRegister destination = insn.getOperand2();
        if (source instanceof ConstantFloat) {
            this.registers[destination.getNumber()] = new ConstantInt((int) ((ConstantFloat) source).getValue());
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void convert(FLOAT insn) {
        Value source = extractValue(insn.getOperand1());
        GPRegister destination = insn.getOperand2();
        if (source instanceof ConstantInt) {
            this.registers[destination.getNumber()] = new ConstantFloat((float) ((ConstantInt) source).getValue());
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void store(STORE store) {
        Value value = extractValue(store.getOperand1());
        if (store.getOperand2() instanceof RegisterOffset) {
            RegisterOffset registerOffset = (RegisterOffset) store.getOperand2();
            if (Register.LB.equals(registerOffset.getRegister())) {
                int index = registerOffset.getOffset() - 1;
                if (this.locals == null || index < 0 || index >= this.locals.length) {
                    throw new UnsupportedOperationException("not implemented yet");
                }
                this.locals[index] = value;
            } else if (Register.GB.equals(registerOffset.getRegister())) {
                this.global.add(value);
            } else {
                throw new UnsupportedOperationException("not implemented yet");
            }
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public void addSP(ADDSP addSP) {
        int size = addSP.getOperand().getValue();
        this.locals = new Value[size];
    }

    public void newObject(NEW instruction) {
        this.registers[instruction.getOperand2().getNumber()] = new Pointer();
        this.flagsDueToRegister = instruction.getOperand2();
    }

    public void bov(BOV bov) {
        if (this.flagsDueToRegister == null) {
            return;
        }
        Value value = this.registers[this.flagsDueToRegister.getNumber()];
        if (value instanceof Pointer) {
            ((Pointer) value).setNotNull();
        }
    }

    public ConditionCode cmp(CMP cmp) {
        Value value1 = extractValue(cmp.getOperand1());
        Value value2 = extractValue(cmp.getOperand2());
        if (value1 == null || value2 == null) return null;
        return value2.cmp(value1);
    }

    private Value extractValue(Operand operand) {
        if (operand instanceof ImmediateInteger) {
            return new ConstantInt(((ImmediateInteger) operand).getValue());
        } else if (operand instanceof ImmediateFloat) {
            return new ConstantFloat(((ImmediateFloat) operand).getValue());
        } else if (operand instanceof NullOperand) {
            return new ConstantNull();
        } else if (operand instanceof LabelOperand) {
            return new ConstantLabel(((LabelOperand) operand).getLabel());
        } else if (operand instanceof GPRegister) {
            return this.registers[((GPRegister) operand).getNumber()];
        } else if (operand instanceof RegisterOffset) {
            RegisterOffset offset = (RegisterOffset) operand;
            if (Register.LB.equals(offset.getRegister())) {
                int index = offset.getOffset() - 1;
                if (this.locals == null || index < 0 || index >= this.locals.length) return UnknownValue.INSTANCE;
                return this.locals[index];
            } else if (Register.GB.equals(offset.getRegister())) {
                return this.global.get(offset.getOffset() - 1);
            } else {
                throw new UnsupportedOperationException("not implemented yet");
            }
        } else {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }

    public Value getValue(GPRegister register) {
        return this.registers[register.getNumber()];
    }
}
