package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

import static java.util.Objects.requireNonNull;

public class DeclMethod extends Tree {

    private final AbstractType returnType;
    private final AbstractIdentifier name;
    private final ListDeclParam parameters;
    private final AbstractMethodBody body;
    private EnvironmentExp methodEnv = null;
    public DeclMethod(AbstractType returnType, AbstractIdentifier name, ListDeclParam parameters, AbstractMethodBody body) {
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public AbstractIdentifier getName() {
        return name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.returnType.decompile(s);
        s.print(" ");
        this.name.decompile(s);
        s.print("(");
        this.parameters.decompile(s);
        s.print(") ");
        s.indent();
        this.body.decompile(s);
        s.unindent();
    }

    protected void verifyDeclMethodPass2(DecacCompiler compiler, EnvironmentExp classEnv, ClassDefinition currentClass, EnvironmentExp methodEnv) throws ContextualError {
        if (currentClass.getSuperClass() != null) {
            ExpDefinition superDef = currentClass.getSuperClass().getMembers().get(this.name.getName());
            if (superDef instanceof FieldDefinition) {
                throw new ContextualError("Method " + this.name.getName() + " cannot shadow a " + superDef.getNature() + " in the superclass", this.name.getLocation());
            }
        }
        this.methodEnv = methodEnv;
        Type type = this.returnType.verifyType(compiler);
        Signature sig = new Signature();
        for (DeclParam param : this.parameters.getList()) {
            param.verifyParam(compiler);
            Type paramType = param.getType().verifyType(compiler);
            try {
                ParamDefinition definition = new ParamDefinition(paramType, param.getLocation());
                param.getName().setDefinition(definition);
                methodEnv.declare(param.getName().getName(), definition);
            } catch (DoubleDefException e) {
                throw new ContextualError("Param " + param.getName().getName() + " is already declared",
                        param.getLocation());
            }
            sig.add(paramType);
        }

        int index;
        Definition existing = this.methodEnv.get(this.name.getName());
        if (existing instanceof MethodDefinition) {
            MethodDefinition existingMethod = (MethodDefinition) existing;
            Signature existingSignature = existingMethod.getSignature();
            if (sig.size() != existingSignature.size()) {
                throw new ContextualError("Method " + this.name.getName() + " is overridden with different number of parameters", this.name.getLocation());
            }
            for (int i = 0; i < sig.size(); i++) {
                if (!sig.paramNumber(i).sameType(existingSignature.paramNumber(i))) {
                    throw new ContextualError("Method " + this.name.getName() + " is overridden using a " + this.parameters.getList().get(i).decompile() + " parameter, while expecting type " + existingSignature.paramNumber(i), this.name.getLocation());
                }
            }
            if (!existingMethod.getReturnType().isAssignableFrom(type)) {
                throw new ContextualError("Method " + this.name.getName() + " is overridden with a different return type", this.name.getLocation());
            }
            index = existingMethod.getIndex();
        } else {
            index = currentClass.incNumberOfMethods();
        }

        try {
            MethodDefinition methodDefinition = new MethodDefinition(new CallableType(this.name.getName(), type), getLocation(), sig, index);

            Label methodLabel = compiler.labeller.normalize("code." + currentClass.getType().getName().getName() + "." + this.name.getName().getName());
            methodDefinition.setLabel(methodLabel);


            this.name.setDefinition(methodDefinition);
            classEnv.declare(this.name.getName(), methodDefinition);
        } catch (DoubleDefException e) {
            throw new ContextualError("Method " + this.name.getName() + " is already declared",
                    this.name.getLocation());
        }
    }

    protected void verifyDeclMethodPass3(DecacCompiler compiler, EnvironmentExp classEnv, ClassDefinition currentClass) throws ContextualError {
        Type type = this.returnType.verifyType(compiler);
        this.body.verifyMethodBody(compiler, this.methodEnv, currentClass, type);
        ExpDefinition expDefinition = requireNonNull(classEnv.get(this.name.getName()), "method definition should have been set");
        this.name.setDefinition(expDefinition);
    }

    public void codeGenMethod(DecacCompiler compiler) {
        compiler.addLabel(this.name.getMethodDefinition().getLabel());
        int index = -3;
        for (DeclParam param : this.parameters.getList()) {
            ((ParamDefinition) param.getName().getDefinition()).setOperand(new RegisterOffset(index, Register.LB));
            index--;
        }
        body.codeGenMethod(compiler, this.name.getMethodDefinition());
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        this.returnType.prettyPrint(s, prefix, false);
        this.name.prettyPrint(s, prefix, false);
        this.parameters.prettyPrint(s, prefix, false);
        this.body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.returnType.iter(f);
        this.name.iter(f);
        this.parameters.iter(f);
        this.body.iter(f);
    }
}
