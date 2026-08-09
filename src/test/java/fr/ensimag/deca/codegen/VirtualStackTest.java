package fr.ensimag.deca.codegen;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VirtualStackTest {

    @Test
    public void reuseR2() {
        IMAProgram program = new IMAProgram();
        VirtualStack stack = new VirtualStack(program, new CompilerOptions());
        Register r2 = Register.getR(2);
        // Create a scope
        Register result = stack.scoped(dest ->
                assertEquals(r2, dest.getRegister())
        );
        assertEquals(r2, result);
        // Create a new scope
        result = stack.scoped(dest ->
                assertEquals(r2, dest.getRegister())
        );
        assertEquals(r2, result);
        assertEquals(0, stack.getMaxTemporariesAllocations());
        assertEquals("", program.display());
    }

    @Test
    public void createPushPop() {
        IMAProgram program = new IMAProgram();
        VirtualStack stack = new VirtualStack(program, new CompilerOptions().withRegisters(4));
        Register r2 = Register.getR(2);
        Register r3 = Register.getR(3);
        Register result = stack.scoped(dest -> {
            assertEquals(r2, dest.getRegister());
            Register result2 = stack.scoped(dest2 -> {
                assertEquals(r3, dest2.getRegister());
                Register result3 = stack.scoped(dest3 -> {
                    assertEquals(r3, dest3.getRegister());
                });
                assertEquals(Register.R0, result3);
            });
            assertEquals(r3, result2);
        });
        assertEquals(r2, result);
        assertEquals(1, stack.getMaxTemporariesAllocations());
        assertEquals(
                "\tPUSH R3\n" +
                        "\tLOAD R3, R0\n" +
                        "\tPOP R3\n",
                program.display());
    }

    @Test
    public void keepMaxPushPop() {
        IMAProgram program = new IMAProgram();
        VirtualStack stack = new VirtualStack(program, new CompilerOptions());
        assertEquals(0, stack.getMaxTemporariesAllocations());
        stack.push(Register.R0);
        assertEquals(1, stack.getMaxTemporariesAllocations());
        stack.pop(Register.R0);
        stack.push(Register.R0);
        assertEquals(1, stack.getMaxTemporariesAllocations());

        stack.push(Register.R0);
        stack.push(Register.R0);
        stack.push(Register.R0);
        assertEquals(4, stack.getMaxTemporariesAllocations());

        stack.pop(Register.R0);
        stack.push(Register.R0);
        stack.push(Register.R0);
        assertEquals(5, stack.getMaxTemporariesAllocations());
    }

    @Test
    public void keepMaxAddStackPointer() {
        IMAProgram program = new IMAProgram();
        VirtualStack stack = new VirtualStack(program, new CompilerOptions());
        assertEquals(0, stack.getMaxTemporariesAllocations());
        stack.addStackPointer(20);
        assertEquals(20, stack.getMaxTemporariesAllocations());
        stack.subStackPointer(3);
        assertEquals(20, stack.getMaxTemporariesAllocations());

        stack.addStackPointer(5);
        stack.addStackPointer(1);
        assertEquals(23, stack.getMaxTemporariesAllocations());
    }

    @Test
    public void mixPushAndRegisterSpill() {
        IMAProgram program = new IMAProgram();
        VirtualStack stack = new VirtualStack(program, new CompilerOptions().withRegisters(4));
        assertEquals(0, stack.getMaxTemporariesAllocations());
        stack.push(Register.R1);
        assertEquals(1, stack.getMaxTemporariesAllocations());

        stack.scoped(dest -> stack.scoped(dest2 -> stack.scoped(dest3 -> {
            assertEquals(2, stack.getMaxTemporariesAllocations());
            stack.scoped(dest4 -> {
                assertEquals(3, stack.getMaxTemporariesAllocations());
                stack.addStackPointer(1);
                assertEquals(4, stack.getMaxTemporariesAllocations());
                stack.subStackPointer(1);
            });

            stack.addStackPointer(2);
            assertEquals(4, stack.getMaxTemporariesAllocations());
            stack.subStackPointer(2);
        })));
        stack.addStackPointer(3);
        assertEquals(4, stack.getMaxTemporariesAllocations());
    }
}
