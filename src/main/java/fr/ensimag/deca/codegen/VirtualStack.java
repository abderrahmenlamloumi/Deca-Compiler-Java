package fr.ensimag.deca.codegen;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.util.function.Consumer;

/**
 * A stack view that places values in registers first and then
 * pushes them to the stack if there are no more registers.
 */
public class VirtualStack {

    public static final int RESERVED_REGISTER = 2;

    private final IMAProgram program;
    private final GPRegister lastRegister;
    private int stackPointer = 1;
    private int maxStackPointer = 1;
    private int GBIndex = 0;
    private int userPushes;
    private int maxTemporaries;

    public VirtualStack(IMAProgram program, CompilerOptions options) {
        this.program = program;
        this.lastRegister = Register.getR(options.getAvailableRegisters() - 1);
    }

    public int getGBIndex() {
        return GBIndex;
    }

    public int incGBIndex() {
        return ++this.GBIndex;
    }

    /**
     * @return The maximum number of temporary variables allocations at a given time during the execution of the program
     */
    public int getMaxTemporariesAllocations() {
        return this.maxTemporaries;
    }


    public int getStackPointer() {
        return stackPointer;
    }

    public int getMaxStackPointer() {
        return maxStackPointer;
    }

    /**
     * Get a register with a lifetime limited to the scope of a {@link Consumer}.
     * <p>
     * If there are enough registers available, the next one will be returned
     * directly. Otherwise, some instructions will be added to the program to
     * make a register temporarily available and to restore its value afterward.
     * <p>
     * The destination register passed to the consumer should be considered as
     * exclusive to the consumer. The consumer may not, for instance, refer to
     * a {@link Destination} that is not the one passed to it.
     *
     * @param consumer A function that will use the register.
     * @return A register where to read back the value. Note that it may not be the same as the one passed to the consumer.
     */
    public GPRegister scoped(Consumer<Destination> consumer) {
        ++this.stackPointer;
        if (this.stackPointer > maxStackPointer) {
            this.maxStackPointer = this.stackPointer;
        }
        if (this.stackPointer > this.lastRegister.getNumber()) {
            this.push(this.lastRegister);
            consumer.accept(new Destination(this.lastRegister));
            this.program.addInstruction(new LOAD(this.lastRegister, Register.R0));
            this.pop(this.lastRegister);
            --this.stackPointer;
            return Register.R0;
        } else {
            GPRegister register = Register.getR(this.stackPointer);
            consumer.accept(new Destination(register));
            --this.stackPointer;
            return register;
        }
    }

    public void push(GPRegister register) {
        this.program.addInstruction(new PUSH(register));
        ++this.userPushes;
        updateMaxTemporaries();
    }

    public void pop(GPRegister register) {
        this.program.addInstruction(new POP(register));
        --this.userPushes;
    }

    public void addStackPointer(int size) {
        this.program.addInstruction(new ADDSP(size));
        this.userPushes += size;
        updateMaxTemporaries();
    }

    public void subStackPointer(int size) {
        this.program.addInstruction(new SUBSP(size));
        this.userPushes -= size;
    }

    public void bsr(Label label) {
        this.userPushes += 2;
        updateMaxTemporaries();
        this.userPushes -= 2;
        this.program.addInstruction(new BSR(label));
    }

    public void bsr(DAddr addr, String comment) {
        this.userPushes += 2;
        updateMaxTemporaries();
        this.userPushes -= 2;
        this.program.addInstruction(new BSR(addr), comment);
    }

    private void updateMaxTemporaries() {
        this.maxTemporaries = Math.max(this.userPushes, this.maxTemporaries);
    }

    public int initSaving(DecacCompiler compiler) {
        this.userPushes = 0;
        this.maxTemporaries = 0;
        this.stackPointer = 1;
        this.maxStackPointer = 1;
        compiler.addComment("Save Registers");
        return compiler.placeMarker();
    }


    /**
     * Save in Stack all registers between R(RESERVED_REGISTER) and R(numberOfRegisters)
     * Restore all registers between R(RESERVED_REGISTER) and R(numberOfRegisters)
     *
     * @param compiler DecacCompiler
     */

    public void applyRegisterSavingAndRestore(DecacCompiler compiler, int indexToWriteSave) {
        int realRegistersNumber = Math.min(getMaxStackPointer(), lastRegister.getNumber());
        for (int i = RESERVED_REGISTER; i <= realRegistersNumber; ++i) {
            int computedOffset = (i - RESERVED_REGISTER);
            this.program.addInstructionAtIndex(new PUSH(Register.getR(i)), indexToWriteSave, computedOffset);
            ++this.maxTemporaries;
        }

        compiler.addComment("Restore Registers");
        for (int i = realRegistersNumber; i >= RESERVED_REGISTER; --i) {
            this.program.addInstruction(new POP(Register.getR(i)));
        }
        this.stackPointer = realRegistersNumber;
        this.maxStackPointer = getMaxStackPointer();
    }


    public GPRegister getFreeRegister() {
        return Register.R1;
    }
}
