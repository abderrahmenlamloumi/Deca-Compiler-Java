package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

class ConstantInt implements Value {

    private final int value;

    ConstantInt(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    @Override
    public ConditionCode cmp(Value other) {
        if (other instanceof ConstantInt) {
            ConstantInt that = (ConstantInt) other;
            return new ConditionCode(this.value == that.value, this.value < that.value);
        } else {
            return null;
        }
    }

    @Override
    public DVal getDVal() {
        return new ImmediateInteger(value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConstantInt)) return false;
        ConstantInt that = (ConstantInt) o;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }
}
