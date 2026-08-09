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
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUB;

import java.util.Collections;

class StringConcat extends LazyBlock {

    StringConcat() {
        super(new Label("code.string.concat"), Collections.emptyList());
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        if (!isUsed()) {
            return;
        }
        compiler.addLabel(useBlock());
        GPRegister resultString = Register.R0;
        GPRegister firstString = Register.R1;
        GPRegister secondString = Register.getR(2);
        GPRegister length = Register.getR(3);
        GPRegister source = secondString;
        GPRegister resultIndex = Register.getR(4);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.SP), firstString));
        compiler.addInstruction(new CMP(new NullOperand(), firstString));
        compiler.addInstruction(new BEQ(compiler.procedures.dereferencingNull()));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.SP), secondString));
        compiler.addInstruction(new CMP(new NullOperand(), secondString));
        compiler.addInstruction(new BEQ(compiler.procedures.dereferencingNull()));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, firstString), length));
        compiler.addInstruction(new ADD(new RegisterOffset(0, secondString), length));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), length));
        compiler.addInstruction(new NEW(length, resultString));
        compiler.addInstruction(new BOV(compiler.procedures.heapOverFlow()));
        compiler.addInstruction(new SUB(new ImmediateInteger(1), length));
        compiler.addInstruction(new STORE(length, new RegisterOffset(0, resultString)));

        compiler.addInstruction(new LOAD(1, resultIndex));
        Label firstCopyLoop = new Label("first_copy_loop");
        Label firstCopyLoopEnd = new Label("first_copy_loop_end");
        compiler.addLabel(firstCopyLoop);
        compiler.addInstruction(new CMP(new RegisterOffset(0, firstString), resultIndex));
        compiler.addInstruction(new BGT(firstCopyLoopEnd));
        compiler.addInstruction(new LOAD(new RegisterIndex(0, firstString, resultIndex), source));
        compiler.addInstruction(new STORE(source, new RegisterIndex(0, resultString, resultIndex)));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), resultIndex));
        compiler.addInstruction(new BRA(firstCopyLoop));
        compiler.addLabel(firstCopyLoopEnd);

        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.SP), firstString));
        compiler.addInstruction(new LOAD(1, length));
        Label secondCopyLoop = new Label("second_copy_loop");
        Label secondCopyLoopEnd = new Label("second_copy_loop_end");
        compiler.addLabel(secondCopyLoop);
        compiler.addInstruction(new CMP(new RegisterOffset(0, firstString), length));
        compiler.addInstruction(new BGT(secondCopyLoopEnd));
        compiler.addInstruction(new LOAD(new RegisterIndex(0, firstString, length), source));
        compiler.addInstruction(new STORE(source, new RegisterIndex(0, resultString, resultIndex)));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), resultIndex));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), length));
        compiler.addInstruction(new BRA(secondCopyLoop));
        compiler.addLabel(secondCopyLoopEnd);
        compiler.addInstruction(new RTS());
    }
}
