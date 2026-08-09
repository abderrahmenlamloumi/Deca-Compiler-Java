package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.ControlDestination;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     *
     * implements non-terminals "expr" and "lvalue"
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     *
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @param rValueType a display name for the rvalue in case an error occurs
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType,
            String rValueType)
            throws ContextualError {
        Type actualType = verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr result = this;
        if (actualType.isInt() && expectedType.isFloat()) {
            result = new ConvFloat(this);
            actualType = result.verifyExpr(compiler, localEnv, currentClass);
        }
        if (!expectedType.isAssignableFrom(actualType)) {
            throw new ContextualError("Incompatible types for " + rValueType + ": expected " + expectedType + ", got " + actualType, getLocation());
        }
        return result;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        verifyExpr(compiler, localEnv, currentClass);

    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type actualType = verifyExpr(compiler, localEnv, currentClass);
        if (!actualType.isBoolean()) {
            throw new ContextualError("Condition must be of type boolean", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label restoreParametersLabel) {
        codeGenDVal(compiler);
    }

    /**
     * Generate code to place the expression final value in the given register.
     *
     * @param compiler    Current compiler state.
     * @param destination The register where to put the value in.
     */
    protected /* abstract */ void codeGenExpr(DecacCompiler compiler, Destination destination) {
        throw new UnsupportedOperationException("not yet implemented for " + getClass());
    }

    /**
     * Generate code for an immediate value.
     *
     * This generates a value that may allocate a {@link fr.ensimag.ima.pseudocode.GPRegister register}. This implies
     * that this value becomes <strong>invalid after any call to a codegen method</strong> (i.e.
     * {@link #codeGenExpr(DecacCompiler, Destination)} or any other method that adds code).
     *
     * @param compiler Current compiler state.
     * @return An immediate value.
     */
    protected DVal codeGenDVal(DecacCompiler compiler) {
        // If this method is not specialized, it defaults to allocating a register with a short lifetime.
        // Because the user of this method is expected to use this method immediately, it is safe
        // to assume that, even though the destination has expired after this method, the register
        // will still contain the appropriate value.
        return compiler.stack.scoped((destination) -> codeGenExpr(compiler, destination));
    }

    protected void codeGenCmp(DecacCompiler compiler, Destination destination, ControlDestination control) {
        codeGenExpr(compiler, destination);
        int boolValue = control.isBranchToElse() ? 0 : 1;
        compiler.addInstruction(new CMP(boolValue, destination.getRegister()));
        compiler.addInstruction(new BEQ(control.getBranchLabel()));
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    public void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
