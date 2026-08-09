package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.This;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.Arrays;
import java.util.Collections;

public class CodeGenProcedures {

    private final LazyBlock stringConcat = new StringConcat();
    private final LazyBlock stringEquals = new StringEquals();
    private final LazyBlock ioError = errorHandler(new Label("error.io"), "I/O error: Failed to read formatted input.");
    private final LazyBlock divisionByZero = errorHandler(new Label("error.division"), "Arithmetic error: Attempt to divide by zero.");
    private final LazyBlock stackOverFlow = errorHandler(new Label("error.stack_overflow"), "Memory error: Stack Overflow");
    private final LazyBlock heapOverFlow = errorHandler(new Label("error.heap_overflow"), "Memory error: Heap Overflow");
    private final LazyBlock dereferencingNull = errorHandler(new Label("error.null"), "Memory error: Dereferencing a null value");
    private final LazyBlock floatOverflow = errorHandler(new Label("error.float_overflow"), "Overflow error: Attempt to manipulate an overflow value");
    private final LazyBlock missingReturn = errorHandler(new Label("error.missing_return"), "Logical error: Missing expected return");
    private final LazyBlock negativeArrayIndex = errorHandler(new Label("error.negative_array_index"), "Logical error: Negative array index");
    private final LazyBlock castError = errorHandler(new Label("error.cast"), "Cast error: Impossible to downcast to the target class, it is not an instance of the source class");

    private final LazyBlock objectEquals = new LazyBlock(new Label("code.Object.equals"), Arrays.asList(
            new LOAD(This.THIS_REGISTER, Register.R0),
            new CMP(new RegisterOffset(-3, Register.LB), Register.R0),
            new SEQ(Register.R0),
            new RTS()
    ));
    private final LazyBlock stringLength = new LazyBlock(new Label("code.string.length"), Arrays.asList(
            new LOAD(This.THIS_REGISTER, Register.R0),
            new LOAD(new RegisterOffset(0, Register.R0), Register.R0),
            new RTS()
    ));

    public Label stringConcat() {
        return this.stringConcat.useBlock();
    }

    public Label stringEquals() {
        return this.stringEquals.useBlock();
    }

    public Label stringLength() {
        return this.stringLength.useBlock();
    }

    public Label ioError() {
        return this.ioError.useBlock();
    }

    public Label divisionByZero() {
        return this.divisionByZero.useBlock();
    }

    public Label stackOverFlow() {
        return this.stackOverFlow.useBlock();
    }

    public Label heapOverFlow() {
        return this.heapOverFlow.useBlock();
    }

    public Label dereferencingNull() {
        return this.dereferencingNull.useBlock();
    }

    public Label floatOverflow() {
        return this.floatOverflow.useBlock();
    }

    public Label missingReturn() {
        return this.missingReturn.useBlock();
    }

    public Label negativeArrayIndex() {
        return this.negativeArrayIndex.useBlock();
    }

    public Label castError() {
        return this.castError.useBlock();
    }

    public Label objectEquals() {
        return this.objectEquals.useBlock();
    }


    public void codeGen(DecacCompiler program) {
        this.stringConcat.codeGen(program);
        this.stringEquals.codeGen(program);
        this.stringLength.codeGen(program);
        this.ioError.codeGen(program);
        this.divisionByZero.codeGen(program);
        this.stackOverFlow.codeGen(program);
        this.heapOverFlow.codeGen(program);
        this.dereferencingNull.codeGen(program);
        this.floatOverflow.codeGen(program);
        this.missingReturn.codeGen(program);
        this.castError.codeGen(program);
        this.objectEquals.codeGen(program);
        this.negativeArrayIndex.codeGen(program);
    }

    private static LazyBlock errorHandler(Label label, String message) {
        return new LazyBlock(label, Arrays.asList(
                new WSTR(message),
                new WNL(),
                new ERROR()
        ));
    }
}
