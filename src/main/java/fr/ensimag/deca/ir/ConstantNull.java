package fr.ensimag.deca.ir;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;

class ConstantNull implements Value {

    @Override
    public DVal getDVal() {
        return new NullOperand();
    }

    @Override
    public ConditionCode cmp(Value other) {
        if (other instanceof ConstantNull) {
            return new ConditionCode(true, false); // Null is equal to null
        } else if (other instanceof Pointer && !((Pointer) other).isNullable()) {
            return new ConditionCode(false, false); // Null is not equal to non-null pointer
        } else {
            return null;
        }
    }
}
