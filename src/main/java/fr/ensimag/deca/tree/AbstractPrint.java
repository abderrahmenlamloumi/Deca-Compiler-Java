package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl10
 * @date 08/04/2025
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        for (AbstractExpr arg : getArguments().getList()) {
            Type argType = arg.verifyExpr(compiler, localEnv, currentClass);
            if (!argType.isString() && !argType.isInt() && !argType.isFloat()) {
                throw new ContextualError("Only string, int and float may be printed",
                        arg.getLocation());
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label restoreParametersLabel) {
        for (AbstractExpr a : getArguments().getList()) {
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
                if (getPrintHex()) {
                    compiler.addInstruction(new WFLOATX());
                } else {
                    compiler.addInstruction(new WFLOAT());
                }
            } else {
                throw new UnsupportedOperationException("Print not supported for type " + a.getType());
            }
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print");
        s.print(getSuffix());
        if (getPrintHex()) {
            s.print("x");
        }
        s.print("(");
        getArguments().decompile(s);
        s.print(")");
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
