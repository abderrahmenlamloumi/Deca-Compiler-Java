package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.DefaultInitialization;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import static java.util.Objects.requireNonNull;

/**
 * @author gl10
 * @date 08/04/2025
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractType type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractType type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type varType = this.type.verifyType(compiler);
        if (varType.isVoid()) {
            throw new ContextualError("Variable cannot have type void", this.type.getLocation());
        }
        this.initialization.verifyInitialization(compiler, varType, localEnv, currentClass);
        try {
            VariableDefinition definition = new VariableDefinition(varType, this.varName.getLocation());
            localEnv.declare(this.varName.getName(), definition);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable " + this.varName.getName() + " is already declared", this.varName.getLocation());
        }
        this.varName.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    public void codeGenDecl(DecacCompiler compiler) {
        DAddr offset = requireNonNull(getDefinition().getOperand(), "variable location in memory is not set");
        if (this.initialization instanceof Initialization) {
            AbstractExpr initializer = ((Initialization) this.initialization).getExpression();
            GPRegister value = compiler.stack.scoped((destination) -> initializer.codeGenExpr(compiler, destination));
            compiler.addInstruction(new STORE(value, offset));
        } else {
            DVal value = DefaultInitialization.defaultInitializationForType(getDefinition().getType());
            compiler.addInstruction(new LOAD(value, Register.R0));
            compiler.addInstruction(new STORE(Register.R0, offset));
        }
    }

    @Override
    protected VariableDefinition getDefinition() {
        return this.varName.getVariableDefinition();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
