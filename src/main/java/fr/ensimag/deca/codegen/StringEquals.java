package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterIndex;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;

import java.util.Collections;

class StringEquals extends LazyBlock {

    StringEquals() {
        super(new Label("code.string.equals"), Collections.emptyList());
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        if (!isUsed()) {
            return;
        }
        compiler.addLabel(useBlock());
        GPRegister firstString = GPRegister.getR(1);
        GPRegister secondString = GPRegister.getR(2);
        GPRegister value = GPRegister.getR(3);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.SP), firstString));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.SP), secondString));
        compiler.addInstruction(new CMP(new NullOperand(), secondString));
        compiler.addInstruction(new BEQ(compiler.procedures.dereferencingNull()));

        Label differentStrings = compiler.labeller.create("different_strings");
        compiler.addInstruction(new LOAD(new RegisterOffset(0, firstString), GPRegister.R0));
        compiler.addInstruction(new CMP(new RegisterOffset(0, secondString), GPRegister.R0));
        compiler.addInstruction(new BNE(differentStrings));

        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        Label loopStart = compiler.labeller.create("loop_start");
        Label loopEnd = compiler.labeller.create("loop_end");
        compiler.addLabel(loopStart);
        compiler.addInstruction(new CMP(new RegisterOffset(0, firstString), GPRegister.R0));
        compiler.addInstruction(new BGT(loopEnd));
        compiler.addInstruction(new LOAD(new RegisterIndex(0, firstString, Register.R0), value));
        compiler.addInstruction(new CMP(new RegisterIndex(0, secondString,Register.R0), value));
        compiler.addInstruction(new BNE(differentStrings));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new BRA(loopStart));
        compiler.addLabel(loopEnd);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), GPRegister.R0));
        compiler.addInstruction(new RTS());

        compiler.addLabel(differentStrings);
        compiler.addInstruction(new LOAD(0, GPRegister.R0));
        compiler.addInstruction(new RTS());
    }
}
