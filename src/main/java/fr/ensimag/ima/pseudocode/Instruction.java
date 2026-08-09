package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * IMA instruction.
 *
 * @author Ensimag
 * @date 08/04/2025
 */
public abstract class Instruction {
    String getName() {
        return this.getClass().getSimpleName();
    }
    abstract void displayOperands(PrintStream s);
    void display(PrintStream s) {
        s.print(getName());
        displayOperands(s);
    }
}
