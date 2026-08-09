package fr.ensimag.deca.codegen;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;

public final class DefaultInitialization {

    private DefaultInitialization() {}

    public static DVal defaultInitializationForType(Type type) {
        if (type.isInt() || type.isBoolean()) {
            return new ImmediateInteger(0);
        } else if (type.isFloat()) {
            return new ImmediateFloat(0);
        } else {
            return new NullOperand();
        }
    }
}
