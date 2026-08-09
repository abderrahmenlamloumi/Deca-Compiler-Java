package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import static fr.ensimag.deca.codegen.Utf8Scalar.asUtf8Scalars;

/**
 * String literal
 *
 * @author gl10
 * @date 08/04/2025
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type stringType = compiler.environmentType.STRING;
        setType(stringType);
        return stringType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        compiler.addComment("Generating string literal for " + decompile());
        // Memory layout:
        // The first word is reserved for the string length
        // The remaining words are the bytes of string.
        int size = 1 + this.value.length();
        GPRegister ptrRegister = destination.getRegister();
        GPRegister valueRegister = Register.R0;
        GPRegister indexRegister = Register.R1;

        // Allocate the char buffer
        compiler.addInstruction(new NEW(size, ptrRegister));
        compiler.addInstruction(new LOAD(this.value.length(), indexRegister));
        compiler.addInstruction(new STORE(indexRegister, new RegisterOffset(0, ptrRegister)));

        // Write the string bytes in the buffer
        compiler.addInstruction(new LOAD(1, indexRegister));
        for (int scalar : asUtf8Scalars(this.value)) {
            compiler.addInstruction(new LOAD(scalar, valueRegister));
            compiler.addInstruction(new STORE(valueRegister, new RegisterIndex(0, ptrRegister, indexRegister)));
            compiler.addInstruction(new ADD(new ImmediateInteger(1), indexRegister));
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label restoreParametersLabel) {
        // Nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("\"" + this.value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    public String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }

}
