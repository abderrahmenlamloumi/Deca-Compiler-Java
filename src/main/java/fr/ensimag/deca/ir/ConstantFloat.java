package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;

class ConstantFloat implements Value {

    private final float value;

    ConstantFloat(float value) {
        this.value = value;
    }

    float getValue() {
        return value;
    }

    @Override
    public ConditionCode cmp(Value other) {
        if (other instanceof ConstantFloat) {
            ConstantFloat that = (ConstantFloat) other;
            return new ConditionCode(
                Float.compare(this.value, that.value) == 0,
                Float.compare(this.value, that.value) < 0
            );
        } else {
            return null; // Comparison with non-constant float is not defined
        }
    }

    @Override
    public DVal getDVal() {
        return new ImmediateFloat(value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConstantFloat)) return false;
        ConstantFloat that = (ConstantFloat) o;
        return Float.compare(this.value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }
}
