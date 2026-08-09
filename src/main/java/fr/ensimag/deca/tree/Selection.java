package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Destination;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.feature.FeatureFlag;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class Selection extends AbstractLValue {

    private final AbstractIdentifier field;
    private final AbstractExpr object;

    public AbstractExpr getObject() {
        return object;
    }

    public AbstractIdentifier getField() {
        return field;
    }

    public Selection(AbstractExpr object, AbstractIdentifier field) {
        this.field = field;
        this.object = object;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        Type type = object.verifyExpr(compiler, localEnv, currentClass);
        if (!(type instanceof ClassType)) {
            if (compiler.isFeatureEnabled(FeatureFlag.ARRAY) && type.isArray()) {
                if (this.field.getName().getName().equals("length")) {
                    Type intType = compiler.environmentType.INT;
                    setType(intType);
                    field.setDefinition(new DummyDefinition(intType, Location.BUILTIN));
                    return intType;
                }
            }
            throw new ContextualError("Unable to select a field of a non-class type", this.object.getLocation());
        }
        ClassType classType = (ClassType) type;
        
        ExpDefinition def = classType.getDefinition().getMembers().get(field.getName());
        if (def == null) {
            throw new ContextualError("Object " + object.getType() + " does not have an attribute " + field.getName(), this.field.getLocation());
        }

        if (!(def instanceof FieldDefinition)) {
            throw new ContextualError("Error while trying to access an attribute", this.field.getLocation());
        }

        
        //We compare that the field is PROTECTED and that we access it from its class
        FieldDefinition fielddef = (FieldDefinition) def;
        Visibility visib = fielddef.getVisibility();
        //If currentClass is null (in main), you shouldn't be allowed to access protected fields
        //Expression type has to be a subtype of currentClass AND currentClass has to be a subtype of the Class with protected field
        if(visib == Visibility.PROTECTED
            && (currentClass == null
                || !currentClass.getType().isAssignableFrom(type)
                || !fielddef.getContainingClass().getType().isAssignableFrom(currentClass.getType())
                )
        ) {
                throw new ContextualError("Protected field cannot be accessed outside of its class", this.field.getLocation());
        }

        setType(def.getType());
        field.setDefinition(def);
        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.object.decompile(s);
        s.print(".");
        this.field.decompile(s);
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, Destination destination){

        object.codeGenExpr(compiler, destination);
        if (!compiler.getCompilerOptions().doesNoCheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), destination.getRegister()));
            compiler.addInstruction(new BEQ(compiler.procedures.dereferencingNull()));
        }
        if (this.object.getType().isArray()) {
            compiler.addInstruction(new LOAD(new RegisterOffset(0, destination.getRegister()), destination.getRegister()));
            return;
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(field.getFieldDefinition().getIndex(), destination.getRegister()), destination.getRegister()));
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.object.prettyPrint(s, prefix, false);
        this.field.prettyPrint(s, prefix, true);

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.object.iter(f);
        this.field.iter(f);

    }
}
