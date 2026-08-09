package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

import static fr.ensimag.deca.codegen.DefaultInitialization.defaultInitializationForType;

public class DeclField extends Tree {

    private final Visibility visibility;
    private final AbstractType type;
    private final AbstractIdentifier fieldName;
    private final AbstractInitialization initialization;

    public DeclField(Visibility visibility, AbstractType type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        this.visibility = visibility;
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }

    protected FieldDefinition definition;

    public FieldDefinition getDefinition() {
        return this.definition;
    }

    public Symbol getSymbol() {
        return fieldName.getName();
    }

    public Type getType() {
        return type.getType();
    }

    public AbstractInitialization getInitialization() {
        return this.initialization;
    }

    protected void verifyDeclFieldPass2(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type varType = this.type.verifyType(compiler);
        if (varType.isVoid()) {
            throw new ContextualError("Field cannot have type void", this.type.getLocation());
        }
        if (currentClass.getSuperClass() != null) {
            ExpDefinition superDef = currentClass.getSuperClass().getMembers().get(this.fieldName.getName());
            if (superDef != null && !(superDef instanceof FieldDefinition)) {
                throw new ContextualError("Field " + this.fieldName.getName() + " cannot shadow a " + superDef.getNature() + " in the superclass", this.fieldName.getLocation());
            }
        }
        try {
            FieldDefinition definition = new FieldDefinition(varType, this.getLocation(), this.visibility, currentClass, currentClass.incNumberOfFields());
            localEnv.declare(this.fieldName.getName(), definition);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Field " + this.fieldName.getName() + " is already declared", this.fieldName.getLocation());
        }
        this.fieldName.verifyExpr(compiler, localEnv, currentClass);
    }

    protected void verifyDeclFieldPass3(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type varType = this.type.getType();
        this.initialization.verifyInitialization(compiler, varType, localEnv, currentClass);
    }

    public void codeGenInit(DecacCompiler compiler) {
        DVal value = ((Initialization) this.initialization).getExpression().codeGenDVal(compiler);
        compiler.addInstruction(new LOAD(value, Register.R0));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(this.fieldName.getFieldDefinition().getIndex(), Register.R1)));
    }

    public void codeGenDefaultInit(DecacCompiler compiler) {
        DVal value = defaultInitializationForType(this.type.getType());
        compiler.addInstruction(new LOAD(value, Register.R0));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(this.fieldName.getFieldDefinition().getIndex(), Register.R1)));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (this.visibility == Visibility.PROTECTED) {
            s.print("protected ");
        }
        this.type.decompile(s);
        s.print(" ");
        this.fieldName.decompile(s);
        this.initialization.decompile(s);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, false);
        this.fieldName.prettyPrint(s, prefix, false);
        this.initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.fieldName.iter(f);
        this.initialization.iter(f);
    }

}
