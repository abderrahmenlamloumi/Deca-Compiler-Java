package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl10
 * @date 08/04/2025
 */
public class DeclClass extends AbstractDeclClass {


    private final AbstractIdentifier name;
    private final AbstractIdentifier superclass;
    private final ListDeclField attributes;
    private final ListDeclMethod methods;
    private ClassDefinition definition = null;


    public DeclClass(AbstractIdentifier name, AbstractIdentifier superclass, ListDeclField attributes, ListDeclMethod methods) {
        Validate.notNull(name);
        Validate.notNull(attributes);
        Validate.notNull(methods);
        this.name = name;
        this.superclass = superclass;
        this.attributes = attributes;
        this.methods = methods;
    }

    public ListDeclMethod getMethods() {
        return this.methods;
    }

    public AbstractIdentifier getSuperclass() {
        return superclass;
    }

    public AbstractIdentifier getName() {
        return name;
    }

    public ClassDefinition getDefinition() {
        return definition;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        this.name.decompile(s);
        if (this.superclass != null) {
            s.print(" extends ");
            this.superclass.decompile(s);
        }
        s.println(" {");
        s.indent();
        this.attributes.decompile(s);
        this.methods.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //si la definition de la classe existe deja
        if (compiler.environmentType.exist(name.getName())) {
            throw new ContextualError("Class already exist", getLocation());
        }
        //sinon, ajouter la definition a l'environnement type
        ClassDefinition superClass = compiler.environmentType.OBJECT.getDefinition();
        if (this.superclass != null) {
            this.superclass.verifyType(compiler);
            Definition superDef = this.superclass.getDefinition();
            if (!(superDef instanceof ClassDefinition)) {
                throw new ContextualError("Superclass " + this.superclass.getName().getName() + " is not a class", this.superclass.getLocation());
            }
            superClass = (ClassDefinition) superDef;
        }
        ClassType cType = new ClassType(name.getName(), getLocation(), superClass);
        name.setType(cType);
        name.setDefinition(cType.getDefinition());
        this.definition = cType.getDefinition();
        compiler.environmentType.defineClass(name.getName(), this.definition);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        this.definition.copySuperClassMembers();
        //creer l'env
        EnvironmentExp Envexp = this.definition.getMembers();
        //ajouter toutes les declarations dans l'env
        for (DeclField field : attributes.getList()) {
            field.verifyDeclFieldPass2(compiler, Envexp, this.definition);
        }
        for (DeclMethod method : methods.getList()) {
            EnvironmentExp methodEnv = new EnvironmentExp(Envexp);
            method.verifyDeclMethodPass2(compiler, Envexp, this.definition, methodEnv);
        }
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        EnvironmentExp Envexp = this.definition.getMembers();
        for (DeclField field : attributes.getList()) {
            field.verifyDeclFieldPass3(compiler, Envexp, this.definition);
        }
        for (DeclMethod method : methods.getList()) {
            method.verifyDeclMethodPass3(compiler, Envexp, definition);
        }
    }

    @Override
    public String printNodeLine(PrintStream s, String prefix, boolean last, boolean inlist) {
        return super.printNodeLine(s, prefix, last, inlist, getClass().getSimpleName() + " " + name.getName().getName());
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        if (superclass != null) {
            superclass.prettyPrint(s, prefix, false);
        }
        attributes.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.name.iter(f);
        if (this.superclass != null) {
            this.superclass.iter(f);
        }
        this.attributes.iter(f);
        this.methods.iter(f);
    }

    @Override
    protected void codeGenClass(DecacCompiler compiler) {
        Label initLabel = compiler.labeller.create(this.definition);
        compiler.addLabel(initLabel);

        int markerIndex = compiler.stack.initSaving(compiler);

        TSTO tsto = null;
        if (!compiler.getCompilerOptions().doesNoCheck()) {
            tsto = new TSTO(0);
            compiler.addInstruction(tsto);
            compiler.addInstruction(new BOV(compiler.procedures.stackOverFlow()));
        }
        this.attributes.codeGenFieldsDefaultInit(compiler);
        if (this.definition.getSuperClass() != null) {
            // Call super constructor
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
            compiler.stack.push(Register.R0);
            compiler.stack.bsr(compiler.labeller.create(this.definition.getSuperClass()));
            compiler.stack.pop(Register.R0);
        }

        // Code de l'initialisation des champs
        this.attributes.codeGenFields(compiler);
        if (tsto != null) {
            tsto.setOperand(new ImmediateInteger(compiler.stack.getMaxTemporariesAllocations()));
        }
        compiler.stack.applyRegisterSavingAndRestore(compiler, markerIndex);
        compiler.addInstruction(new RTS());
        // Code de génération des méthodes
        this.methods.codeGenMethods(compiler);
    }

}
