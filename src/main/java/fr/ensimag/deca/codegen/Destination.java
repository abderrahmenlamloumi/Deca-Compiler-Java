package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;

import java.util.function.Consumer;

/**
 * Represent a non-scratch register where to store a value.
 * <p>
 * {@link Destination} objects may only be obtained from a
 * {@link VirtualStack} via {@link VirtualStack#scoped(Consumer)}.
 * <p>
 * A destination is bound to a Java call frame. This means that
 * it should not be stored and reused later as it will most likely
 * produce incorrect code.
 */
public class Destination {

    private final GPRegister register;

    /* package-private */ Destination(GPRegister register) {
        this.register = register;
    }

    public GPRegister getRegister() {
        return this.register;
    }
}
