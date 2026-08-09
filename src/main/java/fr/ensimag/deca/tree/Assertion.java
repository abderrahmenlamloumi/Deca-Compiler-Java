package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ControlDestination;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Assertion extends AbstractInst implements PrintableTree{
    private final AbstractExpr condition;
    private final ListExpr expressions;


    public Assertion(AbstractExpr condition) {
        Validate.notNull(condition);
        this.condition = condition;
        this.expressions = new ListExpr();
    }

    public Assertion(AbstractExpr condition, ListExpr expressions) {
        Validate.notNull(condition);
        Validate.notNull(expressions);
        this.condition = condition;
        this.expressions = expressions;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        if (!compiler.isFeatureEnabled(FeatureFlag.ASSERT)) {
            throw new ContextualError("Assertion function is not supported in this version of Deca, please use decac -fassert to enable it", getLocation());
        }
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        for (AbstractExpr arg : expressions.getList()){
            Type argType = arg.verifyExpr(compiler, localEnv, currentClass);
            if (!argType.isString() && !argType.isInt() && !argType.isFloat()) {
                throw new ContextualError("Only string, int and float may be printed in assertion",
                        arg.getLocation());
            }

        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label restoreParametersLabel) {
        Label endLabel = compiler.labeller.create("endAssertion");
        ControlDestination control = new ControlDestination(endLabel).negate();
        compiler.stack.scoped((conditionDestination) -> this.condition.codeGenCmp(compiler, conditionDestination, control));
        compiler.addInstruction(new LOAD(new ImmediateInteger(this.getLocation().getLine()), Register.R1));
        compiler.addInstruction(new WSTR("Assertion failed at line: "));
        compiler.addInstruction(new WINT());
        compiler.addInstruction(new WNL());
        // We send the line number to the assertion error through R1 for a print
        for (AbstractExpr a : expressions.getList()) {
            if (a instanceof StringLiteral) {
                compiler.addInstruction(new WSTR(new ImmediateString(((StringLiteral) a).getValue())));
                continue;
            } else if (a.getType().isString()) {
                Label printStart = compiler.labeller.create("print_string_start");
                Label printEnd = compiler.labeller.create("print_string_end");
                GPRegister stringReg = compiler.stack.scoped(dest -> a.codeGenExpr(compiler, dest));
                compiler.addInstruction(new CMP(new NullOperand(), stringReg));
                compiler.addInstruction(new BEQ(compiler.procedures.dereferencingNull()));
                GPRegister indexRegister = GPRegister.R0;
                RegisterOffset length = new RegisterOffset(0, stringReg);
                compiler.addInstruction(new LOAD(1, indexRegister)); // one-based array (the first element is the string length)
                compiler.addLabel(printStart);
                compiler.addInstruction(new CMP(length, indexRegister));
                compiler.addInstruction(new BGT(printEnd));
                compiler.addInstruction(new LOAD(new RegisterIndex(0, stringReg, indexRegister), Register.R1));
                compiler.addInstruction(new WUTF8());
                compiler.addInstruction(new ADD(new ImmediateInteger(1), indexRegister));
                compiler.addInstruction(new BRA(printStart));
                compiler.addLabel(printEnd);
                continue;
            }
            DVal dVal = a.codeGenDVal(compiler);
            compiler.addInstruction(new LOAD(dVal, Register.R1));
            if (a.getType().isInt()) {
                compiler.addInstruction(new WINT());
            } else if (a.getType().isFloat()) {
                compiler.addInstruction(new WFLOAT());
            } else {
                throw new UnsupportedOperationException("Print not supported for type " + a.getType());
            }
        }
        compiler.addInstruction(new ERROR());
        compiler.addLabel(endLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("assert(");
        this.condition.decompile(s);
        s.println(");");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, true);
    }
}

