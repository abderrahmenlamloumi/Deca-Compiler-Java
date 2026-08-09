package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class Cast extends AbstractExpr implements PrintableTree{
    private final AbstractIdentifier type;
    private final AbstractExpr expression;
    boolean downcast;

    public Cast(AbstractIdentifier type, AbstractExpr expression) {
        this.type = type;
        this.expression = expression;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type Ltype = this.type.verifyType(compiler);
        Type Rtype = this.expression.verifyExpr(compiler, localEnv, currentClass);
        if (Rtype.isVoid()) {
            throw new ContextualError("Cannot cast void type", this.expression.getLocation());
        }
        if (Ltype.isVoid()) {
            throw new ContextualError("Cannot cast expression to type " + Ltype, this.type.getLocation());
        }

        boolean istranstypage = (Ltype.isFloat() && Rtype.isInt()) || (Rtype.isFloat() && Ltype.isInt());
        this.downcast = Rtype.isAssignableFrom(Ltype);

        if (!(Ltype.isAssignableFrom(Rtype) || this.downcast) && !istranstypage) {
            throw new ContextualError("Cannot cast expression of type " + Rtype + " to type " + Ltype, getLocation());
        }
        setType(Ltype);
        return Ltype;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination) {
        this.expression.codeGenExpr(compiler, destination);

        if (this.type.getType().isAssignableFrom(this.expression.getType())) {
            return;
        }

        // Float and int cast
        if (this.type.getType().isFloat()) {
            compiler.addInstruction(new FLOAT(destination.getRegister(), destination.getRegister()));
        } else if (this.type.getType().isInt()) {
            compiler.addInstruction(new INT(destination.getRegister(), destination.getRegister()));
            Label overflowHandler = compiler.labeller.create("cast_overflow_handler");
            Label end = compiler.labeller.create("cast_overflow_end");
            compiler.addInstruction(new BOV(overflowHandler));
            compiler.addInstruction(new BRA(end));

            compiler.addLabel(overflowHandler);
            Label overflowMaxInt = compiler.labeller.create("cast_overflow_max_int");
            compiler.addInstruction(new CMP(new ImmediateFloat(0.0f), destination.getRegister()));
            compiler.addInstruction(new BGE(overflowMaxInt));
            compiler.addInstruction(new LOAD(Integer.MIN_VALUE, destination.getRegister()));
            compiler.addInstruction(new BRA(end));
            compiler.addLabel(overflowMaxInt);
            compiler.addInstruction(new LOAD(Integer.MAX_VALUE, destination.getRegister()));

            compiler.addLabel(end);
        } else {
            // Class Downcast: Verification just like an instanceof with a few modifications
            Label goUp = compiler.labeller.create("Downcast_instanceof_go_up");
            Label end = compiler.labeller.create("Downcast_instanceof_end");
            compiler.addInstruction(new CMP(new NullOperand(), destination.getRegister()));
            compiler.addInstruction(new BEQ(end));

            DAddr expectedAddr = new RegisterOffset(this.type.getClassDefinition().getOffset(), Register.GB);
            GPRegister expected = Register.R0;
            compiler.addInstruction(new LEA(expectedAddr, expected));

            GPRegister reg = Register.R1;

            //save expression value
            compiler.addInstruction(new LOAD(destination.getRegister(), Register.R1));

            compiler.addLabel(goUp);
            compiler.addInstruction(new CMP(new NullOperand(), reg));
            compiler.addInstruction(new BEQ(compiler.procedures.castError()));
            compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg)); // Load vtable header
            compiler.addInstruction(new CMP(expected, reg));
            compiler.addInstruction(new BNE(goUp));
            compiler.addLabel(end);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        this.type.decompile(s);
        s.print(")");
        s.print("(");
        this.expression.decompile(s);
        s.print(")");

    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, false);
        this.expression.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.expression.iter(f);
    }

}
